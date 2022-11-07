Feature: Employee record add, delete and get API
	Scenario: Create an employee record
		Given The cbh api app is running
		And Valid token exists
		When POST operation on employees is performed
		Then Response should be 201

	Scenario: Get employee record
		Given The cbh api app is running
		And Valid token exists
		When Get operation on employees is performed
		Then Response should be 200

	Scenario: Delete employee record
		Given The cbh api app is running
		And Valid token exists
		When Delete operation on employees is performed
		Then Response should be 204

	Scenario: Create an employee record
		Given The cbh api app is running
		And Token does not exist
		When POST operation on employees is performed
		Then Response should be 401

	Scenario: Get employee record
		Given The cbh api app is running
		And Token does not exist
		When Get operation on employees is performed
		Then Response should be 401

	Scenario: Delete employee record
		Given The cbh api app is running
		And Token does not exist
		When Delete operation on employees is performed
		Then Response should be 401

	Scenario: Create an employee record
		Given The cbh api app is running
		And Invalid token exists
		When POST operation on employees is performed
		Then Response should be 403

	Scenario: Get employee record
		Given The cbh api app is running
		And Invalid token exists
		When Get operation on employees is performed
		Then Response should be 403

	Scenario: Delete employee record
		Given The cbh api app is running
		And Invalid token exists
		When Delete operation on employees is performed
		Then Response should be 403
