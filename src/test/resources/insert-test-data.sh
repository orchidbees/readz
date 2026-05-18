#!/bin/bash

if [[ $(docker ps --format "{{.Names}}") == 'postgres' ]]; then
  docker cp ./seed.sql postgres:/docker-entrypoint-initdb.d/seed.sql

  if docker exec -u postgres postgres psql readz postgres -f docker-entrypoint-initdb.d/seed.sql 2>&1 | grep -q -E 'does not exist'
  then
    echo "[ERROR]: Required tables not found. Check if flyway migrations have taken place."
    exit 1;
  fi
else
  echo "[ERROR]: Postgres does not appear to be running. Try running 'docker compose up'."
  exit 1;
fi