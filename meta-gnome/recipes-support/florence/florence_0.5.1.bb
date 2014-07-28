DESCRIPTION = "Florence is a virtual keyboard (also called on-screen-keyboard), which allows the user to input text through a touchscreen."
HOMEPAGE = "http://florence.sourceforge.net/english.html"

#NOTICE: If florence can't find its gconf settings, you need to start florence with --config for one time and save the configuration once.

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

PR = "r1"

DEPENDS = "gtk+ libxml2 libglade librsvg libxtst gconf cairo intltool-native gnome-doc-utils libnotify"

SRC_URI = "${SOURCEFORGE_MIRROR}/florence/florence/${PV}/florence-${PV}.tar.bz2 \
           file://0001-Fix-glib-includes.patch \
           file://0002-Fix-crash-on-layout-change.patch \
           file://0003-Add-minimization-functionality.patch \
           file://0004-Add-option-for-automatic-bring-to-top.patch \
           file://0005-Unpress-key-when-mouse-leaves-key-area.patch \
           file://0006-Fix-memory-leak.patch"

SRC_URI[md5sum] = "56d12e5b47c100d9df172aa5ddc0f609"
SRC_URI[sha256sum] = "7b06ed84ef2b7b22d8d2cf0c7d013a05409bd82028240ac8719a68b192d5bc62"

inherit gettext autotools gconf pkgconfig

EXTRA_OECONF = "--disable-scrollkeeper --without-docs --without-at-spi --without-panelapplet"
