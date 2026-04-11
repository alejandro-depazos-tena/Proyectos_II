#!/usr/bin/env bash
set -euo pipefail

DATADIR="${MARIADB_DATADIR:-/data/mysql}"
UPLOAD_DIR="${APP_UPLOAD_DIR:-/data/uploads}"
INIT_MARKER="${DATADIR}/.ufvshares_initialized"

MARIADB_ROOT_PASSWORD="${MARIADB_ROOT_PASSWORD:-root_password_change_me}"
MARIADB_DATABASE="${MARIADB_DATABASE:-ufvshare}"
MARIADB_USER="${MARIADB_USER:-ufvshares}"
MARIADB_PASSWORD="${MARIADB_PASSWORD:-ufvshares_password_change_me}"

mkdir -p "${DATADIR}" "${UPLOAD_DIR}" /run/mysqld
chown -R mysql:mysql "${DATADIR}" /run/mysqld

if [ ! -d "${DATADIR}/mysql" ]; then
  mariadb-install-db --user=mysql --datadir="${DATADIR}" --skip-test-db
fi

mariadbd \
  --user=mysql \
  --datadir="${DATADIR}" \
  --bind-address=127.0.0.1 \
  --port=3306 \
  --socket=/run/mysqld/mysqld.sock &
PID_DB=$!

for i in $(seq 1 60); do
  if mysqladmin ping --host=127.0.0.1 --port=3306 --silent; then
    break
  fi
  sleep 1
done

if ! mysqladmin ping --host=127.0.0.1 --port=3306 --silent; then
  echo "MariaDB did not start correctly" >&2
  exit 1
fi

if [ ! -f "${INIT_MARKER}" ]; then
  mysql -uroot <<SQL
ALTER USER 'root'@'localhost' IDENTIFIED BY '${MARIADB_ROOT_PASSWORD}';
CREATE DATABASE IF NOT EXISTS \`${MARIADB_DATABASE}\`;
CREATE USER IF NOT EXISTS '${MARIADB_USER}'@'%' IDENTIFIED BY '${MARIADB_PASSWORD}';
GRANT ALL PRIVILEGES ON \`${MARIADB_DATABASE}\`.* TO '${MARIADB_USER}'@'%';
FLUSH PRIVILEGES;
SQL

  if [ -f /docker-entrypoint-initdb.d/01_tablas.sql ]; then
    mysql -uroot -p"${MARIADB_ROOT_PASSWORD}" "${MARIADB_DATABASE}" < /docker-entrypoint-initdb.d/01_tablas.sql
  fi

  if [ -f /docker-entrypoint-initdb.d/02_datos.sql ]; then
    mysql -uroot -p"${MARIADB_ROOT_PASSWORD}" "${MARIADB_DATABASE}" < /docker-entrypoint-initdb.d/02_datos.sql
  fi

  touch "${INIT_MARKER}"
fi

export DB_URL="${DB_URL:-jdbc:mariadb://127.0.0.1:3306/${MARIADB_DATABASE}?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC}"
export DB_USER="${DB_USER:-${MARIADB_USER}}"
export DB_PASSWORD="${DB_PASSWORD:-${MARIADB_PASSWORD}}"
export DB_DRIVER="${DB_DRIVER:-org.mariadb.jdbc.Driver}"
export JPA_DIALECT="${JPA_DIALECT:-org.hibernate.dialect.MariaDBDialect}"
export JPA_DDL_AUTO="${JPA_DDL_AUTO:-update}"
export APP_FRONTEND_URL="${APP_FRONTEND_URL:-http://localhost}"
export APP_API_URL="${APP_API_URL:-http://localhost/api}"
export APP_UPLOAD_DIR="${UPLOAD_DIR}"
export APP_SEED_JSON_ENABLED="${APP_SEED_JSON_ENABLED:-false}"

java -jar /app/app.jar &
PID_BACKEND=$!

nginx -g 'daemon off;' &
PID_NGINX=$!

term_handler() {
  kill -TERM "${PID_NGINX}" "${PID_BACKEND}" "${PID_DB}" 2>/dev/null || true
}

trap term_handler TERM INT

wait -n "${PID_NGINX}" "${PID_BACKEND}" "${PID_DB}"
EXIT_CODE=$?
term_handler
wait || true
exit "${EXIT_CODE}"
