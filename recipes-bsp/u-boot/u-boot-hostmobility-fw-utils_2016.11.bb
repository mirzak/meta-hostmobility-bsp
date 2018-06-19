SUMMARY = "U-boot bootloader fw_printenv/setenv utils"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SECTION = "bootloader"
PROVIDES = "u-boot-fw-utils"
RPROVIDES_${PN} = "u-boot-fw-utils"
DEPENDS = "mtd-utils"

DEFAULT_PREFERENCE = "1"

FILESPATHPKG =. "git:"

SRCREV = "a4c89a31125f1fa11f3809ed908fc720b30af8a5"
SRCBRANCH = "2016.11-hm"
SRC_URI = " \
    git://github.com/hostmobility/u-boot-toradex.git;protocol=https;branch=${SRCBRANCH} \
    file://fw_env.config \
"

SRC_URI_append_tegra3 = " file://fw_unlock_mmc.sh"

PV = "v2016.11-hm+git${SRCPV}"

S = "${WORKDIR}/git"

INSANE_SKIP_${PN} = "already-stripped ldflags"
EXTRA_OEMAKE_class-target = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${CC} ${CFLAGS} ${LDFLAGS}" HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" V=1'
EXTRA_OEMAKE_class-cross = 'ARCH=${TARGET_ARCH} CC="${CC} ${CFLAGS} ${LDFLAGS}" V=1'

inherit pkgconfig uboot-config

do_compile () {
    oe_runmake ${UBOOT_MACHINE}
    oe_runmake env
}

do_install () {
    install -d ${D}${base_sbindir} ${D}${sysconfdir}
    install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
    ln -s fw_printenv ${D}${base_sbindir}/fw_setenv
    install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/
}

do_install_append_tegra3() {
    install -d ${D}${sysconfdir}/profile.d/
    install -m 0644 ${WORKDIR}/fw_unlock_mmc.sh ${D}${sysconfdir}/profile.d/fw_unlock_mmc.sh
}

PACKAGE_ARCH = "${MACHINE_ARCH}"