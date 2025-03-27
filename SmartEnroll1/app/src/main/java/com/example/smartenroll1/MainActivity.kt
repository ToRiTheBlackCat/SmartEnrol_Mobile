package com.example.smartenroll1

import android.content.pm.PackageManager
import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.smartenroll1.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()
        subScribeToTopic()

        binding = ActivityMainBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nvfFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setCurrentFragment(R.id.infoFragmentNav)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
//                R.id.miHome -> setCurrentFragment(R.id.homeFragment)
                R.id.miHome -> setCurrentFragment(R.id.accountManagementNav)
//                R.id.miChat -> setCurrentFragment(R.id.chatFragment)
                R.id.miDash -> setCurrentFragment(R.id.infoFragmentNav)
                R.id.miGraph -> navController.navigate(R.id.chartFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(actionId: Int) {
        navController.navigate(actionId)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermit = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermit) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun subScribeToTopic() {
        Firebase.messaging.subscribeToTopic("Registration")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.i("SUBSCRIBE", msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
    }
}

