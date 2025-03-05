package com.clozingtag.clozingtag.device.service.controller;

import com.clozingtag.clozingtag.device.service.config.TestContainerConfig;
import com.clozingtag.clozingtag.device.service.dto.DeviceRequest;
import com.clozingtag.clozingtag.device.service.dto.DeviceResponse;
import com.clozingtag.clozingtag.device.service.enums.DeviceState;
import com.clozingtag.clozingtag.device.service.service.DeviceService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestContainerConfig.class, SecurityConfig.class})
class DeviceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService deviceService;

    private DeviceResponse deviceResponse;

    private PageRequest pageRequest;


    @BeforeEach
    void setUp() {
        deviceResponse = DeviceResponse.builder()
                .id(1L)
                .name("Test Device")
                .brand("Test Brand")
                .state(DeviceState.AVAILABLE)
                .creationTime(LocalDateTime.now())
                .build();

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    @WithMockUser(username = "user")
    void createDevice_ValidInput_ReturnsCreated() throws Exception {
        DeviceRequest deviceRequest = DeviceRequest.builder()
                .name("Test Device")
                .brand("Test Brand")
                .state(DeviceState.AVAILABLE)
                .build();

        when(deviceService.createDevice(any(DeviceRequest.class))).thenReturn(deviceResponse);

        mockMvc.perform(post("/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deviceRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Device")))
                .andExpect(jsonPath("$.brand", is("Test Brand")))
                .andExpect(jsonPath("$.state", is("AVAILABLE")));
    }

    @Test
    @WithMockUser(username = "user")
    void getDevice_ExistingId_ReturnsDevice() throws Exception {

        when(deviceService.getDeviceById(1L)).thenReturn(deviceResponse);

        mockMvc.perform(get("/v1/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Device")))
                .andExpect(jsonPath("$.brand", is("Test Brand")))
                .andExpect(jsonPath("$.state", is("AVAILABLE")));
    }

    @Test
    @WithMockUser(username = "user")
    void getAllDevices_ReturnsListOfDevices() throws Exception {
        List<DeviceResponse> devices = Collections.singletonList(deviceResponse);

        when(deviceService.getAllDevices(pageRequest)).thenReturn(devices);

        mockMvc.perform(get("/v1/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Device")))
                .andExpect(jsonPath("$[0].brand", is("Test Brand")))
                .andExpect(jsonPath("$[0].state", is("AVAILABLE")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateDevice_ValidInput_ReturnsUpdatedDevice() throws Exception {
        DeviceRequest deviceRequest = DeviceRequest.builder()
                .name("Updated Device")
                .brand("Updated Device")
                .state(DeviceState.AVAILABLE)
                .build();

        DeviceResponse updatedDeviceResponse = DeviceResponse.builder()
                .id(1L)
                .name("Updated Device")
                .brand("Updated Brand")
                .state(DeviceState.IN_USE)
                .creationTime(LocalDateTime.now())
                .build();

        when(deviceService.updateDevice(any(Long.class), any(DeviceRequest.class))).thenReturn(updatedDeviceResponse);

        mockMvc.perform(put("/v1/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deviceRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Device")))
                .andExpect(jsonPath("$.brand", is("Updated Brand")))
                .andExpect(jsonPath("$.state", is("IN_USE")));
    }

    @Test
    @WithMockUser(username = "user")
    void deleteDevice_ExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/v1/devices/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user")
    void getDevicesByBrand_ReturnsListOfDevices() throws Exception {
        List<DeviceResponse> devices = Collections.singletonList(deviceResponse);

        when(deviceService.getDevicesByBrand("Test Brand", pageRequest)).thenReturn(devices);

        mockMvc.perform(get("/v1/devices/brand/Test Brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Device")))
                .andExpect(jsonPath("$[0].brand", is("Test Brand")))
                .andExpect(jsonPath("$[0].state", is("AVAILABLE")));
    }

    @Test
    @WithMockUser(username = "user")
    void getDevicesByState_ReturnsListOfDevices() throws Exception {

        List<DeviceResponse> devices = Collections.singletonList(deviceResponse);

        when(deviceService.getDevicesByState(DeviceState.AVAILABLE, pageRequest)).thenReturn(devices);


        mockMvc.perform(get("/v1/devices/state/AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Device")))
                .andExpect(jsonPath("$[0].brand", is("Test Brand")))
                .andExpect(jsonPath("$[0].state", is("AVAILABLE")));
    }

    @Test
    @WithMockUser(username = "user")
    void getDevicesByState_InvalidState_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/v1/devices/state/INVALID_STATE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("Invalid state value")));
    }
}