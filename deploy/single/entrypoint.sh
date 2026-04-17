#!/usr/bin/env bash
set -euo pipefail

UPLOAD_DIR="${APP_UPLOAD_DIR:-/data/uploads}"
mkdir -p "${UPLOAD_DIR}"

EMBEDDED_PG="${APP_EMBEDDED_POSTGRES:-true}"
PGDATA_DIR="${PGDATA_DIR:-/data/postgres}"
POSTGRES_DB="${POSTGRES_DB:-ufvsharesdb}"
POSTGRES_USER="${POSTGRES_USER:-ufvshares}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD:-ufvshares_password_change_me}"

PID_DB=""

INITDB_BIN="$(command -v initdb || true)"
POSTGRES_BIN="$(command -v postgres || true)"
PG_ISREADY_BIN="$(command -v pg_isready || true)"
PSQL_BIN="$(command -v psql || true)"

if [ -z "${INITDB_BIN}" ]; then
  INITDB_BIN="$(find /usr/lib/postgresql -type f -name initdb 2>/dev/null | sort | tail -n1 || true)"
fi
if [ -z "${POSTGRES_BIN}" ]; then
  POSTGRES_BIN="$(find /usr/lib/postgresql -type f -name postgres 2>/dev/null | sort | tail -n1 || true)"
fi
if [ -z "${PG_ISREADY_BIN}" ]; then
  PG_ISREADY_BIN="$(find /usr/lib/postgresql -type f -name pg_isready 2>/dev/null | sort | tail -n1 || true)"
fi
if [ -z "${PSQL_BIN}" ]; then
  PSQL_BIN="$(find /usr/lib/postgresql -type f -name psql 2>/dev/null | sort | tail -n1 || true)"
fi

if [ -z "${INITDB_BIN}" ] || [ -z "${POSTGRES_BIN}" ] || [ -z "${PG_ISREADY_BIN}" ] || [ -z "${PSQL_BIN}" ]; then
  echo "PostgreSQL binaries not found (initdb/postgres/pg_isready/psql)" >&2
  exit 1
fi

start_embedded_postgres() {
  mkdir -p "${PGDATA_DIR}" /run/postgresql
  chown -R postgres:postgres "${PGDATA_DIR}" /run/postgresql
  chmod 700 "${PGDATA_DIR}"

  if [ ! -s "${PGDATA_DIR}/PG_VERSION" ]; then
    runuser -u postgres -- "${INITDB_BIN}" -D "${PGDATA_DIR}" --encoding=UTF8 --locale=C
  fi

  runuser -u postgres -- "${POSTGRES_BIN}" -D "${PGDATA_DIR}" -h 127.0.0.1 -p 5432 &
  PID_DB=$!

  for i in $(seq 1 60); do
    if runuser -u postgres -- "${PG_ISREADY_BIN}" -h 127.0.0.1 -p 5432 >/dev/null 2>&1; then
      break
    fi
    sleep 1
  done

  if ! runuser -u postgres -- "${PG_ISREADY_BIN}" -h 127.0.0.1 -p 5432 >/dev/null 2>&1; then
    echo "Embedded PostgreSQL did not start correctly" >&2
    exit 1
  fi

  esc_user="${POSTGRES_USER//\'/\'\'}"
  esc_pass="${POSTGRES_PASSWORD//\'/\'\'}"
  esc_db="${POSTGRES_DB//\'/\'\'}"

  runuser -u postgres -- "${PSQL_BIN}" --dbname=postgres -v ON_ERROR_STOP=1 <<SQL
DO \$\$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = '${esc_user}') THEN
    EXECUTE format('CREATE ROLE %I LOGIN PASSWORD %L', '${esc_user}', '${esc_pass}');
  ELSE
    EXECUTE format('ALTER ROLE %I LOGIN PASSWORD %L', '${esc_user}', '${esc_pass}');
  END IF;
END \$\$;

SELECT format('CREATE DATABASE %I OWNER %I', '${esc_db}', '${esc_user}')
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = '${esc_db}')
\gexec
SQL

  export DB_URL="jdbc:postgresql://127.0.0.1:5432/${POSTGRES_DB}"
  export DB_USER="${POSTGRES_USER}"
  export DB_PASSWORD="${POSTGRES_PASSWORD}"
}

if [ "${EMBEDDED_PG}" = "true" ]; then
  start_embedded_postgres
fi

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

