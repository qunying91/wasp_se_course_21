package controllers;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.Customer;
import models.Device;
import models.DeviceLog;
import models.DeviceRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Test;
import play.data.FormFactory;
import play.data.format.Formatters;
import play.http.HttpEntity;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for DeviceController
 */
public class DeviceControllerUT {

    // prepare some common mockups
    private DeviceRepository deviceRepository;
    private Messages msg;
    private MessagesApi msgApi;
    private DeviceController deviceController;

    // prepare some common data models
    private Customer customer = new Customer();
    private Device device = new Device();
    private DeviceLog deviceLog = new DeviceLog();

    // init mocks and prepare common setup
    public void mockup() {
        deviceRepository = mock(DeviceRepository.class);
        msg = mock(Messages.class);
        msgApi = mock(MessagesApi.class);

        ValidatorFactory factory = Validation.byDefaultProvider().configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();

        Config config = ConfigFactory.load();
        FormFactory formFactory = new FormFactory(msgApi, new Formatters(msgApi), factory, config);

        HttpExecutionContext execCxt = new HttpExecutionContext(ForkJoinPool.commonPool());
        deviceController = new DeviceController(formFactory, deviceRepository, execCxt);
    }

    /**
     * TC1: test addCustomer feature from DeviceController class using the prepared mocks
     *
     *  input: a new customer
     *  output: response with the added customer object
     *  oracle: response status is ok, response content is the added customer object
     *
     * @throws Exception
     */
    @Test
    public void testAddCustomer() throws Exception {
        initCustomer();
        mockup();

        when(deviceRepository.addCustomer(any())).thenReturn(supplyAsync(() -> customer));

        // prepare the http request
        Http.Request req = Helpers.fakeRequest("POST", "/add/customer")
                .bodyJson(Json.toJson(customer)).build().withTransientLang(Lang.forCode("en-US"));

        when(msgApi.preferred(req)).thenReturn(msg);

        // call function addCustomer
        CompletionStage<Result> completionStage = deviceController.addCustomer(req);

        // verify function call complete and success
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(completionStage.toCompletableFuture()).isCompletedWithValueMatching(
                        result -> result.status() == Http.Status.OK, "should return added customer"
                )
        );

        Result result = completionStage.toCompletableFuture().get();
        String body = ((HttpEntity.Strict) result.body()).data().decodeString("utf-8");
        Customer addedCustomer = Json.mapper().readValue(body, Customer.class);

