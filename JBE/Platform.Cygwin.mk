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

# Defines stuff specific to using the JBE with Cygnus and NT.  This was written for Cygnus
# B20 on Windows NT 4.0.

# Here we define a bunch of command line tools used in the rest of the system.
# Note that it is safe to use the nice, long GNU style options here.  In the other Makefiles,
# it isn't safe, since we want compatibility with Unix (but not GNU) tools on Solaris.

MV := $(TOOLS_DIR)/mv.exe
ECHO := $(TOOLS_DIR)/echo.exe
MKDIRS := $(TOOLS_DIR)/mkdir.exe --parents
TOUCH := $(TOOLS_DIR)/touch.exe
RM := $(TOOLS_DIR)/rm.exe --force 
RMDIRS := $(TOOLS_DIR)/rm.exe --force --recursive
CP := $(TOOLS_DIR)/cp.exe
CAT := $(TOOLS_DIR)/cat.exe
FIND := $(TOOLS_DIR)/find.exe
TAR := $(TOOLS_DIR)/tar.exe

# Provided internally by Cygnus

CD := cd



