package com.example.parcialkotlinapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (intent.getStringExtra("nombreUsuario").isNullOrBlank()) {
            volverAlLogin()
            return
        }
        findViewById<Button>(R.id.btnParImpar).setOnClickListener {
            startActivity(Intent(this, ParImparActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnSalir).setOnClickListener {
            AlertDialog.Builder(this).setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_confirmation))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(getString(R.string.exit)) { _, _ -> volverAlLogin() }.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val nombre = intent.getStringExtra("nombreUsuario") ?: return
        findViewById<TextView>(R.id.tvSaludo).text = getGreeting(nombre)
    }

    private fun getGreeting(name: String): String {
        val resource = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> R.string.greeting_morning
            in 12..17 -> R.string.greeting_afternoon
            else -> R.string.greeting_evening
        }
        return getString(resource, name)
    }

    private fun volverAlLogin() {
        intent.removeExtra("nombreUsuario")
        intent.removeExtra("correoUsuario")
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}
