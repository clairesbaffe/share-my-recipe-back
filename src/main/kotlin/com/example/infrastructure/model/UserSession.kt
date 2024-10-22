package com.example.infrastructure.model

data class UserSession(val userId: Long, val username: String, val expiryTime: Long)