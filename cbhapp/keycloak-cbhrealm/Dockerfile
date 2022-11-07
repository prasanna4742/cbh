FROM quay.io/keycloak/keycloak:18.0.0
COPY jq-linux64 /bin/jq
COPY realm-export.json /opt/keycloak/data/import/cbh-realm.json
# COPY createUsers.sh /opt/keycloak/bin/createUsers.sh
# COPY cbh-read-user.json /opt/keycloak/bin/cbh-read-user.json
# COPY cbh-write-user.json /opt/keycloak/bin/cbh-write-user.json
# COPY entrypoint.sh /opt/keycloak/bin/entrypoint.sh
COPY scripts/* /opt/keycloak/bin/

ENV KC_HEALTH_ENABLED=true
#ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]
ENTRYPOINT ["/opt/keycloak/bin/entrypoint.sh"]