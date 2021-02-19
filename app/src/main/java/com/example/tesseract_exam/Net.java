package com.example.tesseract_exam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Net {
    private static Net ourinstance = new Net();

    public static Net getInstance(){
        return ourinstance;
    }

    Gson gson = new GsonBuilder().setLenient().create();
    private Net(){
    }
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://3.15.140.109/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    OcrFactory ocrFactory;

    public OcrFactory getOcrFactory(){
        if(ocrFactory == null){
            ocrFactory = retrofit.create(OcrFactory.class);
        }
        return ocrFactory;
    }
    private HttpLoggingInterceptor httpLoggingInterceptor(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                android.util.Log.e("MyLogis :", message + "");
            }
        });

        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
