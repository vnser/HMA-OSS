package icu.nullptr.hidemyapplist.common

import android.os.SystemProperties
import icu.nullptr.hidemyapplist.common.Constants.GMS_PACKAGE_NAME
import icu.nullptr.hidemyapplist.common.Constants.GSF_PACKAGE_NAME
import icu.nullptr.hidemyapplist.common.Constants.VENDING_PACKAGE_NAME

object CommonUtils {
    val gmsPackages = arrayOf(GMS_PACKAGE_NAME, GSF_PACKAGE_NAME, VENDING_PACKAGE_NAME)
    val riskyPackages = arrayOf<String>() + gmsPackages

    val isAppDataIsolationEnabled: Boolean
        get() = SystemProperties.getBoolean(Constants.ANDROID_APP_DATA_ISOLATION_ENABLED_PROPERTY, true)

    val isVoldAppDataIsolationEnabled: Boolean
        get() = SystemProperties.getBoolean(Constants.ANDROID_VOLD_APP_DATA_ISOLATION_ENABLED_PROPERTY, false)
}
