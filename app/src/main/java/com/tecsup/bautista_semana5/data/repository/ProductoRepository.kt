package com.tecsup.bautista_semana5.data.repository

import com.tecsup.bautista_semana5.data.local.Producto
import com.tecsup.bautista_semana5.data.local.ProductoDao

class ProductoRepository(
    private val productoDao: ProductoDao
) {

    suspend fun listarProductos(query: String): List<Producto> {
        return if (query.isBlank()) {
            productoDao.getAll()
        } else {
            productoDao.search(query)
        }
    }

    suspend fun insertar(producto: Producto) {
        productoDao.insert(producto)
    }

    suspend fun actualizar(producto: Producto) {
        productoDao.update(producto)
    }

    suspend fun eliminar(producto: Producto) {
        productoDao.delete(producto)
    }

    suspend fun obtenerPorCodigo(codigo: String): Producto? {
        return productoDao.getByCodigoBarras(codigo)
    }
}
