package com.example.guiabotanica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.guiabotanica.database.PlantaDao
import com.example.guiabotanica.model.Planta
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlantaViewModel(private val plantaDao: PlantaDao) : ViewModel() {

    // Estado que observará la interfaz (la lista de plantas que coinciden con la búsqueda)
    private val _plantasFiltradas = MutableStateFlow<List<Planta>>(emptyList())
    val plantasFiltradas: StateFlow<List<Planta>> = _plantasFiltradas.asStateFlow()

    private var trabajoDeBusqueda: Job? = null

    fun buscarPlantas(query: String) {
        // Si el usuario sigue tecleando rápido, cancelamos la búsqueda anterior
        trabajoDeBusqueda?.cancel()

        if (query.isBlank()) {
            _plantasFiltradas.value = emptyList()
            return
        }

        // Lanzamos la consulta SQL en segundo plano con un pequeño retraso (Debounce)
        trabajoDeBusqueda = viewModelScope.launch {
            delay(300)
            val resultados = plantaDao.buscarPlantas(query)
            _plantasFiltradas.value = resultados
        }
    }
}

// Factory para poder instanciar el ViewModel pasándole el acceso a la base de datos (DAO)
class PlantaViewModelFactory(private val dao: PlantaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlantaViewModel(dao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}