#!/usr/bin/env bash
#Custom keycloak image with preconfigured realm for cbh app
docker build -t prasannakulkarni/cbh-keycloak:1.0.0 .
#docker push docker.io/prasannakulkarni/cbh-keycloak:1.0.0
