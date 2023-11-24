package com.example.tawsila

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MicroServiceApi {


 //   @GET("/auth/all")
 //   fun getAllUsers(): Call<List<UserApp>>

    @POST("/auth/register")
    fun registerUser(@Body userDTO: UserDTO): Call<String>

   // @POST("/auth/token")
  //  fun getToken(@Body authRequest: AuthRequest): Call<String>
   @POST("/auth/token")
   fun getToken(@Body authRequest: AuthRequest): Call<Map<String, Any>>



    @GET("/auth/validate")
    fun validateToken(@Query("token") token: String): Call<String>

    @GET("/auth/admin")
    fun getAdminEndpoint(): Call<String>

    @POST("/auth/upload_image/{id}")
    @Multipart
    fun setImageById(
        @Path("id") id: Long,
        @Part file: MultipartBody.Part
    ): Call<String>

    @GET("/auth/{id}")
    fun getUserInfo(@Path("id") userId: Long): Call<UserDTO>

    @PUT("/auth/{id}")
    fun updateUser(@Path("id") userId: Long, @Body updatedUser: UserDTO): Call<String>
}
