package com.example.internetaccess.core.baserepository

import com.example.internetaccess.core.error_handler.ErrorBody
import com.example.internetaccess.core.error_handler.state.State
import com.example.internetaccess.core.error_handler.GeneralError
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.CancellationException


//TODO base repo should be refactored to accept multiple api calls
abstract class BaseRepository<RequestDto, ResponseDto> {

    //TODO should log which api is failing
    suspend fun getOperationState(requestDto: RequestDto): State<ResponseDto> {
        return try {
            performApiCall(requestDto)
        } catch (e: IOException) {
            State.Error(getIoExceptionError(e))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            State.Error(getGeneralExceptionError(e))
        }
    }

    abstract suspend fun performApiCall(requestDto: RequestDto): State<ResponseDto>

    fun <T> getNotSuccessfulResponseState(response: Response<*>, error: ErrorBody?): State<T> {
        return when {
            response.code() == 401 ->
                State.Error(getUnauthorizedError(response))
            response.code() == 404 && error?.errorCode == TRANSACTION_UUID_NOT_FOUND_ERROR ->
                State.Error(transactionUuidNotFound())
            else ->
                State.Error(getNotSuccessfulResponseError(response))
        }
    }

    private fun transactionUuidNotFound() = GeneralError.E(
        errorCode = TRANSACTION_UUID_NOT_FOUND_ERROR,
        logMessage = "Transaction UUID Not Found"
    )

    private fun getIoExceptionError(e: IOException) = GeneralError.E(
        errorCode = IO_EXCEPTION,
        logMessage = "Failed to load data from Api with IOException:",
        exception = e,
    )

    private fun getGeneralExceptionError(e: Exception) = GeneralError.E(
        errorCode = GENERAL_EXCEPTION,
        logMessage = "Failed to load data from Api with General exception",
        exception = e
    )

    private fun getNotSuccessfulResponseError(response: Response<*>) = GeneralError.E(
        errorCode = RESPONSE_ERROR,
        logMessage = "Api request to url: ${response.raw().request.url}: failed with code ${response.code()}",
        extraData = response
    )

    private fun getUnauthorizedError(response: Response<*>) = GeneralError.E(
        errorCode = RESPONSE_UNAUTHORIZED_ERROR,
        logMessage = "Api request to url: ${response.raw().request.url}: failed with code ${response.code()}",
        extraData = response
    )

    companion object {
        const val IO_EXCEPTION = "IO_EXCEPTION"
        const val GENERAL_EXCEPTION = "GENERAL_EXCEPTION"
        const val RESPONSE_ERROR = "RESPONSE_ERROR"
        const val RESPONSE_UNAUTHORIZED_ERROR = "UNAUTHORIZED_ERROR"
        const val TRANSACTION_UUID_NOT_FOUND_ERROR = "OTU_TUUIDNF"
    }
}