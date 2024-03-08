package com.example.coroutines_training

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        GlobalScope.launch {
            val networkAnswer1 = doNetworkCall1()
            val networkAnswer2 = doNetworkCall2()
            Log.d("Thread", networkAnswer1)
            Log.d("Thread", networkAnswer2)
        }

        GlobalScope.launch (newSingleThreadContext("FirstThread")){
            delay(1000L)
        }

        Log.d("ThreadUi", "The current thread is ${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("ThreadUi", "The current thread is ${Thread.currentThread().name}")
            val answer = doNetworkCall1()
            withContext(Dispatchers.Main){
                Log.d("ThreadUi", "The current thread is ${Thread.currentThread().name}")
                findViewById<TextView>(R.id.dummy).text = answer
            }
        }
    }

    suspend fun doNetworkCall1(): String{
        delay(5000L)
        return "Network call ended"
    }

    suspend fun doNetworkCall2(): String{
        delay(5000L)
        return "Network call ended"
    }
}