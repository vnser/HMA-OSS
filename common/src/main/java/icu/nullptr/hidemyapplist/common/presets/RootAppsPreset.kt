package icu.nullptr.hidemyapplist.common.presets

import android.content.pm.ApplicationInfo

class RootAppsPreset() : BasePreset("root_apps") {
    override fun onAddExactPackages() {
        // root managers
        packageNames += arrayOf(
            "me.weishu.kernelsu",
            "com.rifsxd.ksunext",
            "com.sukisu.ultra",
        )

        // rooted apps
        packageNames += arrayOf(
            "io.github.a13e300.ksuwebui",
            "io.github.muntashirakon.AppManager",
            "com.fox2code.mmm",
            "xzr.hkf",
            "com.speedsoftware.rootexplorer",
            "id.kuato.diskhealth",
            "com.sunilpaulmathew.debloater",
            "com.garyodernichts.downgrader",
            "eu.roggstar.getmitokens",
            "io.github.domi04151309.powerapp",
            "eu.roggstar.luigithehunter.batterycalibrate",
            "at.or.at.plugoffairplane",
            "tk.giesecke.phoenix",
            "com.corphish.nightlight.generic",
            "com.zinaro.cachecleanerwidget",
            "de.buttercookie.simbadroid",
            "simple.reboot.com",
            "ru.evgeniy.dpitunnel",
            "me.zhanghai.android.files",
            "ca.mudar.fairphone.peaceofmind",
            "com.gitlab.giwiniswut.rwremount",
            "com.machiav3lli.backup",
            "com.bartixxx.opflashcontrol",
            "dev.ukanth.ufirewall",
            "org.nuntius35.wrongpinshutdown",
            "ru.nsu.bobrofon.easysshfs",
            "x1125io.initdlight",
        )

        // kernel managers
        packageNames += arrayOf(
            "flar2.exkernelmanager",
            "com.franco.kernel",
            "com.lybxlpsv.kernelmanager",
            "com.html6405.boefflakernelconfig",
            "ccc71.st.cpu",
        )

        // TODO: Add more rooted apps and other root managers
    }

    override fun onReloadPreset(appInfo: ApplicationInfo) {
        val packageName = appInfo.packageName

        // Iconify
        if (packageName.startsWith("com.drdisagree.iconify")) {
            packageNames.add(packageName)
        }

        // MMRL
        if (packageName.startsWith("com.dergoogler.mmrl")) {
            packageNames.add(packageName)
        }

        // Magisk
        if (packageName.endsWith(".magisk")) {
            packageNames.add(packageName)
        }

        // APatch
        if (packageName.contains(".apatch.") || packageName.endsWith(".apatch")) {
            packageNames.add(packageName)
        }

        // DataBackup
        if (packageName.contains("com.xayah.databackup")) {
            packageNames.add(packageName)
        }

        // SmartPack Kernel Manager + Busybox Installer
        if (packageName.startsWith("com.smartpack.")) {
            packageNames.add(packageName)
        }

        // F-Droid Privileged
        if (packageName.startsWith("org.fdroid.fdroid.privileged")) {
            packageNames.add(packageName)
        }
    }
}
