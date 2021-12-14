package ipca.example.photomap.ui.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ipca.example.photomap.R
import ipca.example.photomap.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    var photos = arrayListOf<Photo>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var  listView : ListView
    private lateinit var adapter: PhotosAdapter

    val storage = Firebase.storage
    val storageRef = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_photoFragment)
        }
        adapter = PhotosAdapter()
        listView = binding.listViewPhotos
        listView.adapter = adapter

        val db = Firebase.firestore
        db.collection("photos")
            //.whereEqualTo("state", "CA")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    //Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                photos.clear()

                for (doc in value!!.documents) {

                    photos.add(Photo.fromHashMap(doc.data as HashMap<String, Comparable<Any>?>))
                }
                adapter.notifyDataSetChanged()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class PhotosAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return photos.size
        }

        override fun getItem(position: Int): Any {
            return photos[position]
        }

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            var rootView = layoutInflater.inflate(R.layout.row_photo, viewGroup, false)
            val textViewDescription = rootView.findViewById<TextView>(R.id.textViewPhotoDescription)
            val imageViewPhoto = rootView.findViewById<ImageView>(R.id.imageViewPhoto)

            textViewDescription.text = photos[position].description

            rootView.isClickable = true

            var islandRef = photos[position].photoPath?.let { storageRef.child(it) }

            val ONE_MEGABYTE: Long = 1024 * 1024
            islandRef?.getBytes(ONE_MEGABYTE)?.addOnSuccessListener {
                // Data for "images/island.jpg" is returned, use this as needed
                val bitmap = BitmapFactory.decodeByteArray(it,0, it.size)
                imageViewPhoto.setImageBitmap(bitmap)
            }?.addOnFailureListener {
                // Handle any errors
            }

            return rootView
        }
    }


}