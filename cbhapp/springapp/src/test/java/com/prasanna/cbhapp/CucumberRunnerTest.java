package com.prasanna.cbhapp;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

 
@CucumberOptions(
    plugin = { "pretty", "json:target/cucumber/Cucumber.json", "html:target/site/cucumber-pretty" }, 
    features = {"src/test/resources/features/crudOperations.feature", "src/test/resources/features/summaryStats.feature"}, 
    glue = { "com.prasanna.cbhapp"}
)
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {
}
