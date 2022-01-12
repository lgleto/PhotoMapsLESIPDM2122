package ipca.example.photomap.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ipca.example.photomap.MainActivity
import ipca.example.photomap.Preferences
import ipca.example.photomap.databinding.FragmentProfileBinding
import ipca.example.photomap.models.User
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val storage = Firebase.storage
    val storageRef = storage.reference
    val db = Firebase.firestore

    val REQUEST_IMAGE_CAPTURE = 1001
    var bitmap : Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()

        binding.imageViewUserPhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }

        binding.buttonSave.setOnClickListener {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val filename = UUID.randomUUID().toString() + ".jpg"
            val photoRef = storageRef.child("users_photos/${filename}")

            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()

            var uploadTask = photoRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Toast.makeText(requireContext(),
                    "Não foi possivel guardar a imagem",
                    Toast.LENGTH_LONG).show()
            }.addOnSuccessListener { taskSnapshot ->
                FirebaseAuth.getInstance().currentUser?.let { fbUser ->
                    var user = User()
                    user.photo = filename
                    user.name = binding.editTextName.text.toString()
                    val db = Firebase.firestore
                    db.collection("users")
                        .document(fbUser.uid)
                        .update(user.toHashMap() as Map<String, Any>)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(requireContext(),
                                "Settings guardadas com sucesso",
                                Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(),
                                "Falha ao guardar Settings",
                                Toast.LENGTH_LONG).show()
                        }
                }


            }





        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == Activity.RESULT_OK){
                bitmap = data?.extras?.get("data") as Bitmap
                binding.imageViewUserPhoto.setImageBitmap(bitmap)
            }
        }
    }

    private fun updateUI(){
        FirebaseAuth.getInstance().currentUser?.let {

            binding.editTextEmail.setText(it.email)

            db.collection("users").document(it.uid)
                .get().addOnSuccessListener {
                    val user = User.fromHashMap(it.data as HashMap<String, Comparable<Any>?>)
                    binding.editTextName.setText(user.name?:"")

                    var userPhotoRef = user.photo?.let {
                        storageRef.child("users_photos/${user.photo}")
                    }

                    val ONE_MEGABYTE: Long = 1024 * 1024
                    userPhotoRef?.getBytes(ONE_MEGABYTE)?.addOnSuccessListener {
                        // Data for "images/island.jpg" is returned, use this as needed
                        val bitmap = BitmapFactory.decodeByteArray(it,0, it.size)
                        binding.imageViewUserPhoto.setImageBitmap(bitmap)
                    }?.addOnFailureListener {
                        // Handle any errors
                    }


                }
        }
    }


}