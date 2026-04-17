#!/usr/bin/env bash
set -euo pipefail

UPLOAD_DIR="${APP_UPLOAD_DIR:-/data/uploads}"
mkdir -p "${UPLOAD_DIR}"

# Soporta DATABASE_URL estilo Render: postgresql://user:pass@host:5432/db
if [ -z "${DB_URL:-}" ] && [ -n "${DATABASE_URL:-}" ]; then
  raw="${DATABASE_URL#postgres://}"
  raw="${raw#postgresql://}"

  creds="${raw%%@*}"
  host_db="${raw#*@}"

  if [ "${host_db}" = "${raw}" ]; then
    creds=""
    host_db="${raw}"
  fi

  host_port="${host_db%%/*}"
  db_part="${host_db#*/}"
  db_name="${db_part%%\?*}"
  query=""
  if [ "${db_part}" != "${db_name}" ]; then
    query="${db_part#*\?}"
  fi

  jdbc_url="jdbc:postgresql://${host_port}/${db_name}"
  if [ -n "${query}" ]; then
    jdbc_url="${jdbc_url}?${query}"
  fi
  export DB_URL="${jdbc_url}"

  if [ -n "${creds}" ]; then
    db_user="${creds%%:*}"
    db_pass="${creds#*:}"
    if [ "${db_user}" != "${creds}" ] && [ -z "${DB_USER:-}" ]; then
      export DB_USER="${db_user}"
    fi
    if [ "${db_user}" != "${creds}" ] && [ -z "${DB_PASSWORD:-}" ]; then
      export DB_PASSWORD="${db_pass}"
    fi
  fi
fi

if [ -z "${DB_URL:-}" ]; then
  echo "Missing DB_URL/DATABASE_URL for PostgreSQL connection" >&2
  exit 1
fi

export DB_DRIVER="${DB_DRIVER:-org.postgresql.Driver}"
export JPA_DIALECT="${JPA_DIALECT:-org.hibernate.dialect.PostgreSQLDialect}"
export JPA_DDL_AUTO="${JPA_DDL_AUTO:-update}"
export APP_FRONTEND_URL="${APP_FRONTEND_URL:-https://ufvshares.onrender.com}"
export APP_API_URL="${APP_API_URL:-https://ufvshares.onrender.com/api}"
export APP_UPLOAD_DIR="${UPLOAD_DIR}"
export APP_SEED_JSON_ENABLED="${APP_SEED_JSON_ENABLED:-true}"
export APP_SEED_JSON_MODE="${APP_SEED_JSON_MODE:-always}"

java -jar /app/app.jar &
PID_BACKEND=$!

nginx -g 'daemon off;' &
PID_NGINX=$!

BACKEND_READY=0
for i in $(seq 1 180); do
  if curl -fsS "http://127.0.0.1:8080/api/hello" >/dev/null 2>&1; then
    BACKEND_READY=1
    break
  fi

  if ! kill -0 "${PID_BACKEND}" 2>/dev/null; then
    echo "Backend process exited before becoming ready" >&2
    exit 1
  fi

  sleep 1
done

if [ "${BACKEND_READY}" -ne 1 ]; then
  echo "Backend did not become ready in time, continuing with nginx up" >&2
fi

term_handler() {
  kill -TERM "${PID_NGINX}" "${PID_BACKEND}" 2>/dev/null || true
}

trap term_handler TERM INT

wait -n "${PID_NGINX}" "${PID_BACKEND}" "${PID_DB}"
EXIT_CODE=$?
term_handler
wait || true
exit "${EXIT_CODE}"
