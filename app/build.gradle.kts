import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.google.gson.JsonParser
import org.jose4j.json.internal.json_simple.JSONObject
import java.io.DataInputStream
import java.net.HttpURLConnection
import java.net.URL

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.autoresconfig)
    alias(libs.plugins.refine)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nav.safeargs.kotlin)
}

val appPackageName: String by rootProject.extra
val crowdinProjectId: String by rootProject.extra
val crowdinApiKey: String by rootProject.extra
val localBuild: Boolean by rootProject.extra
val officialBuild: Boolean by rootProject.extra

@Suppress("deprecation")
afterEvaluate {
    if (localBuild || officialBuild) {
        val url = URL("https://crowdin.com/api/v2/projects/$crowdinProjectId/members")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestProperty("authorization", "Bearer $crowdinApiKey")

        val inputStream = DataInputStream(urlConnection.getInputStream())
        val str = String(inputStream.readAllBytes())
        inputStream.close()
        urlConnection.disconnect()

        val json = JsonParser.parseString(str).asJsonObject
        val translators = json.getAsJsonArray("data")
        val translatorsMap = mutableMapOf<String, String>()
        for (item in translators) {
            val translator = item.asJsonObject.getAsJsonObject("data")
            val avatarUrl = translator.get("avatarUrl").asString
            val username = translator.get("username").asString
            val fullName = try {
                translator.get("fullName").asString
            } catch (_: Throwable) {
                ""
            }

            if (fullName.isNotEmpty() && fullName != username) {
                translatorsMap["$fullName ($username)"] = avatarUrl
            } else {
                translatorsMap[username] = avatarUrl
            }
        }

        logger.lifecycle("Translators: " + translatorsMap.keys.joinToString { it })

        val translatorJson = JSONObject(translatorsMap).toJSONString()
        File("${projectDir}/src/main/assets/translators.json").writeText(translatorJson)
    }
}

android {
    namespace = appPackageName

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    packaging {
        dex.useLegacyPackaging = true
        resources {
            excludes += arrayOf(
                "/META-INF/*",
                "/META-INF/androidx/**",
                "/kotlin/**",
                "/okhttp3/**",
            )
        }
    }
}

kotlin {
    jvmToolchain(21)
}

autoResConfig {
    generateClass.set(true)
    generateRes.set(false)
    generatedClassFullName.set("icu.nullptr.hidemyapplist.util.LangList")
    generatedArrayFirstItem.set("SYSTEM")
}

dependencies {
    implementation(projects.common)
    runtimeOnly(projects.xposed)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.com.drakeet.multitype)
    implementation(libs.com.github.bumptech.glide)
    implementation(libs.com.github.kirich1409.viewbindingpropertydelegate)
    implementation(libs.com.github.liujingxing.rxhttp)
    implementation(libs.com.github.liujingxing.rxhttp.converter.serialization)
    implementation(libs.com.github.topjohnwu.libsu.core)
    implementation(libs.com.squareup.okhttp3)
    implementation(libs.dev.rikka.hidden.compat)
    implementation(libs.me.zhanghai.android.appiconloader)
    compileOnly(libs.dev.rikka.hidden.stub)
    ksp(libs.com.github.liujingxing.rxhttp.compiler)

    implementation(libs.androidx.appcompat.appcompat)
    implementation(libs.material)
}

android.applicationVariants.all {
    outputs.all {
        (this as BaseVariantOutputImpl).apply {
            outputFileName = "${rootProject.name.replace(" ", "_")}-${versionName}-${buildType.name}.apk"
        }
    }
}
