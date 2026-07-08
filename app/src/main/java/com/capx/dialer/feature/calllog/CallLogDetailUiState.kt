package com.capx.dialer.feature.calllog

import com.capx.dialer.core.domain.model.RecentCall

data class CallLogDetailUiState(
    val number: String = "",
    val name: String? = null,
    val photoUri: String? = null,
    val calls: List<RecentCall> = emptyList(),
    val isLoading: Boolean = true
)
