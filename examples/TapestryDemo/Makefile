# $Id$

MODULE_NAME = TapestryDemo

PACKAGES = tests.tapestry

CONTEXT_RESOURCES = assets

WEB_INF_RESOURCES = weblogic.xml

LOCAL_CLASSPATH = \
	$(INSTALL_DIR)/SessionTracker.jar \
	$(INSTALL_DIR)/log.jar \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/PrimixFoundation.jar \
	$(INSTALL_DIR)/j2ee.jar

INSTALL_DIR = ../../lib

include $(SYS_MAKEFILE_DIR)/War.mk

# Classpath entries needed when WebLogic starts up.  Since we define some
# Cloudscape database pools, we need to have Cloudscape
# ready to go.

WEBLOGIC_BOOT_CLASSPATH = \
	$(WEBLOGIC_DIR)/lib/weblogic510sp4boot.jar \
	$(CLOUDSCAPE_INSTALL)/lib/cloudscape.jar \
	$(CLOUDSCAPE_INSTALL)/lib/license.jar

# Classpath needed for EJB deployment modules and Wars that are loaded.
# This includes WebLogic licensing as well (seems like that should go in
# the boot classpath, but what do I know?).

WEBLOGIC_CLASSPATH = \
	$(WEBLOGIC_DIR)/lib/weblogic510sp4.jar \
	$(WEBLOGIC_DIR)/license \
	$(WEBLOGIC_DIR)/classes \
	$(WEBLOGIC_DIR)/lib/weblogicaux.jar \
	$(INSTALL_DIR)/gnu-regexp.jar \
	$(INSTALL_DIR)/xerces.jar \
	$(INSTALL_DIR)/log.jar \
	$(INSTALL_DIR)/Tapestry.jar \
	$(INSTALL_DIR)/PrimixFoundation.jar
	
# Convert from space-seperated to proper form, and convert relative paths to absolute paths.

_WEBLOGIC_BOOT_CLASSPATH := $(strip $(shell $(JBE_CANONICALIZE) -classpath $(WEBLOGIC_BOOT_CLASSPATH)))
_WEBLOGIC_CLASSPATH := $(strip $(shell $(JBE_CANONICALIZE) -classpath $(WEBLOGIC_CLASSPATH)))

WEBLOGIC_OPTS = \
	-ms128M -mx128M \
    -Dweblogic.class.path="$(_WEBLOGIC_CLASSPATH)" \
    -Dweblogic.system.home=$(WEBLOGIC_DIR)  \
	-Dcloudscape.system.home="$(CLOUDSCAPE_INSTALL)/demo/databases" \
	-Dcom.primix.tapestry.enable-reset-service=true \
	-classpath "$(_WEBLOGIC_BOOT_CLASSPATH)"
	
configure-weblogic: initialize
	@ $(ECHO) "\n*** Setting WebLogic Server options ***\n"
	$(WEBLOGIC_DIR)/bin/wlconfig.exe $(WEBLOGIC_OPTS)