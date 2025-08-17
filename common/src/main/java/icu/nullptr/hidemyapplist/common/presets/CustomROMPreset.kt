package icu.nullptr.hidemyapplist.common.presets

import android.content.pm.ApplicationInfo
import icu.nullptr.hidemyapplist.common.Utils

class CustomROMPreset() : BasePreset("custom_rom") {
    override fun onAddExactPackages() {}

    override fun onReloadPreset(appInfo: ApplicationInfo) {
        val packageName = appInfo.packageName

        // LineageOS apps
        if (Utils.startsWithMultiple(packageName, "lineageos.", "org.lineageos.")) {
            packageNames.add(packageName)
        }

        // AOSPA
        if (packageName.startsWith("co.aospa.")) {
            packageNames.add(packageName)
        }

        // OmniROM
        if (packageName.startsWith("org.omnirom.")) {
            packageNames.add(packageName)
        }

        // ProtonAOSP
        if (packageName.startsWith("org.protonaosp.")) {
            packageNames.add(packageName)
        }

        // TODO: Add more custom ROM apps
    }
}
