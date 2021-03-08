package models;

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

    @Inject
    public JPADeviceRepository (JPAApi jpaApi, DbExecuteContext execContext) {
        this.jpaApi = jpaApi;
        this.execContext = execContext;
    }

    @Override
    public CompletionStage<Customer> addCustomer(Customer customer) {
        return supplyAsync(() -> wrap(em -> insertCustomer(em, customer)), execContext);
    }

    @Override
    public CompletionStage<Stream<Customer>> listCustomers() {
        return supplyAsync(() -> wrap(em -> listCustomers(em)), execContext);
    }

    @Override
    public CompletionStage<Stream<Device>> listDevices() {
        return supplyAsync(() -> wrap(em -> listDevices(em)), execContext);
    }

    @Override
    public CompletionStage<Device> addDevice(Device device) {
        return supplyAsync(() -> wrap(em -> insertDevice(em, device)), execContext);
    }

    @Override
    public CompletionStage<Long> removeDevice(Long id) {
        return supplyAsync(() -> wrap(em -> removeDevice(em, id)), execContext);
    }

    @Override
    public CompletionStage<DeviceLog> updateDevice(DeviceLog log) {
        return supplyAsync(() -> wrap(em -> update(em, log)), execContext);
    }

    @Override
    public CompletionStage<DeviceLog> checkDevice(Long deviceId) {
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
        TypedQuery<Customer> query = em.createQuery("select c from Customer c where c.name = :name", Customer.class);
        List<Customer> customers = query.setParameter("name", customer.name).getResultList();

        if(customers.size() > 0) {
            return null;
        }

        em.persist(customer);
        return customer;
    }

    private Stream<Device> listDevices(EntityManager em) {
        List<Device> devices = em.createQuery("select d from Device d where d.status !=0", Device.class).getResultList();
        return devices.stream();
    }

    private Device insertDevice(EntityManager em, Device device) {
        em.persist(device);
        return device;
    }

    private Long removeDevice(EntityManager em, Long id) {
        Query query  = em.createQuery("update Device d set d.status = :status, updateAt = :updateAt where d.id = :id");
        query.setParameter("status", 0);
        query.setParameter("updateAt", System.currentTimeMillis());
        query.setParameter("id", id);
        query.executeUpdate();

        return id;
    }

    private DeviceLog update(EntityManager em, DeviceLog log) {
        Query query  = em.createQuery("update Device d set d.status = :status, updateAt = :updateAt where d.id = :id");
        query.setParameter("status", log.status);
        query.setParameter("updateAt", System.currentTimeMillis());
        query.setParameter("id", log.deviceId);
        query.executeUpdate();

        em.persist(log);
        return log;
    }

    private DeviceLog check(EntityManager em, Long id) {
        TypedQuery<DeviceLog> query = em.createQuery("select l from DeviceLog l where l.deviceId = :id", DeviceLog.class);
        List<DeviceLog> logs = query.setParameter("id", id).getResultList();

        if(logs.size() == 0) {
            return null;
        }
        return logs.get(0);
    }
}
