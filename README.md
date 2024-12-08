# EOL Notice

NOTICE: SUPPORT FOR THIS PROJECT ENDED.

This project has reached its end of life.

We will no longer be monitoring the issues for this project or reviewing pull requests.
You are free to continue using this project under the license terms or fork this project at your own risk.

----

# README

Tapestry is a component based framework for creating sophisticated web applications. It's used with existing application servers, acting as a kind of "super servlet". Tapestry applications are built by configuring and combining components to form pages. You get surprisingly large amounts of robust functionality for startingly small amounts of effort and code!

The JARs were created on @OS_NAME@ @OS_VERSION@ using the @JAVA_VENDOR@ JDK @JAVA_VM_VERSION@ compiler.

This product includes software developed by the Apache Software Foundation ([http://www.apache.org/](http://www.apache.org/)).  

## Non-ASL Libraries

Tapestry depends on a handful of non-ASL libraries. Because these libraries are not distributed under the terms of the Apache Software License, they may not be redistributed with as part of Tapestry. Tapestry's build scripts will automatically download the libraries as needed. Several libraries are only needed at build time, but some are needed at runtime (and are typically copied into `WEB-INF/lib`). The two runtime libraries are:

* [OGNL](http://www.ognl.org/) (Object Graph Navigation Library), version @OGNL_VERSION@
* [Javassist](http://jboss.org/developers/projects/javassist.html) (byte code enhancement library), version @JAVASSIST_VERSION@

The necessary libraries will automatically be downloaded when building the examples. The Ant target `download-ext-framework` will also download the libraries. If you are behind a firewall, you may need to [provide some additional properties](ext-dist/README.html) in `config/build.properties`.  


## Configuration

In order to run the Tapestry demos, you need only the **binary** distribution (it includes the source code for the examples). The Ant build scripts will compile and build the Workbench and the Virtual Library example applications on your workstation, then deploy them into Jetty, Tomcat or JBoss.

* JDK 1.3 or newer JVM is installed, and the `JAVA_HOME` environment variable is set.
* [Ant](http://jakarta.apache.org/ant) 1.5 or newer is installed, and is added to the system `PATH`.
* You have both the **binary** and the **source** Tapestry distributions installed

Basic setup: The demos require several external dependencies that cannot be distributed with Tapestry because of licensing conflicts. You will need one of the following:

* [Jetty](http://sf.net/projects/jetty) servlet container, release 4.2 or above.
* [Tomcat](http://jakarta.apache.org/tomcat/index.html) servlet container, release 4.1 or above.
* [JBoss](http://www.jboss.org) application server, release 3.0.6\. Running the Virtual Library application requires JBoss.

In each case you will create the file `config/build.properties` and put an _absolute path_ to the installation directory (of Jetty, JBoss or Tomcat). **Always use only forward slashes in the path name.**

A sample file, `config/build.properties.template`, is provided. You may edit and rename this file.

The build process downloads a number of extra files into the `ext-dist` and `lib/ext` directories. See the [README.html](ext-dist/README.html) in that directory for more details.

### Configuring JBoss

To run the Tapestry Workbench and the Virtual Library applications with the [JBoss](http://sourceforge.net/projects/jboss) server:

* Download and install [JBoss 3.0.6](http://prdownloads.sourceforge.net/jboss/jboss-3.0.6.zip?download).  
  JBoss is an open-source application server, used to run the database and Enterprise JavaBeans in the Virtual Library.

This auto-configuration requires JBoss 3.0.6 exactly, not a later release. This only affects these turn-key demos, not deployment of your own Tapestry applications.

* Update `config/build.properties` and set property **`jboss.dir`** to the absolute path name of the JBoss installation directory.
* Download the external dependencies.
* Execute the command:  
  **`ant -emacs configure run-jboss`**  
  This will build, copy and configure the necessary files in the JBoss installation directory, then run the JBoss server, automatically deploying the example applications. (The `-emacs` option changes the Ant output format to be less verbose).

> You can now run the [Tapestry Workbench](http://localhost/workbench) or the [Virtual Library](http://localhost) applications.

### Configuring Jetty

To run the Tapestry Workbench application with Jetty servlet container:

* Obtain and install a copy of Jetty.
* Update `config/build.properties` and set property **`jetty.dir`** to the absolute path name of the Jetty installation directory.
* Execute the command:  
  **`ant -emacs run-workbench`**  
  This will run the embedded Jetty server.  

> You can now run the [Tapestry Workbench](http://localhost:8080/workbench) application.

### Configuring Tomcat

To deploy the Tapestry Workbench application onto an existing Tomcat installation:

To run the Tapestry Workbench application with Tomcat servlet container:

* Obtain and install a copy of Tomcat.
* Update `config/build.properties` and set property **`tomcat.dir`** to the absolute path name of the Tomcat installation directory, for example `C:/Program Files/Apache Group/Tomcat 4.1`.
* Execute the command:  
  **`ant -emacs deploy-tomcat`**  
  This will deploy the Workbench application into Tomcat. If Tomcat is running, you will be able to access the application immediately.  

> You can then run the [Tapestry Workbench](http://localhost:8080/workbench) application.


## Building from Source

The source distribution includes the source code for the framework and the contrib library. You should extract the source distribution to the same directory as the binary distribution. You may then execute **`ant install`** to compile all the frameworks and examples. Building documentation and running the unit tests requires some additional setup that is discussed in the [Tapestry Contributor's Guide](web/doc/ContributorsGuide/ContributorsGuide.html).


## Documentation

A complete set of [documentation](web/doc.html) is distributed with Tapestry.  

Tapestry is an open source project, hosted by [The Jakarta Project](http://jakarta.apache.org) and distributed under the terms of the [Apache Software License 2.0](LICENSE-2.0.txt).

Â© 2003-2004 Apache Software Foundation.
