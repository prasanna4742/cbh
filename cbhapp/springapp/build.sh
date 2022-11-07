#!/usr/bin/env bash
#Custom keycloak image with preconfigured realm for cbh app
mvn clean install -DskipTests=true
docker build -t prasannakulkarni/cbh-springapp:1.0.0 .
#docker push docker.io/prasannakulkarni/cbh-springapp:1.0.0
