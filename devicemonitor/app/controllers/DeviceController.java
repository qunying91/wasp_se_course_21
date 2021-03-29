package controllers;

import jpa.models.Customer;
import jpa.models.Device;
import jpa.models.DeviceLog;
import jpa.DeviceRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * This controller contains actions to handle HTTP requests
 * to add, remove, list, update, or check the device info.
 *
 * @author Song
 */
public class DeviceController extends Controller {

    private final FormFactory formFactory;
    private final DeviceRepository deviceRepository;
    private final HttpExecutionContext execCxt;

    @Inject
    public DeviceController(FormFactory formFactory, DeviceRepository deviceRepository, HttpExecutionContext execCxt) {
        this.formFactory = formFactory;
        this.deviceRepository = deviceRepository;
        this.execCxt = execCxt;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     * An action that persists a new customer into data storage.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>POST</code> request with a path of <code>/add/customer</code>.
     *
     * @param request http request that contains the customer object
     * @return CompletionStage<Result> contains the added customer
     */
    public CompletionStage<Result> addCustomer(final Http.Request request) {
        Customer customer = formFactory.form(Customer.class).bindFromRequest(request).get();
        return deviceRepository
                .addCustomer(customer)
                .thenApplyAsync(customerAdded -> ok(toJson(customerAdded)), execCxt.current());
    }

    /**
     * An action that retrieves all customer from data storage.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/customers</code>.
     *
     * @return CompletionStage<Result> contains the list of existing customers
     */
    public CompletionStage<Result> getCustomers() {
        return deviceRepository
                .listCustomers()
                .thenApplyAsync(customerStream -> ok(toJson(customerStream.collect(Collectors.toList()))), execCxt.current());
    }

    /**
     * An action that persists a new device into the data storage.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>POST</code> request with a path of <code>/add/device</code>.
     *
     * @param request http request that contains the device object
     * @return CompletionStage<Result> contains the added device
     */
    public CompletionStage<Result> addDevice(final Http.Request request) {
        Device device = formFactory.form(Device.class).bindFromRequest(request).get();
        return deviceRepository
                .addDevice(device)
                .thenApplyAsync(deviceAdded -> ok(toJson(deviceAdded)), execCxt.current());
    }

    /**
     * An action that retrieves all valid devices from data storage.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/devices</code>.
     *
     * @return CompletionStage<Result> contains the list of all valid and existing devices
     */
    public CompletionStage<Result> getDevices() {
        return deviceRepository
                .listDevices()
                .thenApplyAsync(deviceStream -> ok(toJson(deviceStream.collect(Collectors.toList()))), execCxt.current());
    }

    /**
     * An action that removes an existing device from the device list.
     * Note that this action will not actually remove the device from
     * data storage, but setting its status to invalid.
     *
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>POST</code> request with a path of <code>/device/:id</code>.
     *
     * @param id id of the device to be removed
     * @return CompletionStage<Result> contains id from the removed device
     */
    public CompletionStage<Result> removeDevice(Long id) {
        return deviceRepository
                .removeDevice(id)
                .thenApplyAsync(deviceId -> ok(toJson(deviceId)), execCxt.current());
    }

    /**
     * An action that persists the device log into data storage.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>POST</code> request with a path of <code>/update</code>.
     *
     * @param request Http request that contains the device log object
     * @return CompletionStage<Result> contains device log that have been added
     */
    public CompletionStage<Result> updateDevice(final Http.Request request) {
        DeviceLog deviceLog = formFactory.form(DeviceLog.class).bindFromRequest(request).get();
        return deviceRepository
                .updateDevice(deviceLog)
                .thenApplyAsync(deviceLogAdded -> ok(toJson(deviceLogAdded)), execCxt.current());
    }

    /**
     * An action that retrieves status of a selected device.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/check/:id</code>.
     *
     * @param id id of the device that is requesting check for
     * @return CompletionStage<Result> contains status of device that is inquired
     */
    public CompletionStage<Result> checkDevice(Long id) {
        return deviceRepository
                .checkDeviceLog(id)
                .thenApplyAsync(deviceLog -> ok(toJson(deviceLog)), execCxt.current());
    }


}
