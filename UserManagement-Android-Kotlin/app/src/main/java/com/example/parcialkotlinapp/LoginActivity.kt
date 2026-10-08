package com.example.parcialkotlinapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.parcialkotlinapp.api.ApiClient
import com.example.parcialkotlinapp.api.mensajeError
import com.example.parcialkotlinapp.models.LoginRequest
import com.example.parcialkotlinapp.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var loginCall: Call<Usuario>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val correo = findViewById<EditText>(R.id.etCorreo)
        val password = findViewById<EditText>(R.id.etPassword)
        val ingresar = findViewById<Button>(R.id.btnIngresar)
        val crearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val mensaje = findViewById<TextView>(R.id.tvMensaje)

        ingresar.setOnClickListener {
            val correoIngresado = correo.text.toString().trim()
            val passwordIngresado = password.text.toString()
            if (correoIngresado.isBlank()) {
                correo.error = getString(R.string.required_email)
                return@setOnClickListener
            }
            if (passwordIngresado.isBlank()) {
                password.error = getString(R.string.required_password)
                return@setOnClickListener
            }

            ingresar.isEnabled = false
            crearCuenta.isEnabled = false
            mensaje.setText(R.string.logging_in)
            loginCall = ApiClient.usuarioApi.login(LoginRequest(correoIngresado, passwordIngresado))
            loginCall?.enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (isFinishing || isDestroyed) return
                    ingresar.isEnabled = true
                    crearCuenta.isEnabled = true
                    val usuario = response.body()
                    if (response.isSuccessful && usuario != null && !usuario.nombre.isNullOrBlank()) {
                        password.text.clear()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                            putExtra("nombreUsuario", usuario.nombre)
                            putExtra("correoUsuario", usuario.correo)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                        finish()
                    } else {
                        mensaje.text = if (response.isSuccessful) {
                            getString(R.string.invalid_login_response)
                        } else response.mensajeError(this@LoginActivity)
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    if (call.isCanceled || isFinishing || isDestroyed) return
                    ingresar.isEnabled = true
                    crearCuenta.isEnabled = true
                    mensaje.setText(R.string.connection_error)
                }
            })
        }

        crearCuenta.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    override fun onDestroy() {
        loginCall?.cancel()
        super.onDestroy()
    }
}
