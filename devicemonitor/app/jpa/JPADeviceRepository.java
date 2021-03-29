package jpa;

import jpa.models.Customer;
import jpa.models.Device;
import jpa.models.DeviceLog;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPADeviceRepository implements DeviceRepository{

    private final JPAApi jpaApi;
    private final DbExecuteContext execContext;
    private final SqlExecutor sqlExecutor;

    @Inject
    public JPADeviceRepository (JPAApi jpaApi, DbExecuteContext execContext) {
        this.jpaApi = jpaApi;
        this.execContext = execContext;
        this.sqlExecutor = new SqlExecutor();
    }

    /**
     * Persist a new customer into db
     *
     * @param customer to be persisted
     * @return the customer persisted
     */
    @Override
    public CompletionStage<Customer> addCustomer(Customer customer) {
        return supplyAsync(() -> wrap(em -> sqlExecutor.insertCustomer(em, customer)), execContext);
    }

    /**
     * Select existing customers from db
     *
     * @return list of existing customers
     */
    @Override
    public CompletionStage<Stream<Customer>> listCustomers() {
        return supplyAsync(() -> wrap(em -> sqlExecutor.listCustomers(em)), execContext);
    }

    /**
     * Select existing and valid devices from db
     *
     * @return list of existing and valid devices
     */
    @Override
    public CompletionStage<Stream<Device>> listDevices() {
        return supplyAsync(() -> wrap(em -> sqlExecutor.listDevices(em)), execContext);
    }

    /**
     * Persist a new device into db
     *
     * @param device to be persisted
     * @return the persisted device
     */
    @Override
    public CompletionStage<Device> addDevice(Device device) {
        return supplyAsync(() -> wrap(em -> sqlExecutor.insertDevice(em, device)), execContext);
    }

    /**
     * Update device status to 'INVALID' in db
     *
     * @param id device id to be updated
     * @return device id
     */
    @Override
    public CompletionStage<Long> removeDevice(Long id) {
        return supplyAsync(() -> wrap(em -> sqlExecutor.removeDevice(em, id)), execContext);
    }

    /**
     * Persist a new device log and update device status into db
     *
     * @param log to be persisted
     * @return the persisted device log
     */
    @Override
    public CompletionStage<DeviceLog> updateDeviceLog(DeviceLog log) {
        return supplyAsync(() -> wrap(em -> sqlExecutor.updateDeviceLog(em, log)), execContext);
    }

    /**
     * Select the latest updated log of the device from db
     *
     * @param deviceId device id
     * @return the latest device log
     */
    @Override
    public CompletionStage<DeviceLog> checkDeviceLog(Long deviceId) {
        return supplyAsync(() -> wrap(em -> sqlExecutor.checkDeviceLog(em, deviceId)), execContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
}
