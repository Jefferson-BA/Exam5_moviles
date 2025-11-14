package com.tecsup.bautista_semana5.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.bautista_semana5.ui.ProductoViewModel
import com.tecsup.bautista_semana5.ui.components.SimpleTextField
import androidx.compose.material.TopAppBar


@Composable
fun FormScreen(
    viewModel: ProductoViewModel,
    onBack: () -> Unit
) {
    val nombre = viewModel.nombre
    val categoria = viewModel.categoria
    val precio = viewModel.precio
    val stock = viewModel.stock
    val codigoBarras = viewModel.codigoBarras
    val descripcion = viewModel.descripcion
    val isEditing = viewModel.isEditing
    val mensaje = viewModel.mensaje

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar Producto" else "Nuevo Producto")
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SimpleTextField(
                value = nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = "Nombre",
                modifier = Modifier.fillMaxWidth()
            )
            SimpleTextField(
                value = categoria,
                onValueChange = { viewModel.onCategoriaChange(it) },
                label = "Categoría",
                modifier = Modifier.fillMaxWidth()
            )
            SimpleTextField(
                value = precio,
                onValueChange = { viewModel.onPrecioChange(it) },
                label = "Precio",
                modifier = Modifier.fillMaxWidth()
            )
            SimpleTextField(
                value = stock,
                onValueChange = { viewModel.onStockChange(it) },
                label = "Stock",
                modifier = Modifier.fillMaxWidth()
            )
            SimpleTextField(
                value = codigoBarras,
                onValueChange = { viewModel.onCodigoBarrasChange(it) },
                label = "Código de barras (13)",
                modifier = Modifier.fillMaxWidth()
            )
            SimpleTextField(
                value = descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = "Descripción (opcional)",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = {
                    viewModel.guardarProducto()
                    // si todo ok, podrías hacer onBack() aquí si quieres
                }) {
                    Text(if (isEditing) "Actualizar" else "Guardar")
                }
                OutlinedButton(onClick = {
                    viewModel.limpiarFormulario()
                    onBack()
                }) {
                    Text("Cancelar")
                }
            }
        }
    }
}
