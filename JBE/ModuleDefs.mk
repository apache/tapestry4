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


# Get the final source directory which is either specified by the
# SOURCE_DIR variable, or defaults to the current directory.

FINAL_SOURCE_DIR := $(firstword $(SOURCE_DIR) .)

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

# Create a macro for recursing into a specific packages.  --unix seems
# to tame things under Windows.  We don't want or need any builtin
# rules.

RECURSE := $(MAKE) --unix --no-builtin-rules

# Note, for this to work, SYS_MAKEFILE_DIR must use only forward slashes. Either
# GNU Make or JAVA is eating the backslashes under NT.

JBE_UTIL = $(JAVA) -classpath $(SYS_MAKEFILE_DIR) com.primix.jbe.Util 

# Command to convert some absolute and/or relative pathnames into
# a list of well-formatted (for GNU Make) absolute pathnames.  Add -classpath
# to use the classpath seperator intead of putting the converted names onto
# seperate lines.

JBE_CANONICALIZE = $(JBE_UTIL) canonicalize