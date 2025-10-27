package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.barberia.model.CarritoItem

class CarritoViewModel : ViewModel() {
    
    private val _carritoItems = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carritoItems: StateFlow<List<CarritoItem>> = _carritoItems
    
    fun agregarItem(item: CarritoItem) {
        val currentItems = _carritoItems.value.toMutableList()
        currentItems.add(item)
        _carritoItems.value = currentItems
    }
    
    fun eliminarItem(item: CarritoItem) {
        val currentItems = _carritoItems.value.toMutableList()
        currentItems.remove(item)
        _carritoItems.value = currentItems
    }
    
    fun limpiarCarrito() {
        _carritoItems.value = emptyList()
    }
    
    fun getTotal(): Double {
        return _carritoItems.value.sumOf { it.calcularTotal() }
    }
    
    fun getCantidadItems(): Int {
        return _carritoItems.value.size
    }
}


