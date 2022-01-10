package ipca.example.photomap

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class Preferences(activity:Activity) {

    var sharedPref : SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

    var fireBaseToken : String?
        get() = sharedPref.getString("FIREBASE_TOKEN","")
    set(value) {
        with (sharedPref.edit()) {
            putString("FIREBASE_TOKEN", value)
            commit()
        }
    }





}