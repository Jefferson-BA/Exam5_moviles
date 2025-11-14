package com.tecsup.bautista_semana5.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.bautista_semana5.data.local.Producto
import com.tecsup.bautista_semana5.ui.ProductoViewModel
import com.tecsup.bautista_semana5.ui.components.SimpleTextField
import androidx.compose.material.TopAppBar


@Composable
fun ListScreen(
    viewModel: ProductoViewModel,
    onNuevoClick: () -> Unit,
    onEditarClick: () -> Unit
) {
    val productos = viewModel.productos
    val mensaje = viewModel.mensaje
    val searchQuery = viewModel.searchQuery

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Productos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.nuevoProducto()
                onNuevoClick()
            }) {
                Text("+")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            SimpleTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchChange(it) },
                label = "Buscar por nombre o categoría",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos) { producto ->
                    ProductoItem(
                        producto = producto,
                        onClick = {
                            viewModel.empezarEdicion(producto)
                            onEditarClick()
                        },
                        onEliminarClick = { productoAEliminar = producto }
                    )
                    Divider()
                }
            }
        }

        if (productoAEliminar != null) {
            AlertDialog(
                onDismissRequest = { productoAEliminar = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Seguro que deseas eliminar este producto?") },
                confirmButton = {
                    TextButton(onClick = {
                        productoAEliminar?.let { viewModel.eliminarProducto(it) }
                        productoAEliminar = null
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { productoAEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    onClick: () -> Unit,
    onEliminarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Categoría: ${producto.categoria}")
            Text(text = "Precio: ${producto.precio}  | Stock: ${producto.stock}")
        }
        TextButton(onClick = onEliminarClick) {
            Text("Eliminar")
        }
    }
}
