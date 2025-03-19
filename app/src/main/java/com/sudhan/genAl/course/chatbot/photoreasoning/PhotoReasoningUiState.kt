package com.sudhan.genAl.course.chatbot.photoreasoning

sealed interface PhotoReasoningUiState {
    data object Initial : PhotoReasoningUiState
    data object Loading : PhotoReasoningUiState
    data class Success(
        val output: String,
    ) : PhotoReasoningUiState

    data class Error(
        val errorMsg: String,
    ) : PhotoReasoningUiState

}