#!/bin/bash
set -e
set -u

function create_user_and_database() {
local database=$1
echo "  Creating user and database '$database'"
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE $database;
    GRANT ALL PRIVILEGES ON DATABASE $database TO $POSTGRES_USER;
EOSQL
}

if [ -n "$POSTGRES_DB" ]; then
echo "Multiple database creation requested"
create_user_and_database db_clientes
create_user_and_database db_cuentas
echo "Multiple databases created"
fi