package com.clozingtag.clozingtag.device.service.integration;

import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.platform.suite.api.SelectClasspathResource;

@Cucumber
@SelectClasspathResource("features")
public class CucumberTestRunner {
}