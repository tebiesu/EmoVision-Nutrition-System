#!/usr/bin/env bash
set -euo pipefail

echo "[1/4] Build frontend"
(cd frontend && npm ci && npm run build)

echo "[2/4] Build backend"
(cd backend && mvn clean package -DskipTests)

echo "[3/4] Database migration"
mysql -h"${MYSQL_HOST}" -P"${MYSQL_PORT}" -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" "${MYSQL_DB}" < db/schema.sql
mysql -h"${MYSQL_HOST}" -P"${MYSQL_PORT}" -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" "${MYSQL_DB}" < db/seed.sql

echo "[4/4] Start backend"
java -jar backend/target/app.jar --spring.profiles.active="${SPRING_PROFILES_ACTIVE:-prod}"
