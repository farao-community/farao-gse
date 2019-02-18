# FARAO - GSE

[![Build Status](https://travis-ci.com/farao-community/farao-gse.svg?branch=master)](https://travis-ci.com/farao-community/farao-gse)
[![MPL-2.0 License](https://img.shields.io/badge/license-MPL_2.0-blue.svg)](https://www.mozilla.org/en-US/MPL/2.0/)

For detailed information about FARAO toolbox, please refer to the [documentation website](https://farao-community.github.io/docs/)

## Requirements
In order to build **farao-gse**, you need the following environment available:
  - Install JDK *(1.8 or greater)*
  - Install Java FX
  ```
  $> sudo dnf install java-1.8.0-openjdk-openjfx java-1.8.0-openjdk-openjfx-devel
  ```
  - Install Maven latest version
  - Install **farao-core**

## Install
To build farao-gse, just do the following:

```
$> git clone https://github.com/farao-community/farao-gse.git
$> cd farao-gse
$> ./install.sh
```
By default it is installed in the "*$HOME/farao*" directory. This default install directory can
be modified using *--prefix* option.

```
$> ./install.sh --prefix=/path/to/the/chosen/install/directory
```

FARAO also needs a loadflow engine and a sensitivity calculation engine.

Hades2 tool from RTE is available as a freeware for demonstration purpose.
For more information about how to get and install Hades2 loadflow, please refer to the
[dedicated documentation](https://rte-france.github.io/hades2/index.html)

## Configure your itools platform
In order for farao to run without error, you will need to configure your itools platform.

Two options are available:
1.  First, you can use the one provided by FARAO. It is saved in the *etc* directory of the installation, and is called *config.yml*.
You just have to copy-paste it in **$HOME/.itools** directory. 

2.  Expert users can also adapt it to their own needs.

## Using itools
```bash
cd <install-prefix>/bin
./itools help
```

## Launching FARAO GSE application
```bash
cd <install-prefix>/bin
./farao-gse-launcher
```
