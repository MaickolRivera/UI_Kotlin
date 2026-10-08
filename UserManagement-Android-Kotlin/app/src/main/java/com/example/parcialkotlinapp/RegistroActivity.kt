package com.example.parcialkotlinapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.parcialkotlinapp.api.ApiClient
import com.example.parcialkotlinapp.api.mensajeError
import com.example.parcialkotlinapp.models.CrearUsuarioRequest
import com.example.parcialkotlinapp.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale

class RegistroActivity : AppCompatActivity() {
    private var nacimiento: Calendar? = null
    private var registrado = false
    private var registroCall: Call<Usuario>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val nombre = findViewById<EditText>(R.id.etNombre)
        val apellido = findViewById<EditText>(R.id.etApellido)
        val correo = findViewById<EditText>(R.id.etCorreoRegistro)
        val password = findViewById<EditText>(R.id.etPasswordRegistro)
        val fecha = findViewById<Button>(R.id.btnFechaNacimiento)
        val edad = findViewById<TextView>(R.id.tvEdadCalculada)
        val universidad = findViewById<EditText>(R.id.etUniversidad)
        val semestre = findViewById<Spinner>(R.id.spSemestre)
        val registrar = findViewById<Button>(R.id.btnRegistrar)
        val mensaje = findViewById<TextView>(R.id.tvMensajeRegistro)
        findViewById<ImageButton>(R.id.btnAtras).setOnClickListener { finish() }
        semestre.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            listOf(getString(R.string.select_semester)) + (1..10).map { it.toString() }).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        fun mostrarFecha() {
            nacimiento?.let {
                fecha.text = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                    it.get(Calendar.DAY_OF_MONTH), it.get(Calendar.MONTH) + 1, it.get(Calendar.YEAR))
                edad.text = calcularEdad(it)
                edad.visibility = View.VISIBLE
            }
        }
        if (savedInstanceState?.containsKey("nacimiento") == true) {
            nacimiento = Calendar.getInstance().apply { timeInMillis = savedInstanceState.getLong("nacimiento") }
            mostrarFecha()
        }
        registrado = savedInstanceState?.getBoolean("registrado") ?: false
        if (registrado) {
            registrar.isEnabled = false
            mostrarRegistroExitoso()
        }
        fecha.setOnClickListener {
            val inicial = nacimiento ?: Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                nacimiento = Calendar.getInstance().apply { clear(); set(year, month, day) }
                mostrarFecha()
            }, inicial.get(Calendar.YEAR), inicial.get(Calendar.MONTH), inicial.get(Calendar.DAY_OF_MONTH)).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }.show()
        }
        registrar.setOnClickListener {
            for ((campo, error) in listOf(nombre to getString(R.string.required_name), apellido to getString(R.string.required_surname),
                correo to getString(R.string.required_email), universidad to getString(R.string.required_university))) {
                if (campo.text.toString().isBlank()) {
                    campo.error = error
                    campo.requestFocus()
                    return@setOnClickListener
                }
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(correo.text.toString().trim()).matches()) {
                correo.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }
            if (password.text.length < 6 || password.text.toString().isBlank()) {
                password.error = getString(R.string.password_min_length)
                return@setOnClickListener
            }
            val seleccion = nacimiento
            if (seleccion == null || seleccion.after(Calendar.getInstance())) {
                mensaje.text = getString(R.string.required_birth_date)
                return@setOnClickListener
            }
            if (semestre.selectedItemPosition !in 1..10) {
                mensaje.text = getString(R.string.required_semester)
                return@setOnClickListener
            }
            val fechaApi = String.format(Locale.US, "%04d-%02d-%02d",
                seleccion.get(Calendar.YEAR), seleccion.get(Calendar.MONTH) + 1, seleccion.get(Calendar.DAY_OF_MONTH))
            val request = CrearUsuarioRequest(
                nombre = nombre.text.toString().trim(),
                apellido = apellido.text.toString().trim(),
                correo = correo.text.toString().trim(),
                password = password.text.toString(),
                fechaNacimiento = fechaApi,
                universidad = universidad.text.toString().trim(),
                semestre = semestre.selectedItemPosition,
                edad = edad.text.toString()
            )
            registrar.isEnabled = false
            mensaje.text = getString(R.string.registering)
            registroCall = ApiClient.usuarioApi.crearUsuario(request)
            registroCall?.enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (isFinishing || isDestroyed) return
                    if (response.isSuccessful) {
                        registrado = true
                        mensaje.text = getString(R.string.registration_success)
                        mostrarRegistroExitoso()
                    } else {
                        registrar.isEnabled = true
                        mensaje.text = response.mensajeError(this@RegistroActivity)
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    if (call.isCanceled || isFinishing || isDestroyed) return
                    registrar.isEnabled = true
                    mensaje.text = getString(R.string.connection_error)
                }
            })
        }
    }

    private fun calcularEdad(fechaNacimiento: Calendar): String {
        val hoy = Calendar.getInstance()
        // El DatePicker proporciona solo la fecha: la hora de referencia es 00:00.
        val nacimiento = (fechaNacimiento.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        var anios = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
        var cursor = (nacimiento.clone() as Calendar).apply { add(Calendar.YEAR, anios) }
        if (cursor.after(hoy)) {
            anios--
            cursor = (nacimiento.clone() as Calendar).apply { add(Calendar.YEAR, anios) }
        }

        // Avanza por meses naturales para respetar su duración y los años bisiestos.
        val aniversario = cursor.clone() as Calendar
        var meses = 0
        for (cantidad in 1..11) {
            val candidato = (aniversario.clone() as Calendar).apply { add(Calendar.MONTH, cantidad) }
            if (candidato.after(hoy)) break
            meses = cantidad
            cursor = candidato
        }

        var dias = 0
        while (true) {
            val siguienteDia = (cursor.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, 1) }
            if (siguienteDia.after(hoy)) break
            dias++
            cursor = siguienteDia
        }
        val horas = (hoy.timeInMillis - cursor.timeInMillis) / (60 * 60 * 1000)
        return getString(R.string.calculated_age, anios, meses, dias, horas)
    }

    private fun mostrarRegistroExitoso() {
        AlertDialog.Builder(this).setTitle(getString(R.string.registration_success))
            .setMessage(getString(R.string.registration_success_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.accept)) { _, _ ->
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }.show()
    }

    override fun onDestroy() {
        registroCall?.cancel()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        nacimiento?.let { outState.putLong("nacimiento", it.timeInMillis) }
        outState.putBoolean("registrado", registrado)
        super.onSaveInstanceState(outState)
    }
}
