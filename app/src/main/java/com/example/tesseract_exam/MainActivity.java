package com.example.tesseract_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//https://choijava.tistory.com/68 참조 방진
public class MainActivity extends AppCompatActivity {
    TessBaseAPI tess;
    String datapath = "";
    private String OCRresult;
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터경로
        datapath = getFilesDir() + "/tesseract/";

        //한글 & 영어 데이터 체크
        checkFile(new File(datapath + "tessdata/"),"kor");
        checkFile(new File(datapath + "tessdata/"),"eng");

        //문자 인식을 수행할 tess 객체 생성
        String lang = "kor+eng";
        tess = new TessBaseAPI();
        tess.init(datapath,lang);

        //문자 인식 진행
        processImage(BitmapFactory.decodeResource(getResources(),R.drawable.test1));


    }

    public void processImage(Bitmap bitmap){
        Toast.makeText(getApplicationContext(), "시간소요~", Toast.LENGTH_LONG).show();
        tess.setImage(bitmap);
        OCRresult = tess.getUTF8Text();
        TextView OCRTextView = (TextView) findViewById(R.id.tv_result);
        OCRTextView.setText(OCRresult);

        Call<String> res = Net.getInstance().getOcrFactory().connect("bangjinhyuk");
            res.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        if(response.body() != null){
                            String success = response.body().toString();
                            Log.d("Main 통신", response.body().toString());
                        }else{
                            Log.e("Main 통신", "실패 1 response 내용이 없음");
                        }
                    }else{
                        Log.e("Main 통신", "실패 2 서버 에러");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Main 통신", "실패 3 통신 에러 "+t.getLocalizedMessage());
                }
            });

        Map<String, String> map = new HashMap<String, String>();
        map.put("name","bbangi");
        map.put("birth", "0103");

        Call<Semester> res1 = Net.getInstance().getOcrFactory().connecting(map);
        res1.enqueue(new Callback<Semester>() {
            @Override
            public void onResponse(Call<Semester> call, Response<Semester> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Semester semesters = response.body();
                        Log.d("Map 통신", semesters.toString());
                    }else{
                        Log.e("Map 통신", "실패 1 response 내용이 없음");
                    }
                }else{
                    Log.e("Map 통신", "실패 2 서버 에러");
                }
            }

            @Override
            public void onFailure(Call<Semester> call, Throwable t) {
                Log.e("Map 통신", "실패 3 통신 에러 "+t.getLocalizedMessage());

            }
        });

        Req_semester req_semester = new Req_semester();
        req_semester.setSemester(new Semester("소프트웨어학과",
                "미디어학과",
                "컴구",
                "창소입",
                "기창경",
                "",
                ""
        ));
        Call<Res_semester> res2 = Net.getInstance().getOcrFactory().semester(req_semester);
        res2.enqueue(new Callback<Res_semester>() {
            @Override
            public void onResponse(Call<Res_semester> call, Response<Res_semester> response) {
                if(response.isSuccessful()) {
                    if (response.body() != null) {
                        Res_semester res_semester = response.body();


                        Log.d("semester 통신",res_semester.code+"");

                    } else {
                        Log.e("semester 통신", "실패 1 response 내용이 없음");
                    }
                }else{
                    Log.e("semester 통신", "실패 2 서버 에러");
                }
            }

            @Override
            public void onFailure(Call<Res_semester> call, Throwable t) {

                Log.e("semester 통신", "실패 3 통신 에러 "+t.getLocalizedMessage());

            }
        });


    }




    private void copyFiles(String lang){
        try {
            String filepath = datapath + "/tessdata/" + lang + ".traineddata";

            AssetManager assetManager= getAssets();

            InputStream inStream = assetManager.open("tessdata/"+lang + ".traineddata");
            OutputStream outStream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while((read = inStream.read(buffer)) != -1){
                outStream.write(buffer,0,read);
            }
            outStream.flush();
            outStream.close();
            inStream.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void checkFile(File dir, String lang){
        if (!dir.exists()&&dir.mkdirs()){
            copyFiles(lang);
        }

        if(dir.exists()){
            String datafilePath = datapath + "/tessdata/" + lang + ".traineddata";
            File datafile = new File(datafilePath);
            if(!datafile.exists()){
                copyFiles(lang);
            }
        }
    }
}

class Res_semester {

    int code; //서버로부터의 응답 코드. 404, 500, 200 등.
    String msg; //서버로부터의 응답 메세지.
    Semester semester; //가입한 유저 정보.

}
class Req_semester {


    Semester semester;


    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

}