package ipca.example.photomap.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ipca.example.photomap.databinding.FragmentPhotoBinding
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*


class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!


    private var param1: String? = null
    private var param2: String? = null

    lateinit var photoViewModel : PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    val REQUEST_IMAGE_CAPTURE = 1001
    var bitmap : Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            if (bitmap == null) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }
        },500L)


        photoViewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)

        photoViewModel.getBitmap().observe(viewLifecycleOwner, Observer {
            binding.imageViewPhoto.setImageBitmap(it)
            bitmap = it
            binding.fabSave.visibility = View.VISIBLE
        })

        binding.fabTakePhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }

        binding.fabSave.visibility = View.GONE

        binding.fabSave.setOnClickListener {
            // TODO save photo
            val storage = Firebase.storage
            val storageRef = storage.reference
            val filename = UUID.randomUUID().toString() + ".jpg"
            val photoRef = storageRef.child("photo/${filename}")

            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()

            var uploadTask = photoRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Toast.makeText(requireContext(),
                    "Não foi possivel guardar a imagem",
                    Toast.LENGTH_LONG).show()
            }.addOnSuccessListener { taskSnapshot ->

                // Add a new document with a generated id.
                val data = hashMapOf(
                    "photo_path" to photoRef.path,
                    "user" to FirebaseAuth.getInstance().currentUser?.uid,
                    "description" to binding.editTextDescription.text.toString(),
                    "date" to Timestamp(Date())
                )
                val db = Firebase.firestore
                db.collection("photos")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        //Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        findNavController().popBackStack()
                    }
                    .addOnFailureListener { e ->
                        //Log.w(TAG, "Error adding document", e)
                        Toast.makeText(requireContext(),
                            "Não foi possivel guardar a imagem",
                            Toast.LENGTH_LONG).show()
                    }


            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == Activity.RESULT_OK){
                val imageBitmap = data?.extras?.get("data") as Bitmap
                photoViewModel.setBitmap(imageBitmap)
            }
        }
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}