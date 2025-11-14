package com.tecsup.bautista_semana5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.bautista_semana5.data.local.AppDatabase
import com.tecsup.bautista_semana5.data.repository.ProductoRepository
import com.tecsup.bautista_semana5.ui.ProductoViewModel
import com.tecsup.bautista_semana5.ui.ProductoViewModelFactory
import com.tecsup.bautista_semana5.ui.screens.FormScreen
import com.tecsup.bautista_semana5.ui.screens.ListScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(this)
        val repository = ProductoRepository(db.productoDao())
        val factory = ProductoViewModelFactory(repository)

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    val viewModel: ProductoViewModel = viewModel(factory = factory)

                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        composable("list") {
                            ListScreen(
                                viewModel = viewModel,
                                onNuevoClick = {
                                    navController.navigate("form")
                                },
                                onEditarClick = {
                                    navController.navigate("form")
                                }
                            )
                        }
                        composable("form") {
                            FormScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
