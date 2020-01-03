SUMMARY = "Simple DNS resolver for asyncio"
DESCRIPTION = "aiodns provides a simple way for doing asynchronous DNS resolutions using pycares."
HOMEPAGE = "https://github.com/saghul/aiodns"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a565d8b5d06b9620968a135a2657b093"

SRC_URI[md5sum] = "3e121f9eb7ef3ba3556ba7ec28c6f63a"
SRC_URI[sha256sum] = "815fdef4607474295d68da46978a54481dd1e7be153c7d60f9e72773cd38d77d"

PYPI_PACKAGE = "aiodns"
inherit pypi
RDEPENDS_${PN} = "\
    ${PYTHON_PN}-pycares \
"
