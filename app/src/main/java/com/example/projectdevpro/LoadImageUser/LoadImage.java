package com.example.projectdevpro.LoadImageUser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.projectdevpro.LoadImageUser.IloadImage;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadImage {
    IloadImage iloadImage;
    Context context;

    public LoadImage(Context context, IloadImage iloadImage){
        this.context = context;
        this.iloadImage = iloadImage;
    }

    public class loadImageFromInternet extends AsyncTask<String, Void, byte[]>{
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        @Override
        protected byte[] doInBackground(String... strings) {
            Request.Builder builder = new Request.Builder();
            builder.url(strings[0]);
            Request request = builder.build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            if (bytes.length > 0){
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iloadImage.getBitmap(bitmap);
            }
            super.onPostExecute(bytes);
        }
    }
}