        // verify attributes in the response body
        assertEquals("customer id should be the same", customer.id, addedCustomer.id);
        assertEquals("customer name should be the same", customer.name, addedCustomer.name);
    }

    /**
     * TC2: test getCustomers feature from DeviceController class using the prepared mocks
     *
     *  input: no test input required
     *  output: response with the a list of existing customers
     *  oracle: response status is ok, response content is the list of existing customers
     *
     * @throws Exception
     */
    @Test
    public void testGetCustomers() throws Exception{
        initCustomer();
        mockup();

        // prepare the customer list
        List<Customer> customers = new ArrayList<Customer>();
        customers.add(customer);

        when(deviceRepository.listCustomers()).thenReturn(supplyAsync(() -> customers.stream()));

        // prepare the http request
        Http.Request req = Helpers.fakeRequest("GET", "/customers")
                .build().withTransientLang(Lang.forCode("en-US"));

        when(msgApi.preferred(req)).thenReturn(msg);

        // call function getCustomer
        CompletionStage<Result> completionStage = deviceController.getCustomers();

        // verify function call complete and success
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(completionStage.toCompletableFuture()).isCompletedWithValueMatching(
                        result -> result.status() == Http.Status.OK, "should return existing customers"
                )
        );

        Result result = completionStage.toCompletableFuture().get();
        String body = ((HttpEntity.Strict) result.body()).data().decodeString("utf-8");
        List<Customer> retrievedCustomers = Arrays.asList(Json.mapper().readValue(body, Customer[].class));

        // verify attributes in the response body
        assertTrue("retrieved customer list contains one entry", retrievedCustomers.size() == 1);
        assertEquals("customer id should be the same", customer.id, retrievedCustomers.get(0).id);
        assertEquals("customer name should be the same", customer.name, retrievedCustomers.get(0).name);
    }

    /**
     * TC3: test addDevice feature from DeviceController class using the prepared mocks
     *
     *  input: a new device
     *  output: response with the added device object
     *  oracle: response status is ok, response content is the device added
     *
     * @throws Exception
     */
    @Test
    public void testAddDevice() throws Exception {
        initCustomer();
        initDevice();
        mockup();

        when(deviceRepository.addDevice(any())).thenReturn(supplyAsync(() -> device));

        // prepare the http request
        Http.Request req = Helpers.fakeRequest("POST", "/add/device")
                .bodyJson(Json.toJson(device)).build().withTransientLang(Lang.forCode("en-US"));

        when(msgApi.preferred(req)).thenReturn(msg);

        // call function addDevice
        CompletionStage<Result> completionStage = deviceController.addDevice(req);

        // verify function call complete and success
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(completionStage.toCompletableFuture()).isCompletedWithValueMatching(
                        result -> result.status() == Http.Status.OK, "should return added device"
                )
        );

        Result result = completionStage.toCompletableFuture().get();
        String body = ((HttpEntity.Strict) result.body()).data().decodeString("utf-8");
        Device addedDevice = Json.mapper().readValue(body, Device.class);

        // verify attributes in the response body
        assertEquals("device id should be the same", device.id, addedDevice.id);
        assertEquals("device customer id should be the same", device.customerId, addedDevice.customerId);
        assertEquals("device status should be the same", device.status, addedDevice.status);
        assertEquals("device updated time should be the same", device.updateAt, addedDevice.updateAt);
    }

    /**
     * TC4: test getDevices feature from DeviceController class using the prepared mocks
     *
     *  input: no test input required
     *  output: response with a list of existing and valid devices
     *  oracle: response status is ok, response content is the list of existing devices that are still valid.
     *
     * @throws Exception
     */
    @Test
    public void testGetDevices() throws Exception{
        initCustomer();
        initDevice();
        mockup();

        // prepare the device list
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);

        when(deviceRepository.listDevices()).thenReturn(supplyAsync(() -> devices.stream()));

        // prepare the http request
        Http.Request req = Helpers.fakeRequest("GET", "/devices")
                .build().withTransientLang(Lang.forCode("en-US"));

        when(msgApi.preferred(req)).thenReturn(msg);

        // call function getDevices
        CompletionStage<Result> completionStage = deviceController.getDevices();

        // verify function call complete and success
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(completionStage.toCompletableFuture()).isCompletedWithValueMatching(
                        result -> result.status() == Http.Status.OK, "should return existing and valid devices"
                )
        );

        Result result = completionStage.toCompletableFuture().get();
        String body = ((HttpEntity.Strict) result.body()).data().decodeString("utf-8");
        List<Device> retrievedDevices = Arrays.asList(Json.mapper().readValue(body, Device[].class));

        // verify attributes in the response body
        assertTrue("retrieved device list contains one entry", retrievedDevices.size() == 1);

        // verify attributes in the response body
        assertEquals("device id should be the same", device.id, retrievedDevices.get(0).id);
        assertEquals("device customer id should be the same", device.customerId, retrievedDevices.get(0).customerId);
        assertEquals("device status should be the same", device.status, retrievedDevices.get(0).status);
        assertEquals("device updated time should be the same", device.updateAt, retrievedDevices.get(0).updateAt);
    }

    /**
     * TC5: test removeDevice feature from DeviceController class using the prepared mocks
     *
     *  input: id of an existing device
     *  output: response with the id of the removed device
     *  oracle: response status is ok, response content is the id of the device removed
     *
     * @throws Exception
     */
    @Test
    public void testRemoveDevice() throws Exception{
        initCustomer();
        initDevice();
        mockup();

        when(deviceRepository.removeDevice(device.id)).thenReturn(supplyAsync(() -> device.id));

        // prepare the http request
        Http.Request req = Helpers.fakeRequest("POST", "/remove/101175")
                .build().withTransientLang(Lang.forCode("en-US"));

        when(msgApi.preferred(req)).thenReturn(msg);

        // call function removeDevice
        CompletionStage<Result> completionStage = deviceController.removeDevice(device.id);

        // verify function call complete and success
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(completionStage.toCompletableFuture()).isCompletedWithValueMatching(
                        result -> result.status() == Http.Status.OK, "should return id from the removed device"
                )
        );

        Result result = completionStage.toCompletableFuture().get();
        String body = ((HttpEntity.Strict) result.body()).data().decodeString("utf-8");
        Long removedDeviceId = Json.mapper().readValue(body, Long.class);

        // verify attributes in the response body
        assertEquals("device id should be the same", device.id, removedDeviceId);
    }

    /**
     * TC6: test updateDevice feature from DeviceController class using the prepared mocks
     *
     *  input: a device log
     *  output: response with the added device log object
     *  oracle: response status is ok, response content is the device log added
     *
     * @throws Exception
     */
    @Test
    public void testUpdateDevice() throws Exception{
        initCustomer();
        initDevice();
        initDeviceLog();
        mockup();

        when(deviceRepository.updateDevice(any())).thenReturn(supplyAsync(() -> deviceLog));

        // prepare the http request
        Http.Request req = Helpers.fakeRequest("POST", "/update")
                .bodyJson(Json.toJson(deviceLog)).build().withTransientLang(Lang.forCode("en-US"));

        when(msgApi.preferred(req)).thenReturn(msg);

        // call function checkDevice
        CompletionStage<Result> completionStage = deviceController.updateDevice(req);

        // verify function call complete and success
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(completionStage.toCompletableFuture()).isCompletedWithValueMatching(
                        result -> result.status() == Http.Status.OK, "should return the add device log"
                )
        );

        Result result = completionStage.toCompletableFuture().get();
        String body = ((HttpEntity.Strict) result.body()).data().decodeString("utf-8");
        DeviceLog addedDeviceLog = Json.mapper().readValue(body, DeviceLog.class);

        // verify attributes in the response body
        assertEquals("device log id should be the same", deviceLog.id, addedDeviceLog.id);
        assertEquals("device id should be the same", deviceLog.deviceId, addedDeviceLog.deviceId);
        assertEquals("device status should be the same", deviceLog.status, addedDeviceLog.status);
        assertEquals("device execution hour should be the same", deviceLog.executionHours, addedDeviceLog.executionHours);
        assertEquals("device updated time should be the same", deviceLog.updateAt, addedDeviceLog.updateAt);
    }

    /**
     * TC7: test checkDevice feature from DeviceController class using the prepared mocks
     *
     *  input: id of an existing device
     *  output: response with the latest updated device log of the given device id
     *  oracle: response status is ok, response content is the latest updated device log
     *
     * @throws Exception
     */
    @Test
    public void testCheckDevice() throws Exception{
        initCustomer();
        initDevice();
        initDeviceLog();
        mockup();

        when(deviceRepository.checkDevice(device.id)).thenReturn(supplyAsync(() -> deviceLog));

        // prepare the http request
        Http.Request req = Helpers.fakeRequest("POST", "/check/101175")
                .build().withTransientLang(Lang.forCode("en-US"));

        when(msgApi.preferred(req)).thenReturn(msg);

        // call function checkDevice
        CompletionStage<Result> completionStage = deviceController.checkDevice(device.id);

        // verify function call complete and success
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(completionStage.toCompletableFuture()).isCompletedWithValueMatching(
                        result -> result.status() == Http.Status.OK, "should return latest updated device info"
                )
        );

        Result result = completionStage.toCompletableFuture().get();
        String body = ((HttpEntity.Strict) result.body()).data().decodeString("utf-8");
        DeviceLog fetchedDeviceLog = Json.mapper().readValue(body, DeviceLog.class);

        // verify attributes in the response body
        assertEquals("device log id should be the same", deviceLog.id, fetchedDeviceLog.id);
        assertEquals("device id should be the same", deviceLog.deviceId, fetchedDeviceLog.deviceId);
        assertEquals("device status should be the same", deviceLog.status, fetchedDeviceLog.status);
        assertEquals("device execution hour should be the same", deviceLog.executionHours, fetchedDeviceLog.executionHours);
        assertEquals("device updated time should be the same", deviceLog.updateAt, fetchedDeviceLog.updateAt);
    }

    private void initCustomer() {
        customer.id = 101175L;
        customer.name = "absT4";
    }

    private void initDevice() {
        device.id = 203375L;
        device.customerId = customer.id;
        device.status = "ACTIVE";
        device.updateAt = System.currentTimeMillis();
    }

    private void initDeviceLog() {
        deviceLog.id = 390078L;
        deviceLog.deviceId = device.id;
        deviceLog.executionHours = 10;
        deviceLog.status = "ACTIVE";
        deviceLog.updateAt = System.currentTimeMillis();
        deviceLog.error = StringUtils.EMPTY;
    }
}
