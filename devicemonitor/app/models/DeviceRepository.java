package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPADeviceRepository.class)
public interface DeviceRepository {

    CompletionStage<Customer> addCustomer(Customer customer);

    CompletionStage<Stream<Customer>> listCustomers();

    CompletionStage<Stream<Device>> listDevices();

    CompletionStage<Device> addDevice(Device device);

    CompletionStage<Long> removeDevice(Long id);

    CompletionStage<DeviceLog> updateDevice(DeviceLog log);

    CompletionStage<DeviceLog> checkDevice(Long deviceId);
}
