SDWN Loxigen
============

This is a fork of the loxigen tool by the (Floodlight Project)[https://github.com/floodlight/loxigen]. This version includes extensions for optical networks introduced by (ONOS)[http://onosproject.org/] as well as SDWN extensions. It generates libraries for Java (OpenFlowJ version 3.1.0.sdwn) and C to be used with the SDWN ONOS controller.

**WARNING: Do not use the C library for optical networks. This fork was made for the SDWN extensions. It only includes the optical parts for C to be compatible with the Java lib for ONOS. The generated C library will not work correctly.**

Installation
============

## Java
1. Clone the repository
2. Build the Java library: ```make java```
3. Install the Java library to your local maven repository ```cd loxi_output/openflowj && mvn clean install```
4. Include the Java library in your maven project as a dependency
```
<dependencies>
    ...
    <dependency>
        <groupId>de.tuberlin.inet.sdwn</groupId>
        <artifactId>openflowj</artifactId>
        <version>3.1.0.sdwn-SNAPSHOT</version>
    </dependency>
    ...
</dependencies>
```
## C
1. Clone the repository
2. Build the C library: ```make C```
3. Copy the generated sources to your project from ```loxi_output/loci```
