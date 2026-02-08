#!/bin/bash
set -e

ADMIN_PASSWORD="$(tr -dc 'A-Za-z0-9' </dev/urandom | head -c 16 || true)"
export ADMIN_PASSWORD

exec "$@"