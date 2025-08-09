package shachar.afeka.course.volunteers.utilities

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object MetaData {
    fun getGoogleGeoAPIKey(context: Context): String? {
        var res: String? = null

        try {
            val ai: ApplicationInfo = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            val bundle = ai.metaData
            if (bundle != null) {
                val apiKey = bundle.getString("com.google.android.geo.API_KEY")
                res = apiKey
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw e
        }
        return res
    }
}