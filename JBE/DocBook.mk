# $Id$
#
# Tapestry Web Application Framework
# Copyright (c) 2001 by Howard Ship and Primix
#
# Primix
# 311 Arsenal Street
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

# Allows for the creation of HTML from a DocBook SGML file.
# Automatically handles installation of the necessary DocBook DTDs, the
# DSSSL stylesheets and the jade DSSSL processor.

	
default: html

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk

HTML_DIR := html

DOCUMENT_RESOURCE_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/document-resources

DISTRO_STAMP_FILE := $(DOCBOOK_DIR)/.distro-stamp

DOCBOOK_DISTROS := \
	$(DOCBOOK_DIR)/dtd-4.1.tar.gz \
	$(DOCBOOK_DIR)/dsssl-1.62.tar.gz \
	$(OPENJADE_DISTRO)

initialize: setup-jbe-util $(DISTRO_STAMP_FILE)
	@$(MKDIRS) $(SYS_BUILD_DIR_NAME) $(HTML_DIR)

$(DISTRO_STAMP_FILE): $(DOCBOOK_DISTROS)
	$(call NOTE, "Unarchiving DocBook distributions ...")
	@for archive in $? ; do \
		$(ECHO) Extracting: $$archive ; \
		$(CAT) $$archive | \
		($(CD) $(DOCBOOK_DIR) && \
		 $(GNUTAR) $(GNUTAR_EXTRACT_OPT) $(GNUTAR_GZIP_OPT)) ; \
	done
	@$(TOUCH) $@

SGML_CATALOG_FILES := \
	$(DOCBOOK_DIR)/dtd/docbook.cat \
	$(DOCBOOK_DSSSL_DIR)/catalog \
	$(DOCBOOK_OPENJADE_DIR)/dsssl/catalog

MOD_VARIABLE_DEFS := \
	%html-ext%=.html \
	%html-prefix%=$(HTML_DIR)/ \
	%root-filename%="$(HTML_DIR)/$(basename $(MAIN_DOCUMENT))"
	
FINAL_VARIABLE_DEFS := $(MOD_VARIABLE_DEFS) $(VARIABLE_DEFS)

FINAL_STYLESHEET = $(firstword $(STYLESHEET) $(DOCBOOK_DSSSL_DIR)/html/docbook.dsl)

html: initialize $(DOCUMENT_RESOURCE_STAMP_FILE)
	$(call NOTE, Generating HTML documentation from $(MAIN_DOCUMENT) ...)
	$(OPENJADE) -t sgml -d $(FINAL_STYLESHEET) $(OPENJADE_OPT) \
	$(foreach vardef,$(FINAL_VARIABLE_DEFS),-V $(vardef)) \
	$(foreach cat,$(SGML_CATALOG_FILES),-c $(cat)) \
	$(MAIN_DOCUMENT)


clean:
	$(call  NOTE, "Cleaning ...")
	@$(RM) $(HTML_DIR) $(SYS_BUILD_DIR_NAME)
	
install: html
ifeq "$(INSTALL_DIR)" ""
	$(error You must set a value for INSTALL_DIR)
endif
	$(call NOTE, Installing HTML documentation to $(INSTALL_DIR) ...)
	@$(MKDIRS) $(INSTALL_DIR)
	$(call COPY_TREE, $(HTML_DIR), . , $(INSTALL_DIR))	

# Rules for dealing with DOCUMENT_RESOURCES, generally images (or directories of images)
# that should be included with the generated HTML. 

ifneq "$(DOCUMENT_RESOURCES)" ""
FINAL_DOCUMENT_RESOURCES := \
	${shell $(FIND) $(DOCUMENT_RESOURCES) \
		-name \* ! \( -name CVS -prune \) -type f \
		-print}
endif

$(DOCUMENT_RESOURCE_STAMP_FILE): $(FINAL_DOCUMENT_RESOURCES)
ifneq "$(FINAL_DOCUMENT_RESOURCES)" ""
	$(call NOTE, Copying document resources ...)
	@$(ECHO) Copying: $(notdir $?)
	@$(call COPY_TREE, . , $? , $(HTML_DIR))
endif
	@$(TOUCH) $@

.PHONY: default html initialize install