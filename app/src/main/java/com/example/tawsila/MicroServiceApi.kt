package com.example.tawsila

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MicroServiceApi {


 //   @GET("/auth/all")
 //   fun getAllUsers(): Call<List<UserApp>>

    @POST("/auth/register")
    fun registerUser(@Body userDTO: UserDTO): Call<String>

    @POST("/auth/token")
    fun getToken(@Body authRequest: AuthRequest): Call<String>

    @GET("/auth/validate")
    fun validateToken(@Query("token") token: String): Call<String>

    @GET("/auth/admin")
    fun getAdminEndpoint(): Call<String>
}
