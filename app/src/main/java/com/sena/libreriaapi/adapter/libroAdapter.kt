package com.sena.libreriaapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sena.libreriaapi.R
import com.sena.libreriaapi.config.urls
import com.sena.libreriaapi.entity.book

class libroAdapter(
    private var libros: List<book>,
    private val onEliminarClick: (Int) -> Unit,
    private val onEditarClick: (book) -> Unit
)

    : RecyclerView.Adapter<libroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(ViewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(ViewGroup.context).inflate(R.layout.item_lista, ViewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val libro = libros[i]
        viewHolder.itemTitle.text = libros[i].titulo
        viewHolder.itemAutor.text = libros[i].autor
        viewHolder.itemGenero.text = libros[i].genero
        viewHolder.btnEditar.setOnClickListener {
            onEditarClick(libro) // Llama al callback de edición
        }
        viewHolder.btnEliminar.setOnClickListener {
            onEliminarClick(libro.id)        }
    }

    override fun getItemCount(): Int {
        return libros.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView;
        var itemAutor: TextView;
        var itemGenero: TextView;
        var btnEditar: Button;
        var btnEliminar: Button;

        init {
            itemTitle = itemView.findViewById(R.id.lblTitulo);
            itemAutor = itemView.findViewById(R.id.lblAutor);
            itemGenero = itemView.findViewById(R.id.lblGenero)
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

}