package pl.dzazef.newton.api.zeroes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NewtonZeroesAPI {
    @GET("/{operation}/{expression}")
    fun getResult(@Path("operation") operation : String, @Path("expression") expression : String) : Call<NewtonZeroesDTO>
}