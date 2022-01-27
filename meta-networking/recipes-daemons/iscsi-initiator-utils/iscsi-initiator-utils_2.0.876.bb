SUMMARY = "iSCSI daemon and utility programs"
DESCRIPTION = "Open-iSCSI project is a high performance, transport \
independent, multi-platform implementation of RFC3720. The iscsi package \
provides the server daemon for the iSCSI protocol, as well as the utility \
programs used to manage it. iSCSI is a protocol for distributed \
disk access using SCSI commands sent over Internet Protocol networks."
HOMEPAGE = "http://www.open-iscsi.com/"
LICENSE = "GPLv2 & LGPLv2.1"
SECTION = "net"
DEPENDS = "openssl flex-native bison-native open-isns util-linux"

LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRCREV ?= "24580adc4c174bbc5dde3ae7594a46d57635e906"

SRC_URI = "git://github.com/open-iscsi/open-iscsi;protocol=https \
    file://initd.debian \
    file://99_iscsi-initiator-utils \
    file://iscsi-initiator \
    file://iscsi-initiator.service \
    file://iscsi-initiator-targets.service \
    file://set_initiatorname \
    file://0001-libopeniscsiusr-Include-limit.h-for-PATH_MAX.patch \
    file://0002-libopeniscsiusr-Add-CFLAGS-to-linker-cmdline.patch \
    file://0001-qedi.c-Removed-unused-linux-ethtool.h.patch \
    file://0002-idbm.c-Include-fcnl.h-for-O_RDWR-and-O_CREAT-definit.patch \
    file://0003-bnx2x.c-Reorder-the-includes-to-avoid-duplicate-defi.patch \
    file://0004-fwparam_ppc.c-Do-not-use-__compar_fn_t.patch \
"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit update-rc.d systemd autotools

EXTRA_OECONF = " \
    --target=${TARGET_SYS} \
    --host=${BUILD_SYS} \
"

EXTRA_OEMAKE = ' \
    OS="${TARGET_SYS}" \
    TARGET="${TARGET_OS}" \
    BASE="${prefix}" \
    MANDIR="${mandir}" \
'

do_configure () {
    cd ${S}/iscsiuio ; autoreconf --install; ./configure ${EXTRA_OECONF}
}

do_compile () {
    # Make sure we DO NOT regenerate prom_lex.c.
    if [ -f ${S}/utils/fwparam_ibft/prom_lex.l ]; then
        mv ${S}/utils/fwparam_ibft/prom_lex.l ${S}/utils/fwparam_ibft/prom_lex.l.unused
    fi
    oe_runmake -C ${S} ${EXTRA_OEMAKE} user
}

do_install () {
    #install necessary directories
    install -d ${D}${sbindir} \
        ${D}${sysconfdir}/init.d \
        ${D}${sysconfdir}/iscsi \
        ${D}${localstatedir}/lib/iscsi/nodes \
        ${D}${localstatedir}/lib/iscsi/send_targets \
        ${D}${localstatedir}/lib/iscsi/static \
        ${D}${localstatedir}/lib/iscsi/isns \
        ${D}${localstatedir}/lib/iscsi/slp \
        ${D}${localstatedir}/lib/iscsi/ifaces \
        ${D}${libdir} \
        ${D}${mandir}/man8

    install -p -m 755 ${S}/usr/iscsid ${S}/usr/iscsiadm \
        ${S}/utils/iscsi-iname \
        ${S}/usr/iscsistart ${D}/${sbindir}

    cp -dR ${S}/libopeniscsiusr/libopeniscsiusr.so* ${D}${libdir}
    install -p -m 644 ${S}/doc/iscsiadm.8 ${S}/doc/iscsid.8 ${D}/${mandir}/man8
    install -p -m 644 ${S}/etc/iscsid.conf ${D}${sysconfdir}/iscsi
    install -p -m 755 ${WORKDIR}/initd.debian ${D}${sysconfdir}/init.d/iscsid

    sed -i -e "s:= /sbin/iscsid:= ${sbindir}/iscsid:" ${D}${sysconfdir}/iscsi/iscsid.conf

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/tmpfiles.d
        echo "d /run/${BPN}/lock - - - -" \
                     > ${D}${sysconfdir}/tmpfiles.d/iscsi.conf
        install -d ${D}/etc/default/
        install -p -m 755 ${WORKDIR}/iscsi-initiator ${D}${sysconfdir}/default/

        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/iscsi-initiator.service \
                        ${WORKDIR}/iscsi-initiator-targets.service \
                        ${D}${systemd_unitdir}/system/
        install -d ${D}${nonarch_libdir}/iscsi
        install -m 0755 ${WORKDIR}/set_initiatorname ${D}${nonarch_libdir}/iscsi
    else
        install -d ${D}/etc/default/volatiles
        install -m 0644 ${WORKDIR}/99_iscsi-initiator-utils ${D}/etc/default/volatiles
    fi
}

pkg_postinst_${PN}() {
    if [ "x$D" = "x" ]; then
        if [ -e /etc/init.d/populate-volatile.sh ]; then
            /etc/init.d/populate-volatile.sh update
        elif command -v systemd-tmpfiles >/dev/null; then
            systemd-tmpfiles --create ${sysconfdir}/tmpfiles.d/iscsi.conf
        fi
    fi
}

SYSTEMD_SERVICE = " iscsi-initiator.service iscsi-initiator-targets.service "
INITSCRIPT_NAME = "iscsid"
INITSCRIPT_PARAMS = "start 30 1 2 3 4 5 . stop 70 0 1 2 3 4 5 6 ."

FILES_${PN} += "${nonarch_libdir}/iscsi"
