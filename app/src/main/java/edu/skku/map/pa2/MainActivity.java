package edu.skku.map.pa2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    Button btn1;
    EditText editText;
    Bitmap bitmap;
    public String link;
    int[] check;
    int[] board_check;
    GridView gridView;
    GridView row_number;
    GridView column_number;
    int[][]row;
    int[][]column;
    int final_check;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.button1);
        editText = (EditText) findViewById(R.id.edittext);
        gridView = findViewById(R.id.grid_view);
        gridView.setNumColumns(20);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = new int[400];
                board_check=new int[400];
                String clientId = "NSlXcr6s9PIHU_HfXz3M";
                String clientSecret = "vrrG0ay54A";

                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://openapi.naver.com/v1/search/image").newBuilder();
                urlBuilder.addQueryParameter("query", editText.getText().toString());
                urlBuilder.addQueryParameter("display", "1");
                urlBuilder.addQueryParameter("start", "1");
                String apiURL = urlBuilder.build().toString();

                OkHttpClient client = new OkHttpClient();

                Request req = new Request.Builder()
                        .url(apiURL)
                        .addHeader("X-Naver-Client-Id", clientId)
                        .addHeader("X-Naver-Client-Secret", clientSecret)
                        .method("GET", null)
                        .build();

                client.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Gson gson = new GsonBuilder().create();

                        final Image data1 = gson.fromJson(response.body().string(), Image.class);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                link=null;
                                List<Image.Item> list = data1.getItems();
                                link = list.get(0).getLink();
                                new DownloadFilesTask().execute(link);

                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = new int[400];
                board_check=new int[400];
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    Bitmap[] imgPic = new Bitmap[400];
                    bitmap=resize(bitmap);
                    bitmap=grayScale(bitmap);

                    final_check=0;
                    int Orgin_H = bitmap.getHeight();
                    int Orgin_W = bitmap.getWidth();
                    int PicW = Orgin_W / 20;
                    int PicH = Orgin_H / 20;

                    for (int i = 0; i < 20; i++) {
                        for (int j = 0; j < 20; j++) {
                            imgPic[i * 20 + j] = Bitmap.createBitmap(bitmap, j * PicW, i * PicH, PicW, PicH);
                            imgPic[i * 20 + j] = meangray(imgPic[i * 20 + j], check, i * 20 + j);
                        }
                    }
                    temp=final_check;
                    row=new int[20][10];
                    column=new int[20][20];
                    row_num(row,check);
                    column_num(column,check);

                    List<String> values;
                    values=new ArrayList<>();
                    for(int i=0;i<20;i++){
                        String str = null;
                        for(int j=0;j<10;j++){
                            int a=row[i][j];
                            if(a==0){
                                if(j==0) {
                                    str = String.valueOf(a);
                                    break;
                                }
                                else{
                                    break;
                                }
                            }
                            else{
                                if(str==null){
                                    str=String.valueOf(a);
                                }
                                else {
                                    str += ("  "+String.valueOf(a));
                                }
                            }

                        }
                        values.add(str);
                    }
                    List<String> values2;
                    values2=new ArrayList<>();
                    for(int i=0;i<20;i++){
                        String str = null;
                        for(int j=0;j<10;j++){
                            int a=column[i][j];
                            if(a==0){
                                if(j==0) {
                                    str = String.valueOf(a);
                                    break;
                                }
                                else{
                                    break;
                                }
                            }
                            else{
                                if(str==null){
                                    str=String.valueOf(a);
                                }
                                else {
                                    str += ("\n"+String.valueOf(a));
                                }
                            }
                        }
                        values2.add(str);
                    }

                    nonoboard baseAdapter = new nonoboard(MainActivity.this, imgPic,board_check);
                    gridView.setAdapter(baseAdapter);

                    row_number=findViewById(R.id.row_number);
                    rowAdapter baseAdapter2=new rowAdapter(MainActivity.this,values);
                    row_number.setAdapter(baseAdapter2);

                    column_number=findViewById(R.id.column_number);
                    rowAdapter baseAdapter3=new rowAdapter(MainActivity.this,values2);
                    column_number.setAdapter(baseAdapter3);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            int x = position / 20;
                            int y = position % 20;
                            int a = check[x * 20 + y];
                            if ((a==1)&&board_check[x*20+y]==0) {
                                board_check[x*20+y]=1;
                                nonoboard baseAdapter4 = new nonoboard(MainActivity.this, imgPic,board_check);
                                gridView.setAdapter(baseAdapter4);
                                if((--temp)==0)Toast.makeText(MainActivity.this,"finish",Toast.LENGTH_LONG).show();
                            }
                            else if(a==0){
                                Toast.makeText(MainActivity.this,"Wrong", Toast.LENGTH_SHORT).show();
                                board_check=new int[400];
                                temp=final_check;
                                nonoboard baseAdapter = new nonoboard(MainActivity.this, imgPic,board_check);
                                gridView.setAdapter(baseAdapter);


                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Bitmap[] imgPic = new Bitmap[400];

            result=resize(result);
            result=grayScale(result);

            final_check=0;
            int Orgin_H = result.getHeight();
            int Orgin_W = result.getWidth();
            int PicW = Orgin_W / 20;
            int PicH = Orgin_H / 20;

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    imgPic[i * 20 + j] = Bitmap.createBitmap(result, j * PicW, i * PicH, PicW, PicH);
                    imgPic[i * 20 + j] = meangray(imgPic[i * 20 + j], check, i * 20 + j);
                }
            }

            temp=final_check;
            row=new int[20][10];
            column=new int[20][20];
            row_num(row,check);
            column_num(column,check);

            List<String> values;
            values=new ArrayList<>();
            for(int i=0;i<20;i++){
                String str = null;
                for(int j=0;j<10;j++){
                    int a=row[i][j];
                    if(a==0){
                        if(j==0) {
                            str = String.valueOf(a);
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    else{
                        if(str==null){
                            str=String.valueOf(a);
                        }
                        else {
                            str += ("  "+String.valueOf(a));
                        }
                    }

                }
                values.add(str);
            }
            List<String> values2;
            values2=new ArrayList<>();
            for(int i=0;i<20;i++){
                String str = null;
                for(int j=0;j<10;j++){
                    int a=column[i][j];
                    if(a==0){
                        if(j==0) {
                            str = String.valueOf(a);
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    else{
                        if(str==null){
                            str=String.valueOf(a);
                        }
                        else {
                            str += ("\n"+String.valueOf(a));
                        }
                    }
                }
                values2.add(str);
            }

           nonoboard baseAdapter = new nonoboard(MainActivity.this, imgPic,board_check);
           gridView.setAdapter(baseAdapter);

           row_number=findViewById(R.id.row_number);
           rowAdapter baseAdapter2=new rowAdapter(MainActivity.this,values);
           row_number.setAdapter(baseAdapter2);

           column_number=findViewById(R.id.column_number);
           rowAdapter baseAdapter3=new rowAdapter(MainActivity.this,values2);
           column_number.setAdapter(baseAdapter3);

           gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    int x = position / 20;
                    int y = position % 20;
                    int a = check[x * 20 + y];
                    if ((a==1)&&board_check[x*20+y]==0) {
                        board_check[x*20+y]=1;
                        nonoboard baseAdapter4 = new nonoboard(MainActivity.this, imgPic,board_check);
                        gridView.setAdapter(baseAdapter4);
                        if((--temp)==0)Toast.makeText(MainActivity.this,"finish",Toast.LENGTH_SHORT).show();
                    }
                    else if(a==0){
                        Toast.makeText(MainActivity.this,"Wrong", Toast.LENGTH_SHORT).show();
                        board_check=new int[400];
                        temp=final_check;
                        nonoboard baseAdapter = new nonoboard(MainActivity.this, imgPic,board_check);
                        gridView.setAdapter(baseAdapter);

                    }
                }
            });
        }
    }

    private Bitmap resize(Bitmap result){
        int new_W=300;
        int new_H=300;
        result=Bitmap.createScaledBitmap(result,new_W,new_H,true);
        return result;
    }

    private Bitmap grayScale(final Bitmap orgBitmap){
        Log.i("gray","in");
        int width,height;
        width=orgBitmap.getWidth();
        height=orgBitmap.getHeight();

        Bitmap bmpGrayScale=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_4444);

        int A,R,G,B;
        int pixel;
        for(int x=0;x<width;++x){
            for(int y=0;y<height;++y){
                pixel=orgBitmap.getPixel(x,y);
                A= Color.alpha(pixel);
                R=Color.red(pixel);
                G=Color.green(pixel);
                B=Color.blue(pixel);
                int gray=(int)(0.2989*R+0.5870*G+0.1140*B);

                if(gray>128)
                    gray=255;
                else
                    gray=0;
                bmpGrayScale.setPixel(x,y,Color.argb(A,gray,gray,gray));
            }
        }
        return bmpGrayScale;
    }

    private Bitmap meangray(final Bitmap orgBitmap, int[] check, int index) {
        Log.i("gray", "in");
        int width, height;
        width = orgBitmap.getWidth();
        height = orgBitmap.getHeight();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        long mean_gray = 0;
        long pixelCount = 0;
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixel = orgBitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

                mean_gray += gray;
                pixelCount++;

                if (gray > 128)
                    gray = 255;
                else
                    gray = 0;
                bmpGrayScale.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }


        mean_gray = mean_gray / pixelCount;
        if (mean_gray > 128) {
            check[index] = 0;
        } else {
            check[index] = 1;
            final_check++;
            System.out.println(final_check);
        }

        return bmpGrayScale;
    }

    private int[][] row_num(final int [][] row, final int[] check){
        for(int i=0;i<20;i++){
            int cnt=0;
            int index=0;
            for(int j=0;j<20;j++){
                if(check[i*20+j]==1){
                    cnt++;
                    if(j==19){
                        row[i][index]=cnt;
                        System.out.println(cnt);
                        index++;
                        cnt=0;
                    }
                }
                else{
                    if(cnt!=0){
                        row[i][index]=cnt;
                        System.out.println(cnt);
                        index++;
                    }
                    cnt=0;
                }
            }
        }
        return row;
    }

    private int[][] column_num(final int[][]column,final int[]check){
        for(int i=0;i<20;i++){
            int index=0;
            int cnt=0;
            for(int j=0;j<20;j++){
                if(check[j*20+i]==1){
                    cnt++;
                    if(j==19){
                        column[i][index]=cnt;
                        System.out.println(cnt);
                        index++;
                        cnt=0;
                    }
                }
                else{
                    if(cnt!=0){
                        column[i][index]=cnt;
                        System.out.println(cnt);
                        index++;
                    }
                    cnt=0;
                }
            }
        }
        return column;
    }

}