package icu.nullptr.hidemyapplist.xposed

import android.os.Build
import com.github.kyuubiran.ezxhelper.utils.findField

class Utils4Xposed {
    companion object {
        fun getPackageNameFromPackageSettings(packageSettings: Any): String? {
            return runCatching {
                findField(packageSettings::class.java, true) {
                    name == if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) "mName" else "name"
                }.get(packageSettings) as? String
            }.getOrNull()
        }
    }
}
