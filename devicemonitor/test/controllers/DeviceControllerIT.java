package controllers;

import jpa.models.Customer;
import jpa.models.Device;
import jpa.models.DeviceLog;
import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.WSTestClient;
import play.test.WithServer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test class for DeviceController
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceControllerIT extends WithServer {

    // prepare some common data objects
    private Customer customer = new Customer();
    private Device device = new Device();
    private DeviceLog deviceLog = new DeviceLog();

    // prepare some constant fields that will be used in the test cases
    private static final String customerName = "test";
    private static final Long customerId = System.currentTimeMillis();
    private static final Long deviceId = System.currentTimeMillis();
    private static final Long deviceLogId = System.currentTimeMillis();
    private static final String description = "test device for quality inspection";
    private static final String status = "ACTIVE";
    private static final int executionHours = 12;

    /**
     * TC1: test add customer through /add/customer url
     *
     * input: a new customer object
     * output: the added customer object
     * oracle: assert the attributes of the added customer object
     *
     * @throws Exception
     */
    @Test
    public void tc1_AddCustomerThroughUrl() throws Exception {
        // init customer
        customer = new Customer();
        customer.id = customerId;
        customer.name = customerName;

        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/add/customer").post(Json.toJson(customer));
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            Customer addedCustomer = Json.mapper().readValue(body, Customer.class);
            assertEquals(customer.id, addedCustomer.id);
            assertEquals(customer.name, addedCustomer.name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TC2: test get list of existing customers through /customers url
     *
     * input: no input required
     * output: the existing customers that are persisted in db
     * oracle: assert the most recent added customer object
     *
     * @throws Exception
     */
    @Test
    public void tc2_GetCustomersThroughUrl() throws Exception {
        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/customers").get();
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            List<Customer> retrievedCustomer = Arrays.asList(Json.mapper().readValue(body, Customer[].class));

            // sort the list by id in descending order
            Collections.sort(retrievedCustomer, new Comparator<Customer>() {
                @Override
                public int compare(Customer c1, Customer c2) {
                    return c2.getId().compareTo(c1.getId());
                }
            });

            assertTrue(retrievedCustomer.size() >= 1);
            assertEquals(customerId, retrievedCustomer.get(0).id);
            assertEquals(customerName, retrievedCustomer.get(0).name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TC3: test add a new device through /add/device url
     *
     * input: a new device object
     * output: the added device object
     * oracle: assert the attributes of the added device object
     *
     * @throws Exception
     */
    @Test
    public void tc3_AddDeviceThroughUrl() throws Exception {
        // init device
        device.id = deviceId;
        device.description = description;
        device.customerId = customerId;
        device.status = status;
        device.updateAt = deviceId;

        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/add/device").post(Json.toJson(device));
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            Device addedDevice = Json.mapper().readValue(body, Device.class);
            assertEquals(device.id, addedDevice.id);
            assertEquals(device.description, addedDevice.description);
            assertEquals(device.customerId, addedDevice.customerId);
            assertEquals(device.status, addedDevice.status);
            assertEquals(device.updateAt, addedDevice.updateAt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TC4: test get the list of existing and valid devices through /devices url
     *
     * input: no input required
     * output: the list of valid devices existed in current db
     * oracle: assert the most recent added device object
     *
     * @throws Exception
     */
    @Test
    public void tc4_GetDevicesThroughUrl() throws Exception {
        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/devices").get();
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            List<Device> retrievedDevices = Arrays.asList(Json.mapper().readValue(body, Device[].class));
            assertTrue(retrievedDevices.size() >= 1);

            // sort the list by id in descending order
            Collections.sort(retrievedDevices, new Comparator<Device>() {
                @Override
                public int compare(Device d1, Device d2) {
                    return d2.getId().compareTo(d1.getId());
                }
            });

            assertEquals(deviceId, retrievedDevices.get(0).id);
            assertEquals(description, retrievedDevices.get(0).description);
            assertEquals(customerId, retrievedDevices.get(0).customerId);
            assertEquals(status, retrievedDevices.get(0).status);
            assertEquals(deviceId, retrievedDevices.get(0).updateAt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TC5 - test update an existing device through /update url
     *
     * input: the device log object
     * output: the added device log object
     * oracle: assert the attributes of the added device log
     *
     * @throws Exception
     */
    @Test
    public void tc5_UpdateDeviceThroughUrl() throws Exception {
        // init device log
        deviceLog.id = deviceLogId;
        deviceLog.deviceId = deviceId;
        deviceLog.status = status;
        deviceLog.executionHours = executionHours;
        deviceLog.updateAt = deviceLogId;

        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/update").post(Json.toJson(deviceLog));
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            DeviceLog addedLog = Json.mapper().readValue(body, DeviceLog.class);
            assertEquals(deviceLog.id, addedLog.id);
            assertEquals(deviceLog.deviceId, addedLog.deviceId);
            assertEquals(deviceLog.status, addedLog.status);
            assertEquals(deviceLog.executionHours, addedLog.executionHours);
            assertEquals(deviceLog.updateAt, addedLog.updateAt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TC6: test check device info through /check/deviceId url
     *
     * input: the device id
     * output: the latest updated device log
     * oracle: assert the attributes of most recent added log for the device
     *
     * @throws Exception
     */
    @Test
    public void tc6_CheckDeviceThroughUrl() throws Exception {
        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/check/".concat(String.valueOf(deviceId))).get();
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            DeviceLog deviceLog = Json.mapper().readValue(body, DeviceLog.class);
            assertEquals(deviceLogId, deviceLog.id);
            assertEquals(deviceId, deviceLog.deviceId);
            assertEquals(status, deviceLog.status);
            assertEquals(executionHours, deviceLog.executionHours);
            assertEquals(deviceLogId, deviceLog.updateAt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TC7: test remove device through /remove/deviceId url
     *
     * input: the device id
     * output: the id of the removed device
     * oracle: the returned id shall be the requested device id
     *
     * @throws Exception
     */
    @Test
    public void tc7_RemoveDeviceThroughUrl() throws Exception {
        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/remove/".concat(String.valueOf(deviceId))).post(StringUtils.EMPTY);
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            Long deviceId = Json.mapper().readValue(body, Long.class);
            assertEquals(this.deviceId, deviceId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
