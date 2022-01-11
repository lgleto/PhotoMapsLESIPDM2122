package ipca.example.photomap.models

import com.google.firebase.Timestamp

class User {

    var photo :String? = null
    var email :String? = null
    var name :String? = null
    var last_login : Timestamp? = null
    var token : String? = null

    fun toHashMap() : HashMap<String, Comparable<Any>?> {
        val data  = hashMapOf(
            "photo"      to photo,
            "email"      to email,
            "name"       to name,
            "last_login" to last_login,
            "token"      to token
        ) as HashMap<String, Comparable<Any>?>

        return data
    }

    companion object{

        fun fromHashMap(data:HashMap<String, Comparable<Any>?>): User {
            val user = User()
            user.photo      = data["photo"     ] as String?
            user.email      = data["email"     ] as String?
            user.name       = data["name"      ] as String?
            user.last_login = data["last_login"] as Timestamp?
            user.token      = data["token"     ] as String?

            return user
        }
    }
}