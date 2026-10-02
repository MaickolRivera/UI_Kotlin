package com.example.parcialkotlinapp

import android.R.attr.password
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parcialkotlinapp.api.ApiClient
import com.example.parcialkotlinapp.models.CrearUsuarioRequest
import com.example.parcialkotlinapp.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registro)

        // COMPONENTES
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etCorreo = findViewById<EditText>(R.id.etCorreoRegistro)
        val etPasswordRegistro = findViewById<EditText>(R.id.etPasswordRegistro)

        val etEdad = findViewById<EditText>(R.id.etEdad)

        val spRol = findViewById<Spinner>(R.id.spRol)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val tvMensaje =
            findViewById<TextView>(R.id.tvMensajeRegistro)

        // ROLES
        val roles = arrayOf(
            "Seleccione un rol",
            "Administrador",
            "Usuario"
        )

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spRol.adapter = adaptador

        // BOTÓN REGISTRAR
        btnRegistrar.setOnClickListener {

            val nombre =
                etNombre.text.toString().trim()

            val apellido =
                etApellido.text.toString().trim()

            val correo =
                etCorreo.text.toString().trim()

            val password = etPasswordRegistro.text.toString().trim()

            val edadTexto =
                etEdad.text.toString().trim()

            // VALIDACIONES
            if (nombre.isBlank()) {
                etNombre.error = "Ingrese el nombre"
                return@setOnClickListener
            }

            if (apellido.isBlank()) {
                etApellido.error = "Ingrese el apellido"
                return@setOnClickListener
            }

            if (correo.isBlank()) {
                etCorreo.error = "Ingrese el correo"
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(correo)
                    .matches()) {

                etCorreo.error = "Correo no válido"
                return@setOnClickListener
            }
            if (password.isBlank()) {
                etPasswordRegistro.error = "Ingrese la contraseña"
                return@setOnClickListener
            }

            if (edadTexto.isBlank()) {
                etEdad.error = "Ingrese la edad"
                return@setOnClickListener
            }

            val edad = edadTexto.toIntOrNull()

            if (edad == null || edad < 1 || edad > 120) {
                etEdad.error = "Edad no válida"
                return@setOnClickListener
            }

            if (spRol.selectedItemPosition == 0) {

                Toast.makeText(
                    this,
                    "Seleccione un rol",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Para esta práctica:
            // Administrador = 1
            // Usuario = 2
            val rolId = spRol.selectedItemPosition

            // OBJETO QUE ENVIAREMOS AL BACKEND

            val nuevoUsuario = CrearUsuarioRequest(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                edad = edad,
                password = password,
                rolId = rolId
            )

            tvMensaje.text = "Registrando usuario..."

            // LLAMADA AL BACKEND
            ApiClient.usuarioApi
                .crearUsuario(nuevoUsuario)
                .enqueue(object : Callback<Usuario> {

                    override fun onResponse(
                        call: Call<Usuario>,
                        response: Response<Usuario>
                    ) {

                        if (response.isSuccessful) {

                            val usuarioCreado = response.body()

                            tvMensaje.text =
                                "Usuario registrado correctamente"

                            Toast.makeText(
                                this@RegistroActivity,
                                "Usuario creado. ID: ${usuarioCreado?.id}",
                                Toast.LENGTH_LONG
                            ).show()

                            // Limpiar formulario
                            etNombre.text.clear()
                            etApellido.text.clear()
                            etCorreo.text.clear()
                            etEdad.text.clear()

                            spRol.setSelection(0)

                        } else {

                            tvMensaje.text =
                                "Error del servidor: ${response.code()}"
                        }
                    }

                    override fun onFailure(
                        call: Call<Usuario>,
                        t: Throwable
                    ) {

                        tvMensaje.text =
                            "Error de conexión: ${t.message}"
                    }
                })
        }

        // VOLVER AL LOGIN
        btnVolver.setOnClickListener {
            finish()
        }
    }
}