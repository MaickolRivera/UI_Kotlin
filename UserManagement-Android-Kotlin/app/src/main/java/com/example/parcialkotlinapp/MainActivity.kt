package com.example.parcialkotlinapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        // Manejo de los bordes de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // =====================================
        // BOTÓN EJERCICIO PAR O IMPAR
        // =====================================

        val btnParImpar =
            findViewById<Button>(R.id.btnParImpar)

        btnParImpar.setOnClickListener {

            val intent = Intent(
                this,
                ParImparActivity::class.java
            )

            startActivity(intent)
        }

        // =====================================
        // VOLVER AL LOGIN
        // =====================================

        val btnVolver2 =
            findViewById<Button>(R.id.btnVolver2)

        btnVolver2.setOnClickListener {

            val intent = Intent(
                this,
                LoginActivity::class.java
            )

            startActivity(intent)
        }

    }
}