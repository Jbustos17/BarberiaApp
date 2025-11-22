package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.barberia.model.CarritoItem

class CarritoViewModel : ViewModel() {
    
    private val _carritoItems = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carritoItems: StateFlow<List<CarritoItem>> = _carritoItems
    
    private val _porcentajeDescuento = MutableStateFlow<Int?>(null)
    val porcentajeDescuento: StateFlow<Int?> = _porcentajeDescuento
    
    private val _codigoCupon = MutableStateFlow<String?>(null)
    val codigoCupon: StateFlow<String?> = _codigoCupon
    
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
        _porcentajeDescuento.value = null
        _codigoCupon.value = null
    }
    
    fun getTotal(): Double {
        val subtotal = _carritoItems.value.sumOf { it.calcularTotal() }
        val descuento = _porcentajeDescuento.value ?: 0
        if (descuento > 0) {
            val montoDescuento = subtotal * (descuento / 100.0)
            return subtotal - montoDescuento
        }
        return subtotal
    }
    
    fun getSubtotal(): Double {
        return _carritoItems.value.sumOf { it.calcularTotal() }
    }
    
    fun getDescuento(): Double {
        val subtotal = getSubtotal()
        val descuento = _porcentajeDescuento.value ?: 0
        if (descuento > 0) {
            return subtotal * (descuento / 100.0)
        }
        return 0.0
    }
    
    fun aplicarCupon(porcentaje: Int, codigo: String) {
        _porcentajeDescuento.value = porcentaje
        _codigoCupon.value = codigo
    }
    
    fun removerCupon() {
        _porcentajeDescuento.value = null
        _codigoCupon.value = null
    }
    
    fun getCantidadItems(): Int {
        return _carritoItems.value.size
    }
}


