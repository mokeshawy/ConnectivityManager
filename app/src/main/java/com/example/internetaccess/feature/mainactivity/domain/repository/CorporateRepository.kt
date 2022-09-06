package com.example.internetaccess.feature.mainactivity.domain.repository

import com.example.internetaccess.core.error_handler.state.State
import com.example.internetaccess.feature.mainactivity.data.model.reponse.corporate_response.CorporateResponseDto
import kotlinx.coroutines.flow.Flow

interface CorporateRepository {
    suspend fun getCorporateDetails(id: Long): Flow<State<CorporateResponseDto>>
}