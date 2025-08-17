package icu.nullptr.hidemyapplist.common

import android.content.pm.IPackageManager
import android.content.pm.PackageManager
import icu.nullptr.hidemyapplist.common.Utils.getInstalledApplicationsCompat
import icu.nullptr.hidemyapplist.common.presets.BasePreset
import icu.nullptr.hidemyapplist.common.presets.CustomROMPreset
import icu.nullptr.hidemyapplist.common.presets.RootAppsPreset
import icu.nullptr.hidemyapplist.common.presets.XposedModulesPreset

// TODO: Update presets when package added/removed
class Presets private constructor() {
    val presetList = mutableListOf<BasePreset>()
    var loggerFunction: ((String) -> Unit)? = null

    companion object {
        private var hiddenInstance: Presets? = null

        val instance: Presets
            get() {
                if (hiddenInstance == null) {
                    hiddenInstance = Presets()
                }

                return hiddenInstance!!
            }
    }

    fun getAllPresetNames() = presetList.map { it.name }.toTypedArray()
    // fun filterPresetsByName(names: Array<String>) = presetList.filter { names.contains(it.name) }
    fun getPresetByName(name: String) = presetList.firstOrNull { it.name == name }

    fun reloadPresetsIfEmpty(pms: IPackageManager) {
        presetList.forEach {
            if (it.packageNames.isEmpty()) {
                it.onAddExactPackages()

                for (appInfo in getInstalledApplicationsCompat(pms, PackageManager.GET_META_DATA.toLong(), 0)) {
                    runCatching {
                        it.onReloadPreset(appInfo)
                    }.onFailure { fail ->
                        loggerFunction?.invoke(fail.toString())
                    }
                }
            }

            loggerFunction?.invoke("Package list for ${it.name}: ${it.packageNames}")
        }
    }

    init {
        presetList.add(CustomROMPreset())
        presetList.add(RootAppsPreset())
        presetList.add(XposedModulesPreset())
    }
}


