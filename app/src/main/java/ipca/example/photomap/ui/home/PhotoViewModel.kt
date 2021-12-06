package ipca.example.photomap.ui.home

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewModel : ViewModel() {

    private val bitmapData: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>()

    fun setBitmap(bitmap: Bitmap){
        bitmapData.postValue(bitmap)
    }

    fun getBitmap() : LiveData<Bitmap>{
        return bitmapData
    }

}