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

MOD_BUILD_DIR = $(SYS_BUILD_DIR_NAME)

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk

# Stamp file used to control copying of top-level resources (such
# as EJB deployment descriptors).

MOD_META_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/meta-resources-stamp

# The MODULE_NAME is specified in the Makefile and becomes the name of
# the ultimate file.

JAR_FILE := $(MODULE_NAME).$(JAR_EXT)

# Javadoc is produced into a directory named after the module.

FINAL_JAVADOC_DIR = $(JAVADOC_DIR)/$(MODULE_NAME)

MOD_JAVA_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/java-stamp

RESOURCE_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/resource-stamp

RMI_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/rmi-stamp

# A file "touched" whenever anything changes inside $(MOD_CLASS_DIR),
# signifying that the Jar should be rebuilt.

MOD_DIRTY_JAR_STAMP_FILE = $(SYS_BUILD_DIR_NAME)/dirty-jar-stamp

# Build the compile-time classpath

FINAL_CLASSPATH = $(strip . $(MOD_CLASS_DIR) $(MOD_CLASSPATH) $(SITE_CLASSPATH) \
	$(LOCAL_CLASSPATH) $(LOCAL_RELATIVE_CLASSPATH))
	
FINAL_CLASSPATH_OPTION = \
	-classpath "$(subst $(SPACE),$(CLASSPATHSEP),$(FINAL_CLASSPATH))"
	
FINAL_JAVAC_OPT = $(strip -d $(MOD_CLASS_DIR) $(FINAL_CLASSPATH_OPTION) $(MOD_JAVAC_OPT) \
	$(SITE_JAVAC_OPT) $(LOCAL_JAVAC_OPT) $(JAVAC_OPT))
	
FINAL_RMIC_OPT = $(strip $(FINAL_JAVAC_OPT) \
	$(MOD_RMIC_OPT) $(SITE_RMIC_OPT) $(LOCAL_RMIC_OPT) $(RMIC_OPT))

# Create a macro for recursing into a specific packages.  --unix seems
# to tame things under Windows.  We don't want or need any builtin
# rules.

RECURSE := $(MAKE) --unix --no-builtin-rules

# A few rules used with recursion.  Recursion works by re-invoking
# make in the Module directory, by specifying a value for PACKAGE on
# the command line (in addition to a target).

# Convert each '.' to a path seperator

PACKAGE_DIR := $(subst $(PERIOD),$(SLASH),$(PACKAGE))

# Create a relative project directory by counting the number of
# This is tricky, because $(foreach) like to add spaces, which we have
# to convert to slashes.

_PACKAGE_TERMS := $(subst $(PERIOD),$(SPACE),$(PACKAGE))
_RELATIVE_MOD_DIR := $(strip $(foreach foo,$(_PACKAGE_TERMS),$(DOTDOT)))

RELATIVE_MOD_DIR := $(subst $(SPACE),$(SLASH),$(_RELATIVE_MOD_DIR))

# Builds a command to re-invoke make for a particular package.

MAKE_IN_PACKAGE = \
	$(MAKE) -C $(PACKAGE_DIR) \
	MOD_BUILD_DIR="$(RELATIVE_MOD_DIR)$(SLASH)$(SYS_BUILD_DIR_NAME)" \
	MOD_PACKAGE_DIR="$(PACKAGE_DIR)"

