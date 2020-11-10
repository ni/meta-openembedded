SUMMARY = "Python interface for c-ares"
DESCRIPTION = "pycares is a Python module which provides an interface to c-ares. c-ares is a C library that performs DNS requests and name resolutions asynchronously."
HOMEPAGE = "https://github.com/saghul/pycares"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b1538fcaea82ebf2313ed648b96c69b1"

SRC_URI[md5sum] = "9d71bb8654cd2f7f34bdfe0ba641b59c"
SRC_URI[sha256sum] = "663c000625725d3a63466a674df4ee7f62bf8ca1ae8a0b87a6411eb811e0e794"

PYPI_PACKAGE = "pycares"
inherit pypi

DEPENDS += "${PYTHON_PN}-cffi-native"

RDEPENDS_${PN} = "\
    ${PYTHON_PN}-cffi \
    ${PYTHON_PN}-idna \
"
