#!/bin/bash

# Copyright (c) 2018, RTE (http://www.rte-france.com)
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.

sourceDir=$(dirname $(readlink -f $0))

## install default settings
###############################################################################
farao_gse_prefix=$HOME/farao

# Targets
farao_gse_clean=false
farao_gse_compile=false
farao_gse_docs=false
farao_gse_package=false
farao_gse_install=false


## read settings from configuration file
###############################################################################
settings="$sourceDir/install.cfg"
if [ -f "${settings}" ]; then
     source "${settings}"
fi


## Usage/Help
###############################################################################
cmd=$0
usage() {
    echo "usage: $cmd [options] [target...]"
    echo ""
    echo "Available targets:"
    echo "  clean                    Clean farao-gse modules"
    echo "  compile                  Compile farao-gse modules"
    echo "  package                  Compile farao-gse modules and create a distributable package"
    echo "  install                  Compile farao-gse modules and install it (default target)"
    echo "  help                     Display this help"
    echo "  docs                     Generate the documentation (Javadoc)"
    echo ""
    echo "farao-gse options:"
    echo "  --help                   Display this help"
    echo "  --prefix                 Set the installation directory (default is $HOME/farao)"
    echo ""
}


## Write Settings functions
###############################################################################
writeSetting() {
    if [[ $# -lt 2 || $# -gt 3 ]]; then
        echo "WARNING: writeSetting <setting> <value> [comment (true|false)]"
        exit 1
    fi

    SETTING=$1
    VALUE=$2
    if [[ $# -eq 3 ]]; then
        echo -ne "# "
    fi
    echo "${SETTING}=${VALUE}"

    return 0
}

writeComment() {
    echo "# $*"
    return 0
}

writeEmptyLine() {
    echo ""
    return 0
}

writeSettings() {
    writeComment " -- farao-gse global options --"
    writeSetting "farao_gse_prefix" ${farao_gse_prefix}

    return 0
}


## Build Java Modules
###############################################################################
farao_core_java()
{
    if [[ ${farao_gse_clean} = true || ${farao_gse_compile} = true || ${farao_gse_docs} = true || ${farao_gse_package} = true ]]; then
        echo "** Building farao Java modules"

        mvn_options="-Pnative-package"
        [ ${farao_gse_clean} = true ] && mvn_options="$mvn_options clean"
        [ ${farao_gse_compile} = true ] && mvn_options="$mvn_options install"
        [ ${farao_gse_package} = true ] && mvn_options="$mvn_options package"
        if [ ! -z "$mvn_options" ]; then
            mvn -f "$sourceDir/pom.xml" ${mvn_options} || exit $?
        fi

        if [ ${farao_gse_docs} = true ]; then
            echo "**** Generating Javadoc documentation"
            mvn -f "$sourceDir/pom.xml" javadoc:javadoc || exit $?
            mvn -f "$sourceDir/distribution/pom.xml" install || exit $?
        fi
    fi
}

## Install farao
###############################################################################
farao_install()
{
    if [ ${farao_gse_install} = true ]; then
        echo "** Installing farao"

        echo "**** Copying files"
        mkdir -p "$farao_gse_prefix" || exit $?
        cp -Rpf "$sourceDir/distribution/target/jfx/native"/farao-gse "$farao_gse_prefix" || exit $?
    fi
}

## Parse command line
farao_options="prefix:"

opts=`getopt -o '' --long "help,$farao_options" -n 'install.sh' -- "$@"`
eval set -- "${opts}"
###############################################################################
while true; do
    case "$1" in
        # farao options
        --prefix) farao_gse_prefix=$2 ; shift 2 ;;

        # Help
        --help) usage ; exit 0 ;;

        --) shift ; break ;;
        *) usage ; exit 1 ;;
    esac
done

if [ $# -ne 0 ]; then
    for command in $*; do
        case "$command" in
            clean) farao_gse_clean=true ;;
            compile) farao_gse_compile=true ;;
            docs) farao_gse_docs=true ;;
            package) farao_gse_package=true ; farao_gse_compile=true ;;
            install) farao_gse_install=true ; farao_gse_compile=true ;;
            help) usage; exit 0 ;;
            *) usage ; exit 1 ;;
        esac
    done
else
    farao_gse_compile=true
    farao_gse_package=true
    farao_gse_install=true
fi

## Build farao platform
###############################################################################

# Build Java modules
farao_core_java

# Install farao
farao_install

# Save settings
writeSettings > "${settings}"
