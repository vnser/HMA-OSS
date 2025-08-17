package icu.nullptr.hidemyapplist.common.presets

import android.content.pm.ApplicationInfo
import java.util.zip.ZipFile

class XposedModulesPreset() : BasePreset("xposed") {
    override fun onAddExactPackages() {}

    override fun onReloadPreset(appInfo: ApplicationInfo) {
        ZipFile(appInfo.sourceDir).use { zipFile ->
            val manifestFile = zipFile.getInputStream(
                zipFile.getEntry("AndroidManifest.xml"))
            val manifestBytes = manifestFile.use { it.readBytes() }
            val manifestStr = String(manifestBytes, Charsets.US_ASCII)

            // Checking with binary because the Android system sucks
            if (manifestStr.contains("\u0000x\u0000p\u0000o\u0000s\u0000e\u0000d\u0000m\u0000o\u0000d\u0000u\u0000l\u0000e")) {
                packageNames.add(appInfo.packageName)
            } else {
                if (zipFile.getEntry("META-INF/xposed")?.isDirectory ?: false) {
                    packageNames.add(appInfo.packageName)
                }
            }
        }
    }
}
