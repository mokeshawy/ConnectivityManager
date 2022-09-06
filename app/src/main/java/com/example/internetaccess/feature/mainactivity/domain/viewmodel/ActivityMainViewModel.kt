package com.example.internetaccess.feature.mainactivity.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internetaccess.core.error_handler.state.State
import com.example.internetaccess.feature.mainactivity.data.model.reponse.corporate_response.CorporateResponseDto
import com.example.internetaccess.feature.mainactivity.domain.repository.CorporateRepository
import com.example.solarus.core.utils.error.GenralError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(
    private val corporateRepository: CorporateRepository,
) : ViewModel() {

    private val _responseState = MutableSharedFlow<State<CorporateResponseDto>>()
    val responseState: SharedFlow<State<CorporateResponseDto>> = _responseState

    fun getCorporate(id: Long) {
        viewModelScope.launch {
            _responseState.emit(State.Loading())
            corporateRepository.getCorporateDetails(id).collect {
                handleCorporateResponse(it)
            }
        }
    }

    private suspend fun handleCorporateResponse(state: State<CorporateResponseDto>) {
        when (state) {
            is State.Error -> _responseState.emit(State.Error(state.error))
            is State.Success -> handleSuccessState(state)
            else -> _responseState.emit(State.Loading())
        }
    }

    private suspend fun handleSuccessState(state: State.Success<CorporateResponseDto>) {
        if (state.data != null) {
            _responseState.emit(State.Success(state.data))
        } else {
            _responseState.emit(State.Error(GenralError.I(CORPORATE_NOT_FOUND)))
        }
    }

    companion object {
        const val CORPORATE_NOT_FOUND = "CORPORATE_NOT_FOUND"
    }
}