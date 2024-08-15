package com.sena.libreriaapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sena.libreriaapi.config.urls
import com.sena.libreriaapi.entity.book
import org.json.JSONObject

private const val ARG_BOOK = "book"

class guardarLibroFragment : Fragment() {
    private var libro: book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            libro = it.getParcelable(ARG_BOOK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guardar_libro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtTitulo = view.findViewById<EditText>(R.id.txtTitulo)
        val txtAutor = view.findViewById<EditText>(R.id.txtAutor)
        val txtIsbn = view.findViewById<EditText>(R.id.txtIsbn)
        val txtGenero = view.findViewById<EditText>(R.id.txtGenero)
        val txtEjemDisponible = view.findViewById<EditText>(R.id.txtEjemDisponible)
        val txtEjemOcupados = view.findViewById<EditText>(R.id.txtEjemOcupados)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        // Pre-cargar los datos del libro si se está editando
        libro?.let {
            txtTitulo.setText(it.titulo)
            txtAutor.setText(it.autor)
            txtIsbn.setText(it.isbn)
            txtGenero.setText(it.genero)
            txtEjemDisponible.setText(it.num_ejem_disponible.toString())
            txtEjemOcupados.setText(it.num_ejem_ocupados.toString())
            btnGuardar.text = "Actualizar Libro"
        }

        btnGuardar.setOnClickListener {
            val titulo = txtTitulo.text.toString()
            val autor = txtAutor.text.toString()
            val isbn = txtIsbn.text.toString()
            val genero = txtGenero.text.toString()
            val num_ejem_disponible = txtEjemDisponible.text.toString().toIntOrNull() ?: 0
            val num_ejem_ocupados = txtEjemOcupados.text.toString().toIntOrNull() ?: 0

            val libroActualizado = book(
                id = libro?.id ?: 0, // Usar el ID del libro si se está editando, de lo contrario 0
                titulo = titulo,
                autor = autor,
                isbn = isbn,
                genero = genero,
                num_ejem_disponible = num_ejem_disponible,
                num_ejem_ocupados = num_ejem_ocupados
            )

            if (libro == null) {
                guardarLibro(libroActualizado)
            } else {
                actualizarLibro(libroActualizado)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(libro: book?) =
            guardarLibroFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_BOOK, libro)
                }
            }
    }


    fun guardarLibro(libro: book) {
        try {
            val parametros = JSONObject().apply {
                put("titulo", libro.titulo)
                put("autor", libro.autor)
                put("isbn", libro.isbn)
                put("genero", libro.genero)
                put("num_ejem_disponible", libro.num_ejem_disponible)
                put("num_ejem_ocupados", libro.num_ejem_ocupados)
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                urls.urlLibro,
                parametros,
                { response ->

                    Toast.makeText(context, "Libro guardado correctamente", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack() // Regresar a la lista después de guardar
                },
                { error ->
                    Toast.makeText(context, "Error al guardar el libro", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(this.context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun actualizarLibro(libro: book) {
        try {
            val parametros = JSONObject().apply {
                put("titulo", libro.titulo)
                put("autor", libro.autor)
                put("isbn", libro.isbn)
                put("genero", libro.genero)
                put("num_ejem_disponible", libro.num_ejem_disponible)
                put("num_ejem_ocupados", libro.num_ejem_ocupados)
            }

            val request = JsonObjectRequest(
                Request.Method.PUT,
                "${urls.urlLibro}${libro.id}/",
                parametros,
                { response ->
                    Toast.makeText(context, "Libro actualizado correctamente", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack() // Regresar a la lista después de actualizar
                },
                { error ->
                    Toast.makeText(context, "Error al actualizar el libro", Toast.LENGTH_SHORT).show()
                }
            )
            val queue = Volley.newRequestQueue(this.context)
            queue.add(request)
        } catch (error: Exception) {
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
