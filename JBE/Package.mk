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

catalog: catalog-java catalog-resources catalog-rmi

ifneq "$(JAVA_FILES)" ""

catalog-java: $(wildcard $(JAVA_FILES))
	@for match in $? ; do  \
		$(ECHO) "$(MOD_SOURCE_DIR_PREFIX)$(MOD_PACKAGE_DIR)$(SLASH)$$match" \
			>> $(MOD_JAVA_CATALOG) ; \
	done

endif

ifneq "$(RESOURCE_FILES)" ""

catalog-resources: $(wildcard $(RESOURCE_FILES))
	@for match in $? ; do \
		$(ECHO) "$(MOD_SOURCE_DIR_PREFIX)$(MOD_PACKAGE_DIR)$(SLASH)$$match" \
			>> $(MOD_RESOURCE_CATALOG) ; \
	done
	
endif

# RMI_CLASSES is a list of class names (not file names), so it can't
# be wildcarded.

ifneq "$(RMI_CLASSES)" ""

catalog-rmi:
	@$(ECHO) "$(addprefix $(PACKAGE)$(DOT),$(RMI_CLASSES))" \
		>> $(MOD_RMI_CLASS_CATALOG)

endif

.PHONY: catalog catalog-java catalog-rmi catalog-resources