# Fallback 1: variables PG* (comunes en proveedores Postgres gestionados)
if [ -z "${DB_URL:-}" ] && [ -n "${PGHOST:-}" ] && [ -n "${PGDATABASE:-}" ]; then
  pg_port="${PGPORT:-5432}"
  export DB_URL="jdbc:postgresql://${PGHOST}:${pg_port}/${PGDATABASE}"
  if [ -z "${DB_USER:-}" ] && [ -n "${PGUSER:-}" ]; then
    export DB_USER="${PGUSER}"
  fi
  if [ -z "${DB_PASSWORD:-}" ] && [ -n "${PGPASSWORD:-}" ]; then
    export DB_PASSWORD="${PGPASSWORD}"
  fi
fi

# Fallback 2: variables POSTGRES_* / DB_* (estilo docker-compose / custom)
if [ -z "${DB_URL:-}" ]; then
  db_host="${DB_HOST:-${POSTGRES_HOST:-}}"
  db_port="${DB_PORT:-${POSTGRES_PORT:-5432}}"
  db_name="${DB_NAME:-${POSTGRES_DB:-}}"
  if [ -n "${db_host}" ] && [ -n "${db_name}" ]; then
    export DB_URL="jdbc:postgresql://${db_host}:${db_port}/${db_name}"
  fi
fi

if [ -z "${DB_USER:-}" ]; then
  if [ -n "${POSTGRES_USER:-}" ]; then
    export DB_USER="${POSTGRES_USER}"
  fi
fi

if [ -z "${DB_PASSWORD:-}" ]; then
  if [ -n "${POSTGRES_PASSWORD:-}" ]; then
    export DB_PASSWORD="${POSTGRES_PASSWORD}"
  fi
fi

if [ -z "${DB_URL:-}" ]; then
  echo "Missing PostgreSQL connection variables. Set one of: DATABASE_URL, DB_URL, or PGHOST+PGDATABASE (optionally PGPORT/PGUSER/PGPASSWORD), or DB_HOST/DB_NAME (+ DB_PORT/DB_USER/DB_PASSWORD)." >&2
  exit 1
fi

export DB_DRIVER="${DB_DRIVER:-org.postgresql.Driver}"
export JPA_DIALECT="${JPA_DIALECT:-org.hibernate.dialect.PostgreSQLDialect}"
export JPA_DDL_AUTO="${JPA_DDL_AUTO:-update}"
export APP_FRONTEND_URL="${APP_FRONTEND_URL:-https://ufvshares.onrender.com}"
export APP_API_URL="${APP_API_URL:-https://ufvshares.onrender.com/api}"
export APP_UPLOAD_DIR="${UPLOAD_DIR}"
export APP_MAIL_DEV_FALLBACK="${APP_MAIL_DEV_FALLBACK:-false}"
export MAIL_HOST="${MAIL_HOST:-smtp.gmail.com}"
export MAIL_PORT="${MAIL_PORT:-587}"
export MAIL_USERNAME="${MAIL_USERNAME:-soporteufvshares@gmail.com}"
export MAIL_PASSWORD="${MAIL_PASSWORD:-uwozumaougrihsqb}"
if [ -z "${MAIL_USERNAME:-}" ] && [ -n "${MAIL_USER:-}" ]; then
  export MAIL_USERNAME="${MAIL_USER}"
fi
if [ -z "${MAIL_FROM:-}" ] && [ -n "${MAIL_USERNAME:-}" ]; then
  export MAIL_FROM="${MAIL_USERNAME}"
fi
export APP_SEED_JSON_ENABLED="${APP_SEED_JSON_ENABLED:-true}"
export APP_SEED_JSON_MODE="${APP_SEED_JSON_MODE:-always}"
export APP_SEED_BOOTSTRAP_ADMIN_ENABLED="${APP_SEED_BOOTSTRAP_ADMIN_ENABLED:-true}"
export APP_SEED_BOOTSTRAP_ADMIN_EMAIL="${APP_SEED_BOOTSTRAP_ADMIN_EMAIL:-admin@ufv.es}"
export APP_SEED_BOOTSTRAP_ADMIN_PASSWORD="${APP_SEED_BOOTSTRAP_ADMIN_PASSWORD:-1234ASDF}"

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
  if [ -n "${PID_DB}" ]; then
    kill -TERM "${PID_NGINX}" "${PID_BACKEND}" "${PID_DB}" 2>/dev/null || true
  else
    kill -TERM "${PID_NGINX}" "${PID_BACKEND}" 2>/dev/null || true
  fi
}

trap term_handler TERM INT

if [ -n "${PID_DB}" ]; then
  wait -n "${PID_NGINX}" "${PID_BACKEND}" "${PID_DB}"
else
  wait -n "${PID_NGINX}" "${PID_BACKEND}"
fi
EXIT_CODE=$?
term_handler
wait || true
exit "${EXIT_CODE}"
