# $Id$

MODULE_NAME = Vlib

PACKAGES = \
	com.primix.vlib \
	com.primix.vlib.pages \
	com.primix.vlib.components \
	com.primix.vlib.jsp

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/j2ee.jar \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/PrimixFoundation.jar \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/VlibBeans.jar \
	$(INSTALL_DIR)/ServletUtils.jar

INSTALL_DIR = ../../lib

JAVADOC_DIR = ../../javadoc

CONTEXT_RESOURCES = jsp

WEB_INF_RESOURCES = weblogic.xml

include $(SYS_MAKEFILE_DIR)/War.mk

# Classpath entries needed when WebLogic starts up.  This includes the Cloudscape
# libraries needed to setup the Cloudscape vlib connection pool.

WEBLOGIC_BOOT_CLASSPATH := \
	$(WEBLOGIC_DIR)/lib/weblogic510sp4boot.jar \
	$(CLOUDSCAPE_INSTALL)/lib/cloudscape.jar \
	$(CLOUDSCAPE_INSTALL)/lib/license.jar

# Classpath needed for EJB deployment modules and Wars that are loaded.
# This includes WebLogic licensing as well.

WEBLOGIC_CLASSPATH := \
	$(WEBLOGIC_DIR)/lib/weblogic510sp4.jar \
	$(WEBLOGIC_DIR)/license \
	$(WEBLOGIC_DIR)/classes \
	$(WEBLOGIC_DIR)/lib/weblogicaux.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/PrimixFoundation.jar \
	$(INSTALL_DIR)/ServletUtils.jar
	
# Convert from space-seperated to proper form, and convert relative paths to absolute paths.

_WEBLOGIC_BOOT_CLASSPATH = $(strip $(shell $(JBE_CANONICALIZE) -classpath $(WEBLOGIC_BOOT_CLASSPATH)))
_WEBLOGIC_CLASSPATH = $(strip $(shell $(JBE_CANONICALIZE) -classpath $(WEBLOGIC_CLASSPATH)))

WEBLOGIC_OPTS = \
	-mx128M -mx128M \
    -Dweblogic.class.path="$(_WEBLOGIC_CLASSPATH)" \
    -Dweblogic.system.home=$(WEBLOGIC_DIR)  \
	-Dcloudscape.system.home="$(CLOUDSCAPE_INSTALL)/demo/databases" \
	-classpath "$(_WEBLOGIC_BOOT_CLASSPATH)"

configure-weblogic: initialize
	@ $(ECHO) "\n*** Configuring WebLogic Server options ***\n"
	$(WEBLOGIC_DIR)/bin/wlconfig.exe $(WEBLOGIC_OPTS)


