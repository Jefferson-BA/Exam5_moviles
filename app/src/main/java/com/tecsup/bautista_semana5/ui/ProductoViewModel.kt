package com.tecsup.bautista_semana5.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.bautista_semana5.data.local.Producto
import com.tecsup.bautista_semana5.data.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    // listado
    var productos by mutableStateOf<List<Producto>>(emptyList())
        private set

    // formulario
    var nombre by mutableStateOf("")
        private set
    var categoria by mutableStateOf("")
        private set
    var precio by mutableStateOf("")
        private set
    var stock by mutableStateOf("")
        private set
    var codigoBarras by mutableStateOf("")
        private set
    var descripcion by mutableStateOf("")
        private set

    // Búsqueda
    var searchQuery by mutableStateOf("")
        private set

    //  edición
    var isEditing by mutableStateOf(false)
        private set
    private var editingId: Int? = null


    var mensaje by mutableStateOf<String?>(null)
        private set

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            productos = repository.listarProductos(searchQuery)
        }
    }

    fun onSearchChange(value: String) {
        searchQuery = value
        cargarProductos()
    }

    // cambios
    fun onNombreChange(value: String) { nombre = value }
    fun onCategoriaChange(value: String) { categoria = value }
    fun onPrecioChange(value: String) { precio = value }
    fun onStockChange(value: String) { stock = value }
    fun onCodigoBarrasChange(value: String) { codigoBarras = value }
    fun onDescripcionChange(value: String) { descripcion = value }

    fun nuevoProducto() {
        limpiarFormulario()
        isEditing = false
        editingId = null
    }

    fun empezarEdicion(producto: Producto) {
        nombre = producto.nombre
        categoria = producto.categoria
        precio = producto.precio.toString()
        stock = producto.stock.toString()
        codigoBarras = producto.codigoBarras
        descripcion = producto.descripcion ?: ""
        isEditing = true
        editingId = producto.idProducto
        mensaje = null
    }

    fun limpiarFormulario() {
        nombre = ""
        categoria = ""
        precio = ""
        stock = ""
        codigoBarras = ""
        descripcion = ""
        isEditing = false
        editingId = null
    }

    fun guardarProducto() {
        viewModelScope.launch {
            // Validaciones
            val precioDouble = precio.toDoubleOrNull()
            val stockInt = stock.toIntOrNull()

            if (nombre.isBlank()) {
                mostrarMensaje("El nombre es obligatorio")
                return@launch
            }
            if (precioDouble == null || precioDouble <= 0) {
                mostrarMensaje("El precio debe ser mayor a 0")
                return@launch
            }
            if (stockInt == null || stockInt < 0) {
                mostrarMensaje("El stock debe ser ≥ 0")
                return@launch
            }
            if (codigoBarras.length != 13) {
                mostrarMensaje("El código de barras debe tener 13 caracteres")
                return@launch
            }

            // Validar código único si es nuevo
            if (!isEditing) {
                val existente = repository.obtenerPorCodigo(codigoBarras)
                if (existente != null) {
                    mostrarMensaje("El código de barras ya existe")
                    return@launch
                }
            }

            val producto = Producto(
                idProducto = editingId ?: 0,
                nombre = nombre,
                categoria = categoria,
                precio = precioDouble,
                stock = stockInt,
                codigoBarras = codigoBarras,
                descripcion = descripcion.ifBlank { null }
            )

            try {
                if (isEditing) {
                    repository.actualizar(producto)
                    mostrarMensaje("Producto actualizado")
                } else {
                    repository.insertar(producto)
                    mostrarMensaje("Producto registrado")
                }
                limpiarFormulario()
                cargarProductos()
            } catch (e: Exception) {
                mostrarMensaje("Error: ${e.message}")
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.eliminar(producto)
                mostrarMensaje("Producto eliminado")
                cargarProductos()
            } catch (e: Exception) {
                mostrarMensaje("Error al eliminar")
            }
        }
    }

    private fun mostrarMensaje(msg: String) {
        mensaje = msg
    }

    fun limpiarMensaje() {
        mensaje = null
    }
}

class ProductoViewModelFactory(
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
