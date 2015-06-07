e2c-server
==========

REST API for Export to China 2.0+

## Development Environment Setup

### Java

You will need to install a [Java Standard Edition Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
Make sure you install the JDK and not just the runtime environment (JRE).

### IDE

You can use any Java IDE that comes with Spring, Maven and Git integration, 
including [IntelliJ IDEA](http://www.jetbrains.com/idea/) and [Spring Tool Suite](http://www.springsource.org/sts).
If you are using Spring Tool Suite, consider using the following plugins:

* For editing locale-specific resource bundles (for validation error messages), install the [Resource Bundle Editor plugin](http://eclipse-rbe.sourceforge.net/)
* To analyze packaging dependencies and identify cycles, install the [JDepend4Eclipse plugin](http://andrei.gmxhome.de/jdepend4eclipse/index.html) 

### Mongo DB
You will need to install MongoDB for support of our Document Store implementation at 
[MongoDB Installation for Windows](http://docs.mongodb.org/manual/tutorial/install-mongodb-on-windows/).  Please follow the install instructions
and make sure you configure to run as a Windows service to avoid starting manually.

### Tomcat
If you do not want to rely on your IDE's bundled version of [Apache Tomcat](http://tomcat.apache.org/), you will need to install your own version.
Add the following to server.xml under the Connector Tag: 

* compression="on" compressionMinSize="2048" noCompressionUserAgents="gozilla, traviata" compressableMimeType="application/json"

## Running Locally

### Spring Tool Suite

Simply right-click on the e2c-server project root in Package Explorer and select  Run As>Run on Server from the context menu. 
Choose the "VMWare vFabric tc Server Developer Edition" server from the list and click the Finish button. 

The server will start up (if it is not already running), then the web application will be built, deployed to it and started up. The root of
the web app will then be displayed in the default browser (which will be a 404 in this case, but you can append URIs to
the base URL to test GET methods).

Once the server configuration is created you need to add the following property 
'spring.profiles.active=local'
to the "conf/catalina.properties" file for the Tomcat server - this will set your profile in Spring to be local. 
You will then need to restart the server.
