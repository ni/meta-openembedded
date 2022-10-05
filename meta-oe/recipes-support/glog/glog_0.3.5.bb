DESCRIPTION = "The glog library implements application-level logging. This \
library provides logging APIs based on C++-style streams and various helper \
macros."
HOMEPAGE = "https://github.com/google/glog"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=dc9db360e0bbd4e46672f3fd91dd6c4b"

DEPENDS = "libunwind"

SRC_URI = " \
    git://github.com/google/glog.git;protocol=https;branch=v035 \
    file://0001-find-libunwind-during-configure.patch \
"

SRCREV = "a6a166db069520dbbd653c97c2e5b12e08a8bb26"

S = "${WORKDIR}/git"

inherit cmake

RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dev = "${PN}-staticdev"
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPKGV})"
