package com.tecsup.bautista_semana5.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "producto",
    indices = [Index(value = ["codigoBarras"], unique = true)]
)
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val idProducto: Int = 0,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int,
    val codigoBarras: String,
    val descripcion: String? = null
)
