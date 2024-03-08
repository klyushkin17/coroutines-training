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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class MainActivity : AppCompatActivity() {

    val TAG = "Threadd"
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

        val job = GlobalScope.launch(Dispatchers.Default){
            Log.d(TAG, "Starting the process")
            repeat(5){
                Log.d(TAG, "The process is still running...")
                delay(1000L)
            }
        }

        runBlocking {
            job.join()
            Log.d(TAG, "The Main thread can be continued")
        }

        val job1 = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting the process")
            for (i in 30..40)
            {
                if(isActive) {
                    Log.d(TAG, "The fib of $i is ${fib(i)}")
                }
            }
        }

        runBlocking {
            delay(3000L)
            job1.cancel()
            Log.d(TAG, "The fib calculation has been canceled")
        }

        GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting the process")
            withTimeout(3000L)
            {
                for (i in 30..40) {
                    if(isActive) {
                            Log.d(TAG, "The fib of $i is ${fib(i)}")
                    }
                }
            }
        }

        Log.d(TAG, "The fib calculation has been canceled")
    }

    fun fib (n: Int) : Long
    {
        if (n == 0) return 0
        else if (n == 1) return 1
        else return fib(n - 1) + fib(n - 2)
    }
}