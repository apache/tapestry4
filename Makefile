# Master Makefile for the source tree
# $Id$

distribution: clean copy-external-jars install javadoc
	@$(RECURSE) clean remove-external-jars prepare-for-packaging

# The job of this Makefile is to re-invoke make in various subdirectories.

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk



# The names of the different modules to invoke make in. Order is important, frameworks
# should come first.

MODULES = \
	Tapestry \
	Examples/Tutorial \
	Examples/VlibBeans \
	Examples/Vlib

JAVADOC_MODULES = \
	Tapestry \
	Examples/VlibBeans

# A list of Jar files that can't be redistributed with
# Tapestry, but are needed to build or run Tapestry or the
# examples.  Some of these (j2ee.jar) are not open source,
# and the others ... we don't want to bloat the Tapestry
# distribution by including thier source code.


EXTERNAL_JARS = \
	xerces.jar \
	gnu-regexp.jar \
	j2ee.jar \
	com.mortbay.jetty.jar \
	javax.servlet.jar \
	org.apache.jasper.jar \
	com.sun.net.ssl.jar \
	ServletUtils.jar

# This reflects my personal build work area structure, where
# I create a subdirectory and checkout all the files.

EXTERNAL_LIB_DIR = ../../lib

LOCAL_LIB_DIR = lib

copy-external-jars:
	@$(ECHO) "\n*** Copying external JARs ... ***\n"
	@$(MKDIRS) $(LOCAL_LIB_DIR)
	@$(CP) $(foreach _jar,$(EXTERNAL_JARS),$(EXTERNAL_LIB_DIR)/$(_jar)) $(LOCAL_LIB_DIR)

remove-external-jars:
	@$(ECHO) "\n*** Removing external JARs ...***\n"
	@$(RMDIRS) $(foreach _jar,$(EXTERNAL_JARS),$(LOCAL_LIB_DIR)/$(_jar))

# REINVOKE needs to specify a TARGET (used by the reinvoke
# rule).

REINVOKE := \
	@$(RECURSE) reinvoke
	


prepare-for-packaging:
	@$(ECHO) "\n*** Removing Word documents from distribution ... ***\n"
	@$(FIND) . -name \*.doc -print -exec $(RMDIRS) {} \;

install: 
	$(REINVOKE) TARGET=install JAVAC_OPT=-g

clean: 
	$(REINVOKE) TARGET=clean

reinvoke: 
	@for module in $(MODULES) ; do \
		$(ECHO) "\n*** Making target $(TARGET) in $$module ... ***\n" ; \
       $(RECURSE) -C $$module $(TARGET) || exit 2 ; \
	done

javadoc:
	@$(ECHO) "\n*** Rebuilding Javadoc ... ***\n"
	@$(RMDIRS) javadoc
	@for module in $(JAVADOC_MODULES) ; do \
		$(ECHO) "\n*** Making javadoc in $$module ... ***\n" ; \
       $(RECURSE) -C $$module javadoc || exit 2 ; \
	done

.PHONY: javadoc