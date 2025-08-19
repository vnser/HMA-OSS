package icu.nullptr.hidemyapplist.common.presets

import android.content.pm.ApplicationInfo
import java.util.zip.ZipFile

class RootAppsPreset() : BasePreset("root_apps") {
    override fun onAddExactPackages() {
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

    override fun onReloadPreset(appInfo: ApplicationInfo): Boolean {
        val packageName = appInfo.packageName

        if (packageNames.contains(packageName)) {
            return false
        }

        // Iconify
        if (packageName.startsWith("com.drdisagree.iconify")) {
            packageNames.add(packageName)
            return true
        }

        // MMRL
        if (packageName.startsWith("com.dergoogler.mmrl")) {
            packageNames.add(packageName)
            return true
        }

        // Magisk
        if (packageName.endsWith(".magisk")) {
            packageNames.add(packageName)
            return true
        }

        // APatch
        if (packageName.contains(".apatch.") || packageName.endsWith(".apatch")) {
            packageNames.add(packageName)
            return true
        }

        // DataBackup
        if (packageName.contains("com.xayah.databackup")) {
            packageNames.add(packageName)
            return true
        }

        // SmartPack Kernel Manager + Busybox Installer
        if (packageName.startsWith("com.smartpack.")) {
            packageNames.add(packageName)
            return true
        }

        // F-Droid Privileged
        if (packageName.startsWith("org.fdroid.fdroid.privileged")) {
            packageNames.add(packageName)
            return true
        }

        ZipFile(appInfo.sourceDir).use { zipFile ->
            if (findRootManagerFromLibs(zipFile)) {
                packageNames.add(appInfo.packageName)
                return true
            }
        }

        return false
    }

    private fun findRootManagerFromLibs(zipFile: ZipFile): Boolean {
        val entryNames = listOf(
            "libkernelsu.so",
            "libapd.so",
            "libmagisk.so",
            "libmmrl-kernelsu.so"
        )
        val architectures = listOf("arm64-v8a", "armeabi-v7a")

        for (entry in entryNames) {
            for (arch in architectures) {
                if (zipFile.getEntry("lib/$arch/$entry") != null) {
                    return true
                }
            }
        }

        return false
    }
}
