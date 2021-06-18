package com.example.nasaapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nasaapp.databinding.ActivityMainBinding
import com.example.nasaapp.errorfragment.FragmentErrorBanner
import com.example.nasaapp.viewmodel.NasaInfoViewModel
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity(), LifecycleObserver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_main)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val viewModel = ViewModelProvider(this, Injection.viewModelFactory()).get(NasaInfoViewModel::class.java)
        lifecycle.addObserver(viewModel)

        //sharedPreference
        val sharedPreferences = this.getSharedPreferences("MyData", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()

        viewModel.isConnected = checkNetworkAvailablity()
        if(!viewModel.isConnected.get()) {
            val beginTransaction = supportFragmentManager.beginTransaction()
            beginTransaction.add(R.id.fragment_container, FragmentErrorBanner())
            beginTransaction.commit()

            viewModel.title.postValue(sharedPreferences.getString("NASA_TITLE", ""))
            viewModel.description.postValue(sharedPreferences.getString("NASA_DESC",""))
            val imageCache = sharedPreferences.getString("NASA_IMAGE", "")
            imageCache?.let {
                val b: ByteArray = Base64.decode(imageCache, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                viewModel.image.postValue(bitmap)
            }

        }

        val titleObserver = Observer<String> { title ->
            binding.title.text = title
            edit.putString("NASA_TITLE", title)
            edit.apply()
        }
        viewModel.title.observe(this, titleObserver)

        val descpritionObserver = Observer<String> { descp ->
            binding.idDescp.text = descp
            edit.putString("NASA_DESC", descp)
            edit.apply()

        }
        viewModel.description.observe(this, descpritionObserver)

        val imageObserver = Observer<Bitmap>{ bitmap ->
            binding.todayImage.setImageBitmap(bitmap)
            val byteArrayOutputStream = ByteArrayOutputStream()
            if(bitmap!=null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val bitmapToString: String =
                    Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
                edit.putString("NASA_IMAGE", bitmapToString)
                edit.apply()
            }
        }
        viewModel.image.observe(this, imageObserver)
    //}
    }

    private fun editPreferences(edit: SharedPreferences.Editor, title: String?) {
        edit.putString("NASA_TITLE", title)
        edit.apply()
    }

    private fun checkNetworkAvailablity() : ObservableBoolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return ObservableBoolean(isConnected)

    }
}