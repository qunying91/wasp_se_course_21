package jpa;

import jpa.models.Customer;
import jpa.models.Device;
import jpa.models.DeviceLog;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPADeviceRepository implements DeviceRepository{

    private final JPAApi jpaApi;
    private final DbExecuteContext execContext;

    private final String STAT_INVALID = "INVALID";

    @Inject
    public JPADeviceRepository (JPAApi jpaApi, DbExecuteContext execContext) {
        this.jpaApi = jpaApi;
        this.execContext = execContext;
    }

    /**
     * Persist a new customer into db
     *
     * @param customer to be persisted
     * @return the customer persisted
     */
    @Override
    public CompletionStage<Customer> addCustomer(Customer customer) {
        return supplyAsync(() -> wrap(em -> insertCustomer(em, customer)), execContext);
    }

    /**
     * Select existing customers from db
     *
     * @return list of existing customers
     */
    @Override
    public CompletionStage<Stream<Customer>> listCustomers() {
        return supplyAsync(() -> wrap(em -> listCustomers(em)), execContext);
    }

    /**
     * Select existing and valid devices from db
     *
     * @return list of existing and valid devices
     */
    @Override
    public CompletionStage<Stream<Device>> listDevices() {
        return supplyAsync(() -> wrap(em -> listDevices(em)), execContext);
    }

    /**
     * Persist a new device into db
     *
     * @param device to be persisted
     * @return the persisted device
     */
    @Override
    public CompletionStage<Device> addDevice(Device device) {
        return supplyAsync(() -> wrap(em -> insertDevice(em, device)), execContext);
    }

    /**
     * Update device status to 'INVALID' in db
     *
     * @param id device id to be updated
     * @return device id
     */
    @Override
    public CompletionStage<Long> removeDevice(Long id) {
        return supplyAsync(() -> wrap(em -> removeDevice(em, id)), execContext);
    }

    /**
     * Persist a new device log and update device status into db
     *
     * @param log to be persisted
     * @return the persisted device log
     */
    @Override
    public CompletionStage<DeviceLog> updateDevice(DeviceLog log) {
        return supplyAsync(() -> wrap(em -> update(em, log)), execContext);
    }

    /**
     * Select the latest updated log of the device from db
     *
     * @param deviceId device id
     * @return the latest device log
     */
    @Override
    public CompletionStage<DeviceLog> checkDeviceLog(Long deviceId) {
        return supplyAsync(() -> wrap(em -> check(em, deviceId)), execContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Stream<Customer> listCustomers(EntityManager em) {
        List<Customer> customers = em.createQuery("select c from Customer c", Customer.class).getResultList();
        return customers.stream();
    }

    private Customer insertCustomer(EntityManager em, Customer customer) {
        em.persist(customer);
        return customer;
    }

    private Stream<Device> listDevices(EntityManager em) {
        List<Device> devices = em.createQuery("select d from Device d where d.status != 'INVALID'", Device.class).getResultList();
        return devices.stream();
    }

    private Device insertDevice(EntityManager em, Device device) {
        em.persist(device);
        return device;
    }

    private Long removeDevice(EntityManager em, Long id) {
        Query query  = em.createQuery("update Device d set d.status = :status, d.updateAt = :updateAt where d.id = :id");
        query.setParameter("status", STAT_INVALID);
        query.setParameter("updateAt", System.currentTimeMillis());
        query.setParameter("id", id);
        query.executeUpdate();

        return id;
    }

    private DeviceLog update(EntityManager em, DeviceLog log) {
        Query query  = em.createQuery("update Device d set d.status = :status, d.updateAt = :updateAt where d.id = :id");
        query.setParameter("status", log.status);
        query.setParameter("updateAt", System.currentTimeMillis());
        query.setParameter("id", log.deviceId);
        query.executeUpdate();

        em.persist(log);
        return log;
    }

    private DeviceLog check(EntityManager em, Long id) {
        TypedQuery<DeviceLog> query = em.createQuery("select l from DeviceLog l where l.deviceId = :id order by id desc", DeviceLog.class);
        List<DeviceLog> logs = query.setParameter("id", id).getResultList();

        return logs.size() == 0 ? null : logs.get(0);
    }
}
