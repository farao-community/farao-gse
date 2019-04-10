# FARAO - GSE

[![Build Status](https://travis-ci.com/farao-community/farao-gse.svg?branch=master)](https://travis-ci.com/farao-community/farao-gse)
[![MPL-2.0 License](https://img.shields.io/badge/license-MPL_2.0-blue.svg)](https://www.mozilla.org/en-US/MPL/2.0/)
[![Join the community on Spectrum](https://withspectrum.github.io/badge/badge.svg)](https://spectrum.chat/farao-community)

![FARAO horizontal logo](https://raw.githubusercontent.com/farao-community/.github/master/logo-farao-horizontal.svg?sanitize=true) 

FARAO stands for *Fully Autonomous Remedial Actions Optimisation*. It is an open-source toolbox that aims at providing a modular engine for remedial actions optimisation.

**farao-gse** repository contains a graphical user interface for FARAO. It is a desktop application written in [JavaFX](https://openjfx.io/).

For detailed information about FARAO toolbox, please refer to the [documentation website](https://farao-community.github.io/docs/)

This project and everyone participating in it is governed by the [FARAO Code of Conduct](https://github.com/farao-community/.github/blob/master/CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.
Please report unacceptable behavior to [contact@farao-community.com](mailto:contact@farao-community.com).

## Getting started

These instructions will get you a copy of the project up and running on your local machine
for development and testing purposes.

### Prerequisites

In order to build **farao-gse**, you need the following environment available:
  - Install JDK *(1.8 or greater)*,
  - Install Java FX,
  ```
  $> sudo dnf install java-1.8.0-openjdk-openjfx java-1.8.0-openjdk-openjfx-devel
  ```
  - Install Maven latest version,
  - Install [**farao-core**](https://github.com/farao-community/farao-core).

### Installing

To build **farao-gse**, enter on the command line:

```
$> git clone https://github.com/farao-community/farao-gse.git
$> cd farao-gse
$> ./install.sh
```

It is installed by default in the "*$HOME/farao*" directory. This default install directory can
be modified using *--prefix* option.

```
$> ./install.sh --prefix=/path/to/the/chosen/install/directory
```
### Running

For using *farao-gse*, enter on the command line:

```bash
cd <install-prefix>/bin
./farao-gse-launcher
```

## License

This project is licensed under the Mozilla Public License 2.0 - see the [LICENSE.txt](https://github.com/farao-community/farao-gse/blob/master/LICENSE.txt) file for details.
