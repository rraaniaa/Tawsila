package com.example.tawsila


data class UserDTO(
    val name: String = "",
    val email: String = "",
    val password: String? = "",
    val profileImage: Any?,
    val roleName: String = ""
)
