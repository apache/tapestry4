This package contains pre-compiled demo applications for Tapestry.  Tapestry is a comprehensive web
application framework for Java and is released under the Apache Software License 2.0.  More details
about Tapestry are available at the Tapestry home page: http://jakarta.apache.org/tapestry

Because of licensing considerations, these demos are not downloadable from apache.org or any 
Apache mirror site.

The demos require a FRESH INSTALL of the JBoss application server, version 4.0.2.  
JBoss is available from http://jboss.org.

The installation will overwrite some configuration files inside JBoss.

The provided Ant build script, build.xml, will copy the necessary files into place.  You will need a
copy of the Ant build tool, available from http://ant.apache.org.

Execute the command "ant".  You will be asked for the directory into which you installed JBoss.
On windows systems, this will commonly be C:/jboss-4.0.2.

After a confirmation message, JBoss will be shut down (if running).  If JBoss is not running you will see
an exception message.

After JBoss is shut down, files are copied to the default JBoss server profile.  You may then restart JBoss
to access the applications.

Tapestry Workbench 
	http://localhost:8080/workbench
	
	The workbench is a testbed for many common Tapestry components.  The tabs across the top
	demonstrate different Tapestry features and components, including localization,
	input validation, exception reporting, and the Palette and Table components.
	

The Virtual Library
	http://localhost:8080/vlib
	
	The Virtual Library is a small but complete J2EE application. It uses J2EE Entity beans
	with container managed persistence, and a stateless session facade, with Tapestry on the
	front end.  The application is used to manage a shared pool of books, tracking who owns
	and who is currently borrowing each book.
	
	Three users are built into the library.  All three use the same password, 'secret':
	
		dilbert@bigco.com
		squeue@bug.org
		ringbearer@bagend.shire
		
		The "ringbearer" user has administrative access.
	
