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

	
default: documentation

include $(SYS_MAKEFILE_DIR)/CommonDefs.mk
include $(SYS_MAKEFILE_DIR)/CommonRules.mk

HTML_DIR := html

DOCUMENT_RESOURCE_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/document-resources
HTML_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/html
VALID_PARSE_STAMP_FILE := $(SYS_BUILD_DIR_NAME)/valid-parse

HTML_PACKAGE_FILE := $(basename $(MAIN_DOCUMENT)).tar.gz

DISTRO_STAMP_FILE := $(DOCBOOK_DIR)/.distro-stamp

DOCBOOK_DISTROS := \
	$(DOCBOOK_DIR)/dtd-4.1.tar.gz \
	$(DOCBOOK_DIR)/dsssl-1.62.tar.gz

initialize: setup-jbe-util $(DISTRO_STAMP_FILE)
ifeq "$(OPENJADE)" ""
	$(error DocBook modules are not supported on this platform.)
endif
ifeq "$(OPENJADE_DIR)" ""
	$(error You must set OPENJADE_DIR to the OpenJade installation directory.)
endif
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
	$(OPENJADE_DSSSL_DIR)/catalog

CATALOG_OPT := $(foreach cat,$(SGML_CATALOG_FILES),-c $(cat))

# This rule validates the document when it (or any component) changes.
# We use the open NSGMLs parsed distributed with OpenJade.  The -s
# option suppresses the normal output (though errors will still be generated).
#
# Note: use of this rule has been disabled because ONSGMLS can't seem to
# produce valid error messages (in the 1.3 release).  To be investigated.
#

$(VALID_PARSE_STAMP_FILE): $(MAIN_DOCUMENT) $(OTHER_DOC_FILES)
	$(call NOTE, "Validating $(MAIN_DOCUMENT) ...")
	$(ONSGMLS) $(CATALOG_OPT) -s $(MAIN_DOCUMENT)
	@$(TOUCH) $@

MOD_HTML_VARIABLE_DEFS := \
	%html-ext%=.html \
	use-output-dir \
	%output-dir%=$(HTML_DIR) \
	%root-filename%=$(basename $(MAIN_DOCUMENT))
	
FINAL_HTML_VARIABLE_DEFS := $(MOD_HTML_VARIABLE_DEFS) $(HTML_VARIABLE_DEFS) $(VARIABLE_DEFS)

FINAL_HTML_STYLESHEET := \
	$(firstword $(HTML_STYLESHEET) $(STYLESHEET) $(DOCBOOK_DSSSL_DIR)/html/docbook.dsl)

# Callable for generating documentation using OpenJade.
#
# Usage:
#
#  $(call RUN_OPENJADE, main document, type, variable defs, type-specific opts)
#
# Example:
#
#  $(call RUN_OPENJADE, sgml, \
#		$(FINAL_HTML_VARIABLE_DEFS), -d $(FINAL_HTML_STYLESHEET) $(HTML_OPENJADE_OPT))
#

RUN_OPENJADE = \
	$(OPENJADE) -t $(1)  $(OPENJADE_OPT) $(3) \
	$(foreach vardef,$(2),-V $(vardef)) \
	$(CATALOG_OPT) \
	$(MAIN_DOCUMENT)

# -t sgml-raw:  This is voodoo black magic.  sgml and sgml-raw both work
# (sgml-raw is prettier), html takes forever and does nothing!

html: initialize $(HTML_STAMP_FILE)

$(HTML_STAMP_FILE): $(DOCUMENT_RESOURCE_STAMP_FILE)  \
	$(MAIN_DOCUMENT) $(FINAL_HTML_STYLESHEET) $(OTHER_DOC_FILES) $(VALID_PARSE_STAMP_FILE)
	$(call NOTE, Generating HTML from $(MAIN_DOCUMENT) ...)
	$(call RUN_OPENJADE, sgml-raw, \
		$(FINAL_HTML_VARIABLE_DEFS), \
		-d $(FINAL_HTML_STYLESHEET) \
		$(HTML_OPENJADE_OPT))
	@$(TOUCH) $@

clean:
	$(call  NOTE, "Cleaning ...")
	@$(RM) $(HTML_DIR) $(SYS_BUILD_DIR_NAME) $(HTML_PACKAGE_FILE)
	
FINAL_HTML_INSTALL_DIR := \
	$(firstword $(HTML_INSTALL_DIR) $(INSTALL_DIR))
	
install-html: html
ifeq "$(FINAL_HTML_INSTALL_DIR)" ""
	$(error You must set a value for HTML_INSTALL_DIR or INSTALL_DIR)
endif
	$(call NOTE, Installing HTML documentation to $(FINAL_HTML_INSTALL_DIR) ...)
	@$(MKDIRS) $(FINAL_HTML_INSTALL_DIR)
	@$(call COPY_TREE, $(HTML_DIR), . , $(FINAL_HTML_INSTALL_DIR))	

package-html: $(HTML_PACKAGE_FILE)

$(HTML_PACKAGE_FILE): html
	$(call NOTE, Packaging HTML as $(HTML_PACKAGE_FILE) ...)
	$(GNUTAR) czf $(HTML_PACKAGE_FILE) --directory=$(HTML_DIR) .

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

FINAL_FORMATS := $(if $(FORMATS),$(FORMATS),html)

documentation: $(FINAL_FORMATS)

install: $(addprefix install-,$(FINAL_FORMATS))

.PHONY: html install-html package-html
.PHONY: default documentation initialize clean
.PHONY: install