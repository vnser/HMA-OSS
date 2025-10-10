package icu.nullptr.hidemyapplist.common.app_presets

import android.content.pm.ApplicationInfo
import java.util.zip.ZipFile

class SuspiciousAppsPreset : BasePreset("sus_apps") {
    override val exactPackageNames = setOf(
        "com.reveny.vbmetafix.service",
        "com.speedsoftware.rootexplorer",
        "me.zhanghai.android.files",
        "com.lonelycatgames.Xplore",
        "org.fossify.filemanager",

        // Detector apps
        "com.reveny.nativecheck",
        "icu.nullptr.nativetest",
        "io.github.rabehx.securify",
        "com.zhenxi.hunter",
        "io.github.vvb2060.mahoshojo",
        "io.github.huskydg.memorydetector",
        "org.akanework.checker",
        "icu.nullptr.applistdetector",
        "com.byxiaorun.detector",
        "com.kimchangyoun.rootbeerFresh.sample",
        "com.androidfung.drminfo",
        "com.kikyps.crackme",
        "org.matrix.demo",
        "com.rem01gaming.disclosure",
        "luna.safe.luna",
        "com.AndroLua",
        "com.detect.mt",
        "io.liankong.riskdetector",
        "com.suisho.rc",
        "com.ahmed.security_tester",
        "id.my.pjm.qbcd_okr_dvii",
        "wu.Zygisk.Detector",
        "com.atominvention.rootchecker",

        // Play Integrity checkers
        "krypton.tbsafetychecker",
        "gr.nikolasspyr.integritycheck",
        "com.henrikherzig.playintegritychecker",
        "com.thend.integritychecker",
        "com.flinkapps.safteynet",

        // Other checkers
        "com.bryancandi.knoxcheck",
    )

    val libNames = arrayOf(
        "liblsplant.so",
    )

    val assetNames = arrayOf(
        // ~All possible APK editors
        "APKEditor.pk8",
        "testkey.pk8",
        "key/testkey.pk8",

        // TODO: Add more suspicious apps by checking for files
    )

    override fun canBeAddedIntoPreset(appInfo: ApplicationInfo): Boolean {
        val packageName = appInfo.packageName

        // Termux, all of its plugins and some of Termux forks
        if (packageName.startsWith("com.termux")) {
            return true
        }

        // All Garfield packages
        if (packageName.startsWith("me.garfieldhan.")) {
            return true
        }

        // All Shizuku apps
        if (packageName.startsWith("moe.shizuku.")) {
            return true
        }

        // All RealVNC apps (categorized as suspicious, because some of apps checking for them)
        if (packageName.startsWith("com.realvnc.")) {
            return true
        }

        // FX File Manager
        if (packageName.startsWith("nextapp.fx")) {
            return true
        }

        // TotalCommander and its plugins
        if (packageName.startsWith("com.ghisler.")) {
            return true
        }

        // ZDevs apps (ZArchiver etc.)
        if (packageName.startsWith("ru.zdevs.")) {
            return true
        }

        // MiXplorer, MiXplorer Silver and its plugins
        if (packageName.startsWith("com.mixplorer")) {
            return true
        }

        // MT Manager
        if (packageName.startsWith("bin.mt.plus")) {
            return true
        }

        // Key attestation apps
        if (packageName.endsWith(".keyattestation")) {
            return true
        }

        ZipFile(appInfo.sourceDir).use { zipFile ->
            if (findAppsFromLibs(zipFile, libNames) || findAppsFromAssets(zipFile, assetNames)) {
                return true
            }
        }

        // TODO: Add more suspicious apps
        return false
    }
}