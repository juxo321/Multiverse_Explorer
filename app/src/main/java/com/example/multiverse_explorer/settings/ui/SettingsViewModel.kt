package com.example.multiverse_explorer.settings.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import com.example.multiverse_explorer.settings.domain.usecases.GetDataSourceUseCase
import com.example.multiverse_explorer.settings.domain.usecases.SaveDataSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getDataSourceUseCase: GetDataSourceUseCase,
    private val saveDataSourceUseCase: SaveDataSourceUseCase,
): ViewModel() {

    private val _dataSource: MutableStateFlow<String> = MutableStateFlow("Rest")
    val dataSource: StateFlow<String> = _dataSource

    var settingsUiState: UiState by mutableStateOf(UiState.Loading)
        private set


    init {
        getDataSource()
    }


    private fun getDataSource(){
        viewModelScope.launch {
            getDataSourceUseCase().collect() { result ->
                when(result){
                    is ResultApi.Success -> {
                        _dataSource.value = result.data
                        settingsUiState = UiState.Success
                    }
                    is ResultApi.Error -> {
                        settingsUiState = UiState.Error(result.message)
                    }
                }
            }

        }
    }

    fun saveDataSource(dataSource: String){
        viewModelScope.launch {
            saveDataSourceUseCase(dataSource)
        }
    }
}