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

class PlantaViewModel(private val dao: PlantaDao) : ViewModel() {

    private val _plantasFiltradas = MutableStateFlow<List<Planta>>(emptyList())
    val plantasFiltradas: StateFlow<List<Planta>> = _plantasFiltradas.asStateFlow()

    private var trabajoBusqueda: Job? = null

    fun buscarPlantas(query: String) {
        trabajoBusqueda?.cancel()

        if (query.isBlank()) {
            _plantasFiltradas.value = emptyList()
            return
        }

        trabajoBusqueda = viewModelScope.launch {
            delay(300)
            _plantasFiltradas.value = dao.buscarPlantas(query)
        }
    }
}


class PlantaViewModelFactory(private val dao: PlantaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlantaViewModel(dao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}