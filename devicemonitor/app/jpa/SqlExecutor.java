package jpa;

import jpa.models.Customer;
import jpa.models.Device;
import jpa.models.DeviceLog;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Stream;

public class SqlExecutor {

    private final String STAT_INVALID = "INVALID";

    Stream<Customer> listCustomers(EntityManager em) {
        List<Customer> customers = em.createQuery("select c from Customer c", Customer.class).getResultList();
        return customers.stream();
    }

    Customer insertCustomer(EntityManager em, Customer customer) {
        em.persist(customer);
        return customer;
    }

    Stream<Device> listDevices(EntityManager em) {
        List<Device> devices = em.createQuery("select d from Device d where d.status != 'INVALID'", Device.class).getResultList();
        return devices.stream();
    }

    Device insertDevice(EntityManager em, Device device) {
        em.persist(device);
        return device;
    }

    Long removeDevice(EntityManager em, Long id) {
        Query query  = em.createQuery("update Device d set d.status = :status, d.updateAt = :updateAt where d.id = :id");
        query.setParameter("status", STAT_INVALID);
        query.setParameter("updateAt", System.currentTimeMillis());
        query.setParameter("id", id);

        int updatedEntry = query.executeUpdate();
        // assert the sql statement is executed, and the entry is updated
        assert(updatedEntry == 1);

        return id;
    }

    DeviceLog checkDeviceLog(EntityManager em, Long id) {
        TypedQuery<DeviceLog> query = em.createQuery("select l from DeviceLog l where l.deviceId = :id order by id desc", DeviceLog.class);
        List<DeviceLog> logs = query.setParameter("id", id).getResultList();

        return logs.size() == 0 ? null : logs.get(0);
    }

    DeviceLog updateDeviceLog(EntityManager em, DeviceLog log) {
        updateDevice(em, log);

        em.persist(log);
        return log;
    }

    private void updateDevice(EntityManager em, DeviceLog log) {
        Query query  = em.createQuery("update Device d set d.status = :status, d.updateAt = :updateAt where d.id = :id");
        query.setParameter("status", log.status);
        query.setParameter("updateAt", System.currentTimeMillis());
        query.setParameter("id", log.deviceId);

        int updatedEntry = query.executeUpdate();
        // assert the sql statement is executed, and the entry is updated
        assert(updatedEntry == 1);
    }

}
