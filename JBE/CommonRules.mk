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

 
# Rule to make sure the JBE Util class is around.  Usually executed just once, the first
# time the JBE is used.

setup-jbe-util: $(SYS_MAKEFILE_DIR)/com/primix/jbe/Util.class

$(SYS_MAKEFILE_DIR)/com/primix/jbe/Util.class: $(SYS_MAKEFILE_DIR)/com/primix/jbe/Util.java
	@$(ECHO) "\n*** Compiling JBE Utility ... ***\n";
	$(CD) $(SYS_MAKEFILE_DIR) ; \
	$(JAVAC) com/primix/jbe/Util.java

# Note, for this to work, SYS_MAKEFILE_DIR must use only forward slashes. Either
# GNU Make or JAVA is eating the backslashes under NT.

JBE_UTIL = $(JAVA) -classpath $(SYS_MAKEFILE_DIR) com.primix.jbe.Util 

# Command to convert some absolute and/or relative pathnames into
# a list of well-formatted (for GNU Make) absolute pathnames.  Add -classpath
# to use the classpath seperator intead of putting the converted names onto
# seperate lines.

JBE_CANONICALIZE = $(JBE_UTIL) canonicalize

.PHONY: setup-jbe-util