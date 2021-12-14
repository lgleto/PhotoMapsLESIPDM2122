package ipca.example.photomap.ui.home

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.HashMap

class Photo {

    var photoPath :String? = null
    var userId :String? = null
    var description :String? = null
    var date :Timestamp? = null

    fun toHashMap() : HashMap<String, Comparable<Any>?> {
        val data  = hashMapOf(
            "photo_path" to photoPath,
            "user" to userId,
            "description" to description,
            "date" to date
        ) as HashMap<String, Comparable<Any>?>

        return data
    }

    companion object{

        fun fromHashMap(data:HashMap<String, Comparable<Any>?>):Photo{
            val photo = Photo()
            photo.photoPath = data["photo_path"] as String?
            photo.userId = data["user"] as String?
            photo.description = data["description"] as String?
            photo.date = data["date"] as Timestamp

            return photo
        }
    }



}