package com.example.internetaccess.feature.second_fragment.data.repository

import com.example.internetaccess.core.baserepository.BaseRepository
import com.example.internetaccess.core.error_handler.getErrorBody
import com.example.internetaccess.core.error_handler.state.State
import com.example.internetaccess.services.InternetServices
import com.example.internetaccess.core.error_handler.GeneralError
import com.example.internetaccess.feature.second_fragment.data.model.reponse.corporate_response.CorporateResponseDto
import com.example.internetaccess.feature.second_fragment.domain.repository.CorporateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CorporateRepositoryImpl @Inject constructor(private val internetServices: InternetServices) :
    BaseRepository<Long, CorporateResponseDto>(), CorporateRepository {


    override suspend fun getCorporateDetails(id: Long) = flow {
        emit(getOperationState(id))
    }.flowOn(Dispatchers.IO)

    override suspend fun performApiCall(requestDto: Long): State<CorporateResponseDto> {
        val response = internetServices.retrieveCorporate(requestDto)
        return handleResponse(response)
    }

    private fun handleResponse(response: Response<CorporateResponseDto>): State<CorporateResponseDto> {
        val errorBody = response.getErrorBody()
        return when {
            response.isSuccessful -> State.Success(response.body())
            response.code() == 404 -> getCorporateNotFoundError()
            else -> getNotSuccessfulResponseState(response, errorBody)
        }
    }

    private fun <T> getCorporateNotFoundError() =
        State.Error<T>(
            GeneralError.E(errorCode = CORPORATE_NOT_FOUND,
            logMessage = "Corporate not found"))

    companion object {
        const val CORPORATE_NOT_FOUND = "NOT_FOUND"
    }
}