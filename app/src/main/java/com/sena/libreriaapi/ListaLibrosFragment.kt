    package com.sena.libreriaapi

    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.EditText
    import android.widget.Toast
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.android.volley.toolbox.JsonArrayRequest
    import com.android.volley.Request
    import com.android.volley.toolbox.JsonObjectRequest
    import com.android.volley.toolbox.Volley
    import com.sena.libreriaapi.adapter.libroAdapter
    import com.sena.libreriaapi.config.urls
    import com.sena.libreriaapi.entity.book

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private const val ARG_PARAM1 = "param1"
    private const val ARG_PARAM2 = "param2"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: libroAdapter

    /**
     * A simple [Fragment] subclass.
     * Use the [ListaLibrosFragment.newInstance] factory method to
     * create an instance of this fragment.
     */
    class ListaLibrosFragment : Fragment() {
        private lateinit var recyclerView: RecyclerView
        private lateinit var adapter: libroAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflar el layout para este fragmento
            val view = inflater.inflate(R.layout.fragment_lista_libros, container, false)

            // Inicializar el RecyclerView
            recyclerView = view.findViewById(R.id.listLibros)
            recyclerView.layoutManager = LinearLayoutManager(context)

            // Inicializar el adaptador con una lista vacía
            adapter = libroAdapter(mutableListOf(), { id ->
                eliminar_libro(id)
            }, { libro ->
                // Navegar al fragmento de guardar libro con los datos del libro a editar
                val fragment = guardarLibroFragment.newInstance(libro)
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            })

            recyclerView.adapter = adapter

            // Cargar la lista de libros
            cargar_lista_libros()

            return view
        }

        // El resto de tu código...



    companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment ListaLibrosFragment.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                ListaLibrosFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }

        fun cargar_lista_libros() {
            try {
                val request = JsonArrayRequest(
                    Request.Method.GET,
                    urls.urlLibro,
                    null,
                    { response ->
                        val libros = mutableListOf<book>()
                        for (i in 0 until response.length()) {
                            val item = response.getJSONObject(i)
                            val libro = book(
                                id = item.getInt("id"),
                                titulo = item.getString("titulo"),
                                autor = item.getString("autor"),
                                isbn = item.getString("isbn"),
                                genero = item.getString("genero"),
                                num_ejem_disponible = item.getInt("num_ejem_disponible"),
                                num_ejem_ocupados = item.getInt("num_ejem_ocupados")
                            )
                            libros.add(libro)
                        }
                        adapter = libroAdapter(libros, { id ->
                            eliminar_libro(id)
                        }, { libro ->
                            // Aquí se maneja la edición del libro
                            val fragment = guardarLibroFragment.newInstance(libro)
                            fragmentManager?.beginTransaction()
                                ?.replace(R.id.fragment_container, fragment)
                                ?.addToBackStack(null)
                                ?.commit()
                        })
                        recyclerView.adapter = adapter
                    },
                    { error ->
                        Toast.makeText(context, "Error al cargar la lista de libros", Toast.LENGTH_SHORT).show()
                    }
                )
                val queue = Volley.newRequestQueue(this.context)
                queue.add(request)
            } catch (error: Exception) {
                Toast.makeText(context, "Error al cargar la lista de libros", Toast.LENGTH_SHORT).show()
            }
        }

        fun eliminar_libro(id:Int){
            try {
                var request= JsonObjectRequest(
                    Request.Method.DELETE,//METODO
                    urls.urlLibro+id+"/",//URL del servicio web
                    null,//parametros
                    {response->
                        cargar_lista_libros()
                        Toast.makeText(this.context, "Eliminar."+id, Toast.LENGTH_SHORT).show()
                    },{error->//respuesta es incorrecta o no es la respuesta esperada
                        var error=error
                    }
                )
                val queue = Volley.newRequestQueue(this.context)
                queue.add(request)
            }catch (error : Exception){
                Toast.makeText(this.context, "Error al Eliminar: "+error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }