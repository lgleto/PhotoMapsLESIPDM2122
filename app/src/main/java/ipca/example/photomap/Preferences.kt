package ipca.example.photomap

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class Preferences {

    lateinit var sharedPref : SharedPreferences

    constructor(activity:Activity){
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    }

    var fireBaseToken : String
        get() {
            return sharedPref.getString("FIREBASE_TOKEN","")!!
        }
    set(value) {
        with (sharedPref.edit()) {
            putString("FIREBASE_TOKEN", value)
            commit()
        }
    }





}