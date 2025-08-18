package icu.nullptr.hidemyapplist.common.presets

import android.content.pm.ApplicationInfo

abstract class BasePreset(val name: String) {
    val packageNames = mutableSetOf<String>()

    abstract fun onAddExactPackages()

    abstract fun onReloadPreset(appInfo: ApplicationInfo): Boolean
}
