package com.codebase.inmateapp.interfaces;

import com.codebase.inmateapp.models.ResponseModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Webservice {

    @GET("api-smssync/v1/gateway")
    Call<ResponseModel> getMessagesFromServer(@Query("secret") String secret, @Query("task") String task);

    @POST("api-smssync/v1/gateway")
    Call<ResponseModel> updateMessagesOnServer(@Query("secret") String secret, @Query("task") String task,
                                               @Body RequestBody body);

    @POST("api-smssync/v1/gateway")
    Call<ResponseModel> publishMessagesToServer(@Query("secret") String secret, @Query("task") String task,
                                                @Body RequestBody body);
}

