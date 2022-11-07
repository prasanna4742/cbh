Feature: Salary summary statistics APIs
	Scenario: Get SS for entire dataset
		Given The cbh api app is running
		And Valid token exists
		When salarySummary API is called	
        Then Response should be 200	
        And SS for entire dataset should be validated

	Scenario: Get SS for on contract employees
		Given The cbh api app is running
		And Valid token exists
		When serchableSalarySummary API is called	
        Then Response should be 200	
        And SS for on contract employees should be validated

	Scenario: Get SS by department
		Given The cbh api app is running
		And Valid token exists
		When salarySummaryByDepartment API is called	
        Then Response should be 200	
        And SS by department should be validated

	Scenario: Get SS by subdepartment
		Given The cbh api app is running
		And Valid token exists
		When salarySummaryBySubDepartment API is called	
        Then Response should be 200	
        And SS by salarySummaryBySubDepartment should be validated
