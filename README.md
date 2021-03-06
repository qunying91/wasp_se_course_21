# DeviceMonitor

DeviceMonitor is a web-based application for monitoring the status of devices that are deployed in the customer environments. The application can be extended to record status of devices of any kind by simpling adding additional properties to the device model and interface view.

## Features

- Add a new device entry with device and customer information.
- List all devices that are deployed on the customer environments.
- Check status, execution time, and error info of a particular device.
- Remove a device that is no longer valid.

## Techniques
The application is developed using a typical MVC architecture in Java. Techniques used involve:
- Java 8 
- Play framework 2.8.7
- MySQL server 8.0.23

## Run application
To run this application, you need to do following setup:
- Install Java 8 or higher and set the environment variable.
- Install sbt (e.g. 1.4.7 or higher).
- Install MySQL server (8.0.23 or higher). 
- Download the code from this repository and open project using your preferred IDE.
- Configure your MySQL credentials in the application congifuration file (applivation.conf under app/conf).
- Get into the project directory using a command prompt, and execute ```sbt run```.
