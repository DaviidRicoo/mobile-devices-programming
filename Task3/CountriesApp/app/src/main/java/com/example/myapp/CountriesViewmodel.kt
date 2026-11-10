package com.example.myapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.my_api_service.RetrofitInstance
import com.example.myapp.mymodel.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {

    // Lista completa recibida de la API
    private val _countries = MutableStateFlow<List<Country>>(emptyList())

    // Criterios de filtrado
    private val _nameFilter = MutableStateFlow("")
    val nameFilter = _nameFilter.asStateFlow()

    private val _regionFilter = MutableStateFlow<String?>(null)
    val regionFilter = _regionFilter.asStateFlow()

    // Voto: nombre del pais favorito (solo uno)
    private val _favourite = MutableStateFlow<String?>(null)
    val favourite = _favourite.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Regiones disponibles (para los chips del filtro)
    val regions: StateFlow<List<String>> = _countries
        .map { list -> list.mapNotNull { it.region }.filter { it.isNotBlank() }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Lista que se muestra: la completa con los filtros aplicados
    val filteredCountries: StateFlow<List<Country>> =
        combine(_countries, _nameFilter, _regionFilter) { countries, name, region ->
            countries.filter { country ->
                country.name.startsWith(name.trim(), ignoreCase = true) &&
                        (region == null || country.region == region)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getCountries()
                Log.i("COUNTRIES INFO:", "Success fetching: ${result.size}")
                _countries.value = result
            } catch (e: Exception) {
                Log.e("COUNTRIES ERROR:", "Error fetching countries: ${e.message}")
                _error.value = "Could not load countries. Check your internet connection."
            }
        }
    }

    fun onNameFilterChange(text: String) {
        _nameFilter.value = text
    }

    fun onRegionSelected(region: String?) {
        _regionFilter.value = region
    }

    fun vote(country: Country) {
        _favourite.value = country.name
    }
}
