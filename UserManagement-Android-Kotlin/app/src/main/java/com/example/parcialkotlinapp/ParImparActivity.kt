package com.example.parcialkotlinapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ParImparActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_par_impar)

        // Conectamos Kotlin con los componentes del XML
        val etNumero = findViewById<EditText>(R.id.etNumero)
        val btnValidar = findViewById<Button>(R.id.btnValidar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val btnAtras = findViewById<ImageButton>(R.id.btnAtras)

        // Evento del botón
        btnValidar.setOnClickListener {

            val texto = etNumero.text.toString()

            // Validar que el campo no esté vacío
            if (texto.isEmpty()) {
                etNumero.error = getString(R.string.required_number)
                return@setOnClickListener
            }

            // Conversión segura
            val numero = texto.toIntOrNull()

            if (numero == null) {
                etNumero.error = getString(R.string.invalid_number)
                return@setOnClickListener
            }

            // Validación par o impar
            if (numero % 2 == 0) {
                tvResultado.text = getString(R.string.result_even, numero)
            } else {
                tvResultado.text = getString(R.string.result_odd, numero)
            }
        }

        // =====================================
        // REGRESAR A LA PANTALLA PRINCIPAL
        // =====================================

        btnAtras.setOnClickListener {
            finish()
        }
    }
}