package org.frknkrc44.hma_oss.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.color.DynamicColors
import icu.nullptr.hidemyapplist.hmaApp
import icu.nullptr.hidemyapplist.ui.util.ThemeUtils
import icu.nullptr.hidemyapplist.ui.util.ThemeUtils.homeItemBackgroundColor
import icu.nullptr.hidemyapplist.util.ConfigUtils.Companion.getLocale
import org.frknkrc44.hma_oss.R
import org.frknkrc44.hma_oss.common.BuildConfig
import org.frknkrc44.hma_oss.databinding.ActivityAboutBinding
import org.frknkrc44.hma_oss.databinding.ActivityAboutListItemBinding
import org.json.JSONObject
import androidx.core.net.toUri

@Suppress("deprecation")
class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DynamicColors.applyToActivityIfAvailable(this)

        enableEdgeToEdge()

        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val barInsets = insets.getInsets(WindowInsets.Type.systemBars())
                binding.root.setPadding(
                    barInsets.left,
                    barInsets.top,
                    barInsets.right,
                    0,
                )

                binding.bottomPadding.minimumHeight = barInsets.bottom
            } else {
                @Suppress("deprecation")
                binding.root.setPadding(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    0,
                )

                binding.bottomPadding.minimumHeight = insets.systemWindowInsetBottom
            }

            insets
        }

        val tint = ColorStateList.valueOf(homeItemBackgroundColor())

        with(binding.aboutHeader) {
            with(backButton.parent as View) {
                setOnClickListener { finish() }
                backgroundTintList = tint
            }

            appIcon.setImageResource(R.mipmap.ic_launcher_round)
            appName.setText(R.string.app_name)
            appVersion.text = BuildConfig.APP_VERSION_NAME

            setOnClickUrl(linkGithub, "https://github.com/frknkrc44/HMA-OSS")
            setOnClickUrl(linkTelegram, "https://t.me/aerathfuns")

            appInfoTop.backgroundTintList = tint
            (appName.parent as View).backgroundTintList = tint
        }

        with(binding.aboutDescription) {
            contentTitle.setText(R.string.title_about)
            contentDescription.setText(R.string.about_description)
            contentDescription.backgroundTintList = tint
        }

        with(binding.aboutForkDescription) {
            contentTitle.setText(R.string.title_about_fork)
            contentDescription.setText(R.string.about_fork_description)
            contentDescription.backgroundTintList = tint
        }

        with(binding.listDeveloper) {
            backgroundTintList = tint
            clipToOutline = true

            // HMA-OSS devs
            addDevItem(this, R.drawable.cont_fk, "frknkrc44", "HMA-OSS Developer", "https://github.com/frknkrc44")

            // Original HMA devs
            addDevItem(this, R.drawable.cont_nullptr, "nullptr", "HMA Developer", "https://github.com/Dr-TSNG")
            addDevItem(this, R.drawable.cont_k, "Ketal", "HMA Collaborator", "https://github.com/keta1")
            addDevItem(this, R.drawable.cont_aviraxp, "aviraxp", "HMA Collaborator", "https://github.com/aviraxp")
            addDevItem(this, R.drawable.cont_icon_designer, "辉少菌", "HMA Icon Designer", "http://www.coolapk.com/u/1560270")
            addDevItem(this, R.drawable.cont_cpp_master,  "LoveSy", "HMA Idea Provider", "https://github.com/yujincheng08")
        }

        with(binding.listTranslator) {
            backgroundTintList = tint
            clipToOutline = true

            val jsonObj = JSONObject(String(assets.open("translators.json").readBytes()))
            for (name in jsonObj.keys()) {
                val avatarUrl = jsonObj.getString(name)
                addTranslatorItem(this, avatarUrl, name)
            }
        }
    }

    fun addDevItem(layout: LinearLayout, @DrawableRes avatarResId: Int, name: String, desc: String, url: String) {
        val newLayout = ActivityAboutListItemBinding.inflate(layoutInflater)
        setOnClickUrl(newLayout.root, url)

        newLayout.aboutPersonIcon.setImageResource(avatarResId)

        with(newLayout.aboutPersonIcon) {
            shapeAppearanceModel = shapeAppearanceModel
                .toBuilder()
                .setAllCornerSizes { 48.0f }
                .build()
        }

        newLayout.text1.text = name
        newLayout.text2.text = desc
        layout.addView(newLayout.root)
    }

    fun addTranslatorItem(layout: LinearLayout, avatarUrl: String, name: String) {
        val newLayout = ActivityAboutListItemBinding.inflate(layoutInflater)
        Glide.with(this)
            .load(avatarUrl)
            .placeholder(R.drawable.outline_info_24)
            .into(newLayout.aboutPersonIcon)

        with(newLayout.aboutPersonIcon) {
            shapeAppearanceModel = shapeAppearanceModel
                .toBuilder()
                .setAllCornerSizes { 48.0f }
                .build()
        }

        newLayout.text1.text = name
        newLayout.text2.visibility = View.GONE
        layout.addView(newLayout.root)
    }

    fun setOnClickUrl(view: View, url: String) {
        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(url.toUri())
            startActivity(intent)
        }
    }

    override fun onApplyThemeResource(theme: Resources.Theme, resid: Int, first: Boolean) {
        super.onApplyThemeResource(theme, resid, first)
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