package com.psaanalytics.psademocompose.data

data class SchemaDescription(
    val description: String?,
)

data class SchemaUrlParts(
    val url: String,
    val name: String,
    val vendor: String,
    val version: String,
)
