package com.example.aes1

import android.R.attr.data
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.OutputStreamWriter
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import android.Manifest
import android.os.Environment
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val STORAGE_PERMISSION_REQUEST_CODE = 1
        val TAG = "test"
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted
            // Continue with your code here
        }
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // Continue with your code here
            } else {
                // Permission denied
                // Handle the case where the user denied the permission
                Toast.makeText(
                    this,
                    "Storage permission denied. Cannot write to external storage.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        fun writeEncryptedTextToFile(context: Context, encryptedText: String, filename: String) {
            try {


                val fileToWrite = File("/storage/emulated/0/","output.txt").writeText(encryptedText)
            } catch (e: IOException) {
                Log.e("Exception", "File write failed: $e")
            }
        }

        fun encryptString(key: String, plaintext: String): String {
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")

            val keygen = KeyGenerator.getInstance("AES")
            keygen.init(256)
            val gkey: SecretKey = keygen.generateKey()

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, gkey)
            val encryptedBytes = cipher.doFinal(plaintext.toByteArray())
            val encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
            return encryptedText
        }

        fun main(text: String, key: String, s: String) {



            val encryptedText = encryptString(key, text)

            val applicationContext: Context =
                applicationContext // Obtain the application context in an Android app
            val filename = "encrypted_text.txt"
            writeEncryptedTextToFile(applicationContext, encryptedText, filename)
            Toast.makeText(applicationContext, "Encrypted text written to file: $filename", Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, encryptedText, Toast.LENGTH_SHORT).show()
        }

        var button : Button = findViewById(R.id.button)
        button.setOnClickListener()
        {
            val text = findViewById<EditText>(R.id.myEditText).toString()
            val key = findViewById<EditText>(R.id.myEditTextKey).toString()
            main(text, key, "encrypted_data.txt")
        }



    }








}