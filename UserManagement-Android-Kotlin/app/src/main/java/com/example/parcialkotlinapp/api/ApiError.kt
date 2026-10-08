package com.example.parcialkotlinapp.api

import android.content.Context
import com.example.parcialkotlinapp.R
import org.json.JSONObject
import retrofit2.Response

fun Response<*>.mensajeError(context: Context): String {
    val json = runCatching { JSONObject(errorBody()?.string().orEmpty()) }.getOrNull()
    (json?.opt("mensaje") as? String)?.takeIf { it.isNotBlank() }?.let { return it }

    // ASP.NET Core devuelve los errores de validación de los DTO en "errors".
    val errors = json?.optJSONObject("errors")
    if (errors != null && code() == 400) {
        val mensajes = mutableListOf<String>()
        for (campo in errors.keys()) {
            val lista = errors.optJSONArray(campo) ?: continue
            for (i in 0 until lista.length()) {
                (lista.opt(i) as? String)?.takeIf { it.isNotBlank() }?.let { mensajes.add(it) }
            }
        }
        if (mensajes.isNotEmpty()) return mensajes.distinct().joinToString("\n")
    }

    val resource = when (code()) {
        400 -> R.string.error_bad_request
        401 -> R.string.error_unauthorized
        404 -> R.string.error_not_found
        409 -> R.string.error_conflict
        in 500..599 -> R.string.error_server
        else -> R.string.error_request
    }
    return context.getString(resource)
}
