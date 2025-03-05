package com.clozingtag.clozingtag.device.service.integration;

import com.clozingtag.clozingtag.device.service.config.JwtHelper;
import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.entity.DeviceEntity;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import com.clozingtag.clozingtag.device.service.exception.DeviceNotFoundException;
import com.clozingtag.clozingtag.device.service.repository.DeviceRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceSteps extends CucumberSpringConfiguration{

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DeviceRepository deviceRepository;

    private DeviceRequest deviceRequest;

    private ResponseEntity<DeviceResponse> response;

    private HttpHeaders headers;


    @Before
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeAll
    public  void globalSetup() {
        deviceRepository.deleteAll();
    }

    @Given("I am authenticated as {string} with role {string}")
    public void iAmAuthenticatedAs(String username, String role) throws Exception{
        List<String> roles = List.of(role);
        String accessToken = JwtHelper.createJwt(username, roles, "test-issuer");
        headers.set("Authorization", "Bearer " + accessToken);
    }

    @Given("a device with name {string}, brand {string}, and state {string}")
    public void aDeviceWithNameBrandAndState(String name, String brand, String state) {
        deviceRequest = DeviceRequest.builder()
                .brand(brand)
                .name(name)
                .state(DeviceState.valueOf(state))
                .build();
    }


    @When("I send a POST request to {string} with the device data")
    public void iSendAPOSTRequestToWithTheDeviceData(String endpoint) {
        HttpEntity<DeviceRequest> request = new HttpEntity<>(deviceRequest, headers);
        response = restTemplate.exchange(endpoint, HttpMethod.POST, request, DeviceResponse.class);
    }


    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, DeviceResponse.class);
    }

    @When("I send a PUT request to {string} with new state {string}")
    public void iSendAPUTRequestToWithNewState(String endpoint, String newState) {
        DeviceEntity existingDevice = deviceRepository.findById(1L).orElseThrow(() -> new DeviceNotFoundException("Device not found")); // Assuming device id is 1
        existingDevice.setState(DeviceState.valueOf(newState)); // change the state.
        HttpEntity<DeviceEntity> requestEntity = new HttpEntity<>(existingDevice, headers);
        response = restTemplate.exchange(endpoint, HttpMethod.PUT, requestEntity, DeviceResponse.class);
    }


    @When("I send a PUT partial request to {string} with new state {string}")
    public void iSendAPUTPartialRequestToWithNewState(String endpoint, String newState) {
        deviceRepository.findById(1L).orElseThrow(() -> new DeviceNotFoundException("Device not found")); // Assuming device id is 1
        String urlWithState = endpoint + "?deviceState=" + newState;
        HttpEntity<DeviceEntity> requestEntity = new HttpEntity<>(headers);
        response = restTemplate.exchange(urlWithState, HttpMethod.PATCH, requestEntity, DeviceResponse.class);
    }


    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, DeviceResponse.class);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @Then("the response body should contain the device name {string}")
    public void theResponseBodyShouldContainTheDeviceName(String name) {
        assertNotNull(response.getBody());
        assertEquals(name, response.getBody().name());
    }

    @Then("the response body should contain the device brand {string}")
    public void theResponseBodyShouldContainTheDeviceBrand(String brand) {
        assertNotNull(response.getBody());
        assertEquals(brand, response.getBody().brand());
    }

    @Then("the response body should contain the device state {string}")
    public void theResponseBodyShouldContainTheDeviceState(String state) {
        assertNotNull(response.getBody());
        assertEquals(DeviceState.valueOf(state), response.getBody().state());
    }

    @Then("the device should be deleted from the database")
    public void theDeviceShouldBeDeletedFromTheDatabase() {
        assertFalse(deviceRepository.existsById(1L)); // Assuming device id is 1
    }
}