STATUS=DOWN
echo "Initial Status: $STATUS"
while true
do
	STATUS=$(curl -s http://localhost:8080/health | jq -r '.status')
	if [ ! -z $STATUS ] && [ $STATUS == "UP" ]
	then
    echo "Keycloak is up"
		break
	else
		echo "Waiting for keycloak to come up"
		sleep 2
	fi
done

echo "Let's create users"

export KEYCLOAK_URL="http://localhost:8080"
export KEYCLOAK_REALM="master"
export KEYCLOAK_USER="admin"
export KEYCLOAK_SECRET="admin"
export REALM_NAME="cbh"

# obtain the access token
echo "Get the admin cli access token"
export ACCESS_TOKEN=$(curl -s -X POST "${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=${KEYCLOAK_USER}" \
  -d "password=${KEYCLOAK_SECRET}" \
  -d "grant_type=password" \
  -d 'client_id=admin-cli' \
  | jq -r '.access_token')

echo "Create cbh-read user"
#Create cbh-read user
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
	-H "Accept: application/json"   \
	-H 'Content-Type: application/json;charset=UTF-8' \
	-H "Authorization: Bearer ${ACCESS_TOKEN}" \
	-d "@/opt/keycloak/bin/cbh-read-user.json"

userid=$(curl -s "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users?username=cbh-read" \
  -H "Accept: application/json"   \
  -H 'Content-Type: application/json;charset=UTF-8'  \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" | jq -r '.[0].id')

#Add cbh-read role to the user
curl -s "http://localhost:8080/admin/realms/cbh/users/${userid}/role-mappings/realm" \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: en-US,en;q=0.9' \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H 'Content-Type: application/json;charset=UTF-8' \
  --data-raw '[{"id":"1ca9422a-6248-4299-bc20-56d0c8747a3f","name":"cbh-readrole","composite":false,"clientRole":false,"containerId":"cbh"}]' \
  --compressed

echo "Create cbh-write role"
#Create cbh-write user
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
	-H "Accept: application/json"   \
	-H 'Content-Type: application/json;charset=UTF-8' \
	-H "Authorization: Bearer ${ACCESS_TOKEN}" \
	-d "@/opt/keycloak/bin/cbh-write-user.json"  

userid=$(curl -s "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users?username=cbh-write" \
  -H "Accept: application/json"   \
  -H 'Content-Type: application/json;charset=UTF-8' \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" | jq -r '.[0].id')

#Create cbh-read role to the user
curl -s "http://localhost:8080/admin/realms/cbh/users/${userid}/role-mappings/realm" \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: en-US,en;q=0.9' \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H 'Content-Type: application/json;charset=UTF-8' \
  --data-raw '[{"id":"1ca9422a-6248-4299-bc20-56d0c8747a3f","name":"cbh-readrole","composite":false,"clientRole":false,"containerId":"cbh"}]' \
  --compressed

#Create cbh-write role to the user
curl -s "http://localhost:8080/admin/realms/cbh/users/${userid}/role-mappings/realm" \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: en-US,en;q=0.9' \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H 'Content-Type: application/json;charset=UTF-8' \
  --data-raw '[{"id":"7510ca27-1276-44a6-90e0-9f5d9d43f380","name":"cbh-writerole","composite":false,"clientRole":false,"containerId":"cbh"}]' \
  --compressed

echo "Create cbh-norole user"
#Create cbh-norole user
curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
	-H "Accept: application/json"   \
	-H 'Content-Type: application/json;charset=UTF-8' \
	-H "Authorization: Bearer ${ACCESS_TOKEN}" \
	-d "@/opt/keycloak/bin/cbh-norole-user.json"  

echo "Keycloak setup for Clipboard Health realm complete."
echo "Application is ready to use."
