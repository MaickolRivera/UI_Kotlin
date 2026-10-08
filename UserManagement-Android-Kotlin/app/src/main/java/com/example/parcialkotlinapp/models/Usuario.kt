package com.example.parcialkotlinapp.models

data class Usuario(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val fechaNacimiento: String,
    val universidad: String,
    val semestre: Int,
    val activo: Boolean,
    val fechaCreacion: String,
    val edad: String = ""
)
