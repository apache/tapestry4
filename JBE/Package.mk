# $Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2000 by Howard Ship and Primix Solutions
#
# Primix Solutions
# One Arsenal Marketplace
# Watertown, MA 02472
# http://www.primix.com
# mailto:hship@primix.com
# 
# This library is free software.
# 
# You may redistribute it and/or modify it under the terms of the GNU
# Lesser General Public License as published by the Free Software Foundation.
#
# Version 2.1 of the license should be included with this distribution in
# the file LICENSE, as well as License.html. If the license is not
# included with this distribution, you may find a copy at the FSF web
# site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
# Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk

default: remake-check
	@$(REMAKE_MODULE) default

# File which records the relative path to the Module

MOD_DIR_FILE := $(SYS_BUILD_DIR_NAME)/mod_dir

# Used to re-invoke make in the Module.  This allows local targets
# such as compile to work, even though these require that the Jar
# module makefile invoke the make in the Package.

REMAKE_MODULE = \
	$(MAKE) --unix -C ${shell $(CAT) $(MOD_DIR_FILE)}

catalog: initialize catalog-java catalog-resources catalog-rmi

ifneq "$(JAVA_FILES)" ""

catalog-java: $(wildcard $(JAVA_FILES))
	@for match in $? ; do  \
		$(ECHO) "$(MOD_PACKAGE_DIR)$(SLASH)$$match" >> $(MOD_JAVA_CATALOG) ; \
	done

endif

ifneq "$(RESOURCE_FILES)" ""

catalog-resources: $(wildcard $(RESOURCE_FILES))
	@for match in $? ; do \
		$(ECHO) "$(MOD_PACKAGE_DIR)$(SLASH)$$match" >> $(MOD_RESOURCE_CATALOG) ; \
	done
	
endif

ifneq "$(RMI_CLASSES)" ""

catalog-rmi:
	@$(ECHO) "$(addprefix $(PACKAGE)$(DOT),$(RMI_CLASSES))" >> $(MOD_RMI_CLASS_CATALOG)

endif


# Initialize is responsible for saving MOD_DIR (relative path from
# this package to the root module directory) and PACKAGE
# (the Java package name for this package).  This is later used
# by REMAKE_PACKAGE to re-invoke the module level make for this
# package.

initialize:
	@if [ ! -r $(MOD_DIR_FILE) ] ; \
	then \
		$(MKDIRS) $(SYS_BUILD_DIR_NAME) ; \
		$(ECHO) $(MOD_DIR) > $(MOD_DIR_FILE) ; \
	fi

# MOD_DIR not defined; so make was invoked for just this directory.
# Invoke Jar make for just this package.

remake-check:
	@if [ ! -r $(MOD_DIR_FILE) ] ; \
	then \
	  $(ECHO) "Package has been cleaned. " \
		"You must re-invoke make at the top level." ; \
	  exit 1; \
	fi

compile: remake-check
	@$(REMAKE_MODULE) compile-package

force: remake-check
	#$(REMAKE_MODULE) force
	
install: remake-check
	@$(REMAKE_MODULE) install	

clean:
	@$(RMDIRS) $(SYS_BUILD_DIR_NAME)

.PHONY: default compile clean install force
.PHONY: catalog catalog-java catalog-rmi catalog-resources

