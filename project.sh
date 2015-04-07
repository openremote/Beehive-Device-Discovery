#!/bin/sh
#  --------------------------------------------------------------------
#  Copyright 2013-2015, Juha Lindfors. All rights reserved.
#
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU Affero General Public License as
#  published by the Free Software Foundation; either version 3 of the
#  License, or (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful, but
#  WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
#  Affero General Public License for more details.
#
#  You should have received a copy of the GNU Affero General Public
#  License along with this program; if not, see
#  http://www.gnu.org/licenses/.
#  --------------------------------------------------------------------
#
##
#  Convenience script to run project build targets. Use:
#
#    > sh project.sh --help
#
#  to view usage details.
#
#  Author: Juha Lindfors 
#
##

printHelp()
{
  echo "";
  echo "-------------------------------------------------------------";
  echo "";
  echo "  Copyright 2013-2015, Juha Lindfors. All rights reserved.";
  echo "";
  echo "  Executes project template build targets.";
  echo "";
  echo "-------------------------------------------------------------";
  echo "";
  echo "  Usage: ";
  echo "";
  echo "    sh project.sh";
  echo "";
  echo "         - Runs the default unit-test target of this project.";
  echo "";
  echo "    sh project.sh [target]";
  echo "";
  echo "         - Executes a project build target.";
  echo "";
  echo "    sh project.sh [-p, -projecthelp]";
  echo "";
  echo "         - Lists all available project build targets.";
  echo "";
  echo "    sh project.sh update";
  echo "";
  echo "         - Updates this project build template.";
  echo "";
  echo "    sh project.sh update-complete";
  echo "";
  echo "         - Updates project including Java runtime libraries";
  echo "           required for cross-compiling.";
  echo "";
  echo "    sh project.sh [target] [-Dproperty=value]...";
  echo "";
  echo "         - Executes project build target with given property values.";
  echo "";
  echo "    sh project.sh [target] -verbose";
  echo "";
  echo "         - Verbose output useful to troubleshoot build issues.";
  echo "";
  echo "";
  echo "Create a 'local.properties' file in the root directory to override";
  echo "any of the build properties for changes that are intended to be";
  echo "kept local and not shared with others (via VCS for example) and";
  echo "modify 'build.properties' file for properties that should be";
  echo "changed permanently and shared with other users.";
  echo "";
  echo "";

  exit 0;
}

if [ "$1" = "help" -o "$1" = "-h" -o "$1" = "--help" ]; then
  printHelp;
fi

if [ "$1" = "update" -o "$1" = "update-complete" ]; then
  ant -f build/project/auto-updated/do-not-modify/project-update.xml $@
else
  ant -f build.xml $@;
  echo "";
  echo "";
fi


