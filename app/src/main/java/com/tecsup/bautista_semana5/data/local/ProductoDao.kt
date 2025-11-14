package com.tecsup.bautista_semana5.data.local

import androidx.room.*

@Dao
interface ProductoDao {

    @Query("SELECT * FROM producto ORDER BY nombre ASC")
    suspend fun getAll(): List<Producto>

    @Query("""
        SELECT * FROM producto 
        WHERE nombre LIKE '%' || :query || '%' 
        OR categoria LIKE '%' || :query || '%'
        ORDER BY nombre ASC
    """)
    suspend fun search(query: String): List<Producto>

    @Query("SELECT * FROM producto WHERE codigoBarras = :codigo LIMIT 1")
    suspend fun getByCodigoBarras(codigo: String): Producto?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(producto: Producto)

    @Update
    suspend fun update(producto: Producto)

    @Delete
    suspend fun delete(producto: Producto)
}
