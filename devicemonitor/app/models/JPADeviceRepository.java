package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public CompletionStage<Error> error(Error error) {
        return supplyAsync(() -> wrap(em -> error(em, error)), execContext);
    }

    @Override
    public CompletionStage<Stream<Error>> listErrors(Long deviceId) {
        return supplyAsync(() -> wrap(em -> listErrors(em, deviceId)), execContext);
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
        List<Device> devices = em.createQuery("select d from Device d", Device.class).getResultList();
        return devices.stream();
    }

    private Device insertDevice(EntityManager em, Device device) {
        em.persist(device);
        return device;
    }

    private Long removeDevice(EntityManager em, Long id) {
        Query query  = em.createQuery("select d from Device d where d.id = :id");
        List<Device> devices = query.setParameter("id", id).getResultList();

        if(devices.size() == 1) {
            em.remove(devices.get(0));
        }

        return id;
    }

    private Error error(EntityManager em, Error error) {
        em.persist(error);
        return error;
    }

    private Stream<Error> listErrors(EntityManager em, Long id) {
        Query query = em.createQuery("select e from Error e where e.deviceId = :id");
        List<Error> errors = query.setParameter("id", id).getResultList();
        return errors.stream();
    }
}
