package jpa;

import com.google.inject.ImplementedBy;
import jpa.models.Customer;
import jpa.models.Device;
import jpa.models.DeviceLog;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPADeviceRepository.class)
public interface DeviceRepository {

    CompletionStage<Customer> addCustomer(Customer customer);

    CompletionStage<Stream<Customer>> listCustomers();

    CompletionStage<Stream<Device>> listDevices();

    CompletionStage<Device> addDevice(Device device);

    CompletionStage<Long> removeDevice(Long id);

    CompletionStage<DeviceLog> updateDeviceLog(DeviceLog log);

    CompletionStage<DeviceLog> checkDeviceLog(Long deviceId);
}
