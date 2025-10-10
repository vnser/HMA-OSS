package org.frknkrc44.hma_oss.ui.activity

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import icu.nullptr.hidemyapplist.hmaApp
import icu.nullptr.hidemyapplist.ui.util.ThemeUtils
import icu.nullptr.hidemyapplist.util.ConfigUtils.Companion.getLocale
import org.frknkrc44.hma_oss.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.activity_open_enter,
                R.anim.fade_out,
            )

            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                R.anim.fade_in,
                R.anim.activity_close_exit,
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(
                R.anim.activity_open_enter,
                R.anim.fade_out,
            )
        }

        enableEdgeToEdge()

        DynamicColors.applyToActivityIfAvailable(
            this,
            DynamicColorsOptions.Builder().also {
                if (!ThemeUtils.isSystemAccent)
                    it.setThemeOverlay(ThemeUtils.getColorThemeStyleRes(this))
            }.build()
        )
    }

    override fun onPause() {
        super.onPause()

        if (isFinishing && Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            @Suppress("DEPRECATION")
            overridePendingTransition(
                R.anim.fade_in,
                R.anim.activity_close_exit,
            )
        }
    }

    override fun onApplyThemeResource(theme: Resources.Theme, resid: Int, first: Boolean) {
        super.onApplyThemeResource(theme, resid, first)
        if (!DynamicColors.isDynamicColorAvailable()) {
            theme.applyStyle(ThemeUtils.getColorThemeStyleRes(this), true)
        }

        theme.applyStyle(ThemeUtils.getNightThemeStyleRes(this), true)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(getLocaleAppliedContext(newBase))
    }

    fun getLocaleAppliedContext(context: Context?): Context? {
        val config = hmaApp.resources.configuration
        config.setLocale(getLocale())

        return context?.createConfigurationContext(config)
    }
}