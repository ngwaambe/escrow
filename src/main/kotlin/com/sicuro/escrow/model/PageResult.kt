package com.sicuro.escrow.model

data class PageResult<T>(
    val totalResultCount: Long,
    val resultCount: Int,
    val data: T
)
