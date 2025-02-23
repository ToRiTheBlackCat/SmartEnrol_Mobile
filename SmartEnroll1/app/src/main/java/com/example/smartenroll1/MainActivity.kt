package com.example.smartenroll1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.smartenroll1.databinding.ActivityMainBinding
import com.example.smartenroll1.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstFragment = HomeFragment()
        val secondFragment = ChatFragment()
        val thirdFragment = AccountFragment()

        val test = supportFragmentManager
            .findFragmentById(R.id.nvfFragment) as NavHostFragment
        navController = test.findNavController()

        setCurrentFragment(R.id.homeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> setCurrentFragment(R.id.homeFragment)
                R.id.miChat -> setCurrentFragment(R.id.chatFragment)
                R.id.miAccount -> setCurrentFragment(R.id.accountFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(actionId: Int) {
        navController.navigate(actionId)
    }




//    private fun setCurrentFragment(fragment: Fragment){
////        supportFragmentManager.beginTransaction().apply {
////            replace(R.id.nvfFragment, fragment)
////            commit()
////        }
//
//        navController.navigate(R.id.action_homeFragment_to_accountFragment)
//    }
}