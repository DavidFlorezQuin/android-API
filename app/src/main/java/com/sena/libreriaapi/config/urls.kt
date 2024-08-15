package com.sena.libreriaapi.config

class urls {
    /*
    va a contener los end-points de la api
    la url basica
    se crea estos variables static
     */
    companion object{
        val urlBasic="http://192.168.1.10:8000/libreria/api/v1/"
        val urlLibro= urlBasic+"libro/"
        //crer variables por cada modelo
    }
}