#!/bin/sh
echo "Starting up cbh keycloak"
echo "Push user creation to background"
/opt/keycloak/bin/createUsers.sh &
echo "Start server"
/opt/keycloak/bin/kc.sh start-dev --import-realm
