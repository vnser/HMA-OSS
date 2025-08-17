package icu.nullptr.hidemyapplist.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialElevationScale
import icu.nullptr.hidemyapplist.hmaApp
import icu.nullptr.hidemyapplist.service.ConfigManager
import icu.nullptr.hidemyapplist.service.ServiceClient
import icu.nullptr.hidemyapplist.ui.activity.AboutActivity
import icu.nullptr.hidemyapplist.ui.util.ThemeUtils.getColor
import icu.nullptr.hidemyapplist.ui.util.ThemeUtils.themeColor
import icu.nullptr.hidemyapplist.ui.util.makeToast
import icu.nullptr.hidemyapplist.ui.util.navController
import icu.nullptr.hidemyapplist.ui.util.setupToolbar
import org.frknkrc44.hma_oss.BuildConfig
import org.frknkrc44.hma_oss.R
import org.frknkrc44.hma_oss.databinding.FragmentHomeBinding
import java.io.IOException

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding<FragmentHomeBinding>()

    private val backupSAFLauncher =
        registerForActivityResult(CreateDocument("application/json")) backup@{ uri ->
            if (uri == null) return@backup
            ConfigManager.configFile.inputStream().use { input ->
                hmaApp.contentResolver.openOutputStream(uri).use { output ->
                    if (output == null) makeToast(R.string.home_export_failed)
                    else input.copyTo(output)
                }
            }
            makeToast(R.string.home_exported)
        }

    private val restoreSAFLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) restore@{ uri ->
            if (uri == null) return@restore
            runCatching {
                val backup = hmaApp.contentResolver
                    .openInputStream(uri)?.reader().use { it?.readText() }
                    ?: throw IOException(getString(R.string.home_import_file_damaged))
                ConfigManager.importConfig(backup)
                makeToast(R.string.home_import_successful)
            }.onFailure {
                it.printStackTrace()
                MaterialAlertDialogBuilder(requireContext())
                    .setCancelable(false)
                    .setTitle(R.string.home_import_failed)
                    .setMessage(it.message)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(R.string.show_crash_log) { _, _ ->
                        MaterialAlertDialogBuilder(requireActivity())
                            .setCancelable(false)
                            .setTitle(R.string.home_import_failed)
                            .setMessage(it.stackTraceToString())
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                    }
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(
            toolbar = binding.toolbar,
            title = getString(R.string.app_name),
            menuRes = R.menu.menu_about,
            onMenuOptionSelected = {
                startActivity(Intent(requireContext(), AboutActivity::class.java))
            }
        )

        binding.templateManage.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.manageCard to "transition_manage")
            navController.navigate(R.id.nav_template_manage, null, null, extras)
        }
        binding.appManage.setOnClickListener {
            navController.navigate(R.id.nav_app_manage)
        }
        binding.howToUse.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.about_how_to_use_title)
                .setMessage(
                    getString(R.string.about_how_to_use_description_1) +
                            "\n\n" +
                            getString(R.string.about_how_to_use_description_2))
                .setNegativeButton(android.R.string.ok, null)
                .show()
        }
        binding.backupConfig.setOnClickListener {
            backupSAFLauncher.launch("HMA_Config_${Calendar.getInstance().timeInMillis}.json")
        }
        binding.restoreConfig.setOnClickListener {
            restoreSAFLauncher.launch("application/json")
        }

        /*
        lifecycleScope.launch {
            loadUpdateDialog()
        }
         */
    }

    @SuppressLint("StringFormatInvalid")
    override fun onStart() {
        super.onStart()
        val serviceVersion = ServiceClient.serviceVersion
        val color = when {
            !hmaApp.isHooked -> getColor(R.color.gray)
            serviceVersion == 0 -> getColor(R.color.invalid)
            else -> themeColor(android.R.attr.colorPrimary)
        }
        binding.statusCard.setCardBackgroundColor(color)
        binding.statusCard.outlineAmbientShadowColor = color
        binding.statusCard.outlineSpotShadowColor = color
        if (hmaApp.isHooked) {
            binding.moduleStatusIcon.setImageResource(R.drawable.outline_done_all_24)
            val versionNameSimple = BuildConfig.VERSION_NAME.substringBefore(".r")
            binding.moduleStatus.text = getString(R.string.home_xposed_activated, versionNameSimple, BuildConfig.VERSION_CODE)
        } else {
            binding.moduleStatusIcon.setImageResource(R.drawable.outline_extension_off_24)
            binding.moduleStatus.setText(R.string.home_xposed_not_activated)
        }
        if (serviceVersion != 0) {
            if (serviceVersion < org.frknkrc44.hma_oss.common.BuildConfig.SERVICE_VERSION) {
                binding.serviceStatus.text = getString(R.string.home_xposed_service_old)
            } else {
                binding.serviceStatus.text = getString(R.string.home_xposed_service_on, serviceVersion)
            }
            binding.filterCount.visibility = View.VISIBLE
            binding.filterCount.text = getString(R.string.home_xposed_filter_count, ServiceClient.filterCount)
        } else {
            binding.serviceStatus.setText(R.string.home_xposed_service_off)
            binding.filterCount.visibility = View.GONE
        }
    }

    /*
    private suspend fun loadUpdateDialog() {
        if (PrefManager.disableUpdate) return
        val updateInfo = fetchLatestUpdate() ?: return
        if (updateInfo.versionCode > BuildConfig.VERSION_CODE) {
            withContext(Dispatchers.Main) {
                MaterialAlertDialogBuilder(requireContext())
                    .setCancelable(false)
                    .setTitle(getString(R.string.home_new_update, updateInfo.versionName))
                    .setMessage(Html.fromHtml(updateInfo.content, Html.FROM_HTML_MODE_COMPACT))
                    .setPositiveButton("GitHub") { _, _ ->
                        startActivity(Intent(Intent.ACTION_VIEW, updateInfo.downloadUrl.toUri()))
                    }
                    .setNegativeButton("Telegram") { _, _ ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/HideMyApplist")))
                    }
                    .setNeutralButton(android.R.string.cancel, null)
                    .show()
            }
        } else if (updateInfo.versionCode > PrefManager.lastVersion) {
            withContext(Dispatchers.Main) {
                MaterialAlertDialogBuilder(requireContext())
                    .setCancelable(false)
                    .setTitle(getString(R.string.home_update, updateInfo.versionName))
                    .setMessage(Html.fromHtml(updateInfo.content, Html.FROM_HTML_MODE_COMPACT))
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        PrefManager.lastVersion = BuildConfig.VERSION_CODE
                    }
                    .show()
            }
        }
    }
     */
}
