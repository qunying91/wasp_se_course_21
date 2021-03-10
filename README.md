# DeviceMonitor

----------------

### 1. Overview

DeviceMonitor platform (this project) is a ```web-based application``` for monitoring the status of devices that are deployed in the customer environments, especially on how long the devices are in operation, and error info if the device is not working as intended. This platform aims to enable a supplier to monitor and manage any type of IoT devices that are deployed on the customer side and can be communicated through a regular internet connection. A figure (index.png) of the platform can be found in the ```root``` directory of this repository. 

#### 1.1 Features
The platform provides mainly four features to the device suppliers, including:
- ```Adding``` a new device entry with device and customer information.
- ```Listing``` all devices that are deployed on the customer environments.
- ```Checking``` status, execution time, and error info of a particular device.
- ```Removing``` a device that is no longer valid.

#### 1.2 Work Flow
A typical work flow of the DeviceMonitor platform follows the below procedures in the listing order:
- Device supplier ```adds``` a new device that is installed by the customer,  on the platform.
- The installed device ```sends``` a device log to the platform, including device status, execution time, and error info, periodically.
- Device supplier ```checks``` regularly on the plfatform about the device info, and contact the customer if necessay, when an error is reported on a device, or remove a device that is no longer valid.

### 2. Development
The application is developed using a typical MVC architecture in ```Java```, based on a lightweighted framework ```Play```. Current development has fulfilled the implementation of all backend services and a basic interface view in displaying the existing and valid devices. ```Please note that other interfaces for, e.g. adding/removing/checking details of a device are not implemented yet due to the limitation of time and capacity in front-end development.```

#### 2.1 Technique Stack 
(Versioned) Techniques and tools used for development of the platform:
- ```Java``` 1.8.0_212 
- ```Play``` Framework 2.8.7
- ```MySQL``` Server 8.0.23
- ```Sbt``` 1.4.7
- ```NodeJs``` 10.24.0
- ```Javascript```
- ```Coffeescript```
- ```HTML```
- ```CSS```
 
#### 2.2 Project Structure
The project is mainly structured with the follow directories:
- ```app``` - directory that contains the source files, e.g. *.java, *.html etc.
- ```conf``` - directory for setting application configuration, e.g. logging, database connection, routing.
- ```public``` - directory for static assets used in the application, e.g. *.css, *.js, etc.
- ```project``` - directory for locating the built target, and plugins to be included.
- ```test``` - directory for test files including, e.g. unit test, integration test, and acceptance test.
- ```script``` - directory for putting script files, e.g. test automation script.
- ```build.sbt``` - file for setting options and dependencies to build project.

### 3. Testing

Four types of testing were implemented or conducted for this project, as shown in the following list. Manual testing was performed locally and manually, to ensure application starts and works as intended. In addition, UT(Unit test), IT(inegration test), and AT(acceptance test) were created and can be found under the ```/test``` directory, which, in total, are ```15``` test cases on all levels and all of them passed. 

- Unit test - a class of unit test cases were implemented, using the mockups, for the main controller (DeviceController), and can be found as ```/test/controllers/DeviceControllerUT.java```. The class has 7 test cases corresponding to the 7 action methods that exist in the DeviceController class.
- Integration test - a class of integration test cases were implemented, within a test server, and can be found as ```/test/controllers/DeviceControllerIT.java```. The class has 7 test cases corresponding to the 7 features that existing for the platform.
- Acceptance test - a class of acceptance test cases for the platform and can be found as ```/test/controllers/DeviceControllerAT.java```, currently only 1 test case implemented, which is requesting and asserting the content of the default page.


```Please note that you need to``` install MySQL Server 8 and run the script ```/conf/create-table.sql```, and also run ```/conf/sample-data.sql``` if you want some default data. Before running the script, you need to change the database name from ```devicemonitor``` to ```test_devicemonitor``` in ```/conf/create-table.sql```. After setting up the database, you need to configure the credentials in the ```conf/application.test.conf``` to connect to the database just created.

```Please also note that you need to``` install ```Java ```, ```Sbt``` and ```nodeJs``` with above-mentioned version (from section 2.1) preferred, other versions of the software may result in errors and fail to start the test. You can either run the test with 1) IDE such as eclipse, or IntelliJ etc., 2). Run  ```sbt test -Dconfig.file=conf/application.test.conf``` in command shell, 3). Run the bash file under ```\scripts\test-sbt``` to automate the testing.

### 4. Run and Installation

To run this application, you need to do following setup as prerequisites of the environment. Please make sure you have all setups down, ```with correct version```,  in place.
- ```Install Java 8``` (Java 1.8.0_212 is recommended) and set the environment variable. Higher version may encounter problems in running the node program, and thus causing compilation failure.
- ```Install Sbt``` (e.g. 1.4.7 or higher), and sets the environment variable.
- ```Install nodeJs``` (10.24.0 is recommended).
- ```Install MySQL Server``` (8.0.23 is recommended). If you have other version, you need to configure the dialect tag in ```/conf/MEATA-INF/persistence.xml``` 
- ```Download the code``` from this repository and open project through command shell or, using your preferred IDE.
- ```Setup your database```, as specified in section 3 and ```configure MySQL credentials``` in the application congifuration file (```/conf/application.conf```).
- Run the application through your IDE, or by executing ```sbt run``` in the command shell.
- ```Type localhost:9000/``` using any web browser you prefer.

