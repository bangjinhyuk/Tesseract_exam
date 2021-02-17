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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
        Toast.makeText(getApplicationContext(), "시간소요", Toast.LENGTH_LONG).show();
        tess.setImage(bitmap);
        OCRresult = tess.getUTF8Text();
        TextView OCRTextView = (TextView) findViewById(R.id.tv_result);
        OCRTextView.setText(OCRresult);

        try{
            mSocket = IO.socket("http://ec2-3-15-140-109.us-east-2.compute.amazonaws.com/ocr");
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on("OCRresult", onMessageReceived);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String finalOCRresult = OCRresult;




    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mSocket.emit("clientMessage", OCRresult);
        }
    };

    private  Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject success = (JSONObject) args[0];
                Log.d("서버 결과", success.getString("success"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

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