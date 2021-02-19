package com.example.tesseract_exam;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OcrFactory {
    @GET("ocr/connect")
    Call<String> connect(@Query("userEmail") String email);

    @GET("ocr/connecting")
    Call<Semester> connecting(@QueryMap Map<String, String> success);

    @POST("ocr/semester")
    Call<Res_semester> semester(@Body Req_semester user);
}
