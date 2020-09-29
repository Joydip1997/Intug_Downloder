package com.androdude.intugdownloder.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.androdude.intugdownloder.R
import com.androdude.intugdownloder.ViewModel.PostViewModel
import com.androdude.intugdownloder.api.PostApiService
import com.androdude.intugdownloder.db.model.PostRespose
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var respose: LiveData<Response<PostRespose>>
    private lateinit var postViewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set to home screen on first time open
        if(savedInstanceState == null)
        {
            chip_menu.setItemSelected(R.id.home_menu)
            supportFragmentManager.beginTransaction().replace(R.id.main_activity_layout,HomeFragment()).commit()
        }



        chip_menu.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener{
            override fun onItemSelected(id: Int) {
                when(id)
                {
                    R.id.home_menu ->supportFragmentManager.beginTransaction().replace(R.id.main_activity_layout,HomeFragment()).commit()
                    R.id.global_menu ->supportFragmentManager.beginTransaction().replace(R.id.main_activity_layout,GalleryFragment()).commit()
                }
            }

        })







    }
}

