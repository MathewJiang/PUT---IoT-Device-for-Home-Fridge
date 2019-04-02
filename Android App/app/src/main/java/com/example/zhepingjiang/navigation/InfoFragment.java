package com.example.zhepingjiang.navigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Objects;

public class InfoFragment extends Fragment {
    private final String TAG = InfoFragment.class.getSimpleName();
    public String f_tempResult;
    public String f_weightResult;
    public String f_ccsResult;
    public String f_fridgeImgURL = "http://192.168.1.120:8080/show_fridge_image";
    // public String f_fridgeImgURL = "http://a4.att.hudong.com/18/69/20300542519369139990697635551_s.jpg";
    ImageView img;
    public Bitmap img_bitmap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Other Info");

        // set the frige image
        img = (ImageView)view.findViewById(R.id.fridgeImageView);
        GetImageFromURL getIMG = new GetImageFromURL(img);
        getIMG.execute(f_fridgeImgURL);


        // get temperature
        final TextView tempDataView = view.findViewById(R.id.temperature_data_text_view);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        String url_getTemperature = "http://192.168.1.120:8080/show_temperature";

        StringRequest tempRequest = new StringRequest(Request.Method.GET, url_getTemperature, response -> {
            f_tempResult = response;
            Log.i(TAG, "onResponse: " + f_tempResult);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (f_tempResult != null) {
                        f_tempResult = f_tempResult.substring(f_tempResult.indexOf("<h1>") + 4, f_tempResult.indexOf("</h1>"));
                        f_tempResult += "°C";
                    }

                    tempDataView.post(new Runnable() {
                        @Override
                        public void run() {
                            tempDataView.setText(f_tempResult);
                        }
                    });
                }
            });

            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, error -> tempDataView.setText("N/A"));
        queue.add(tempRequest);


        // get weight
        String url_getWeight = "http://192.168.1.120:8080/get_weight";
        final TextView weightDataView = view.findViewById(R.id.weight_data_text_view);

        StringRequest weightRequest = new StringRequest(Request.Method.GET, url_getWeight, response -> {
            f_weightResult = response;
            Log.i(TAG, "onResponse: " + f_weightResult);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (f_weightResult != null) {
                        f_weightResult = f_weightResult.substring(f_weightResult.indexOf("<h1>") + 4, f_weightResult.indexOf("</h1>"));
                    }

                    weightDataView.post(new Runnable() {
                        @Override
                        public void run() {
                            weightDataView.setText(f_weightResult);
                        }
                    });
                }
            });

            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, error -> weightDataView.setText("N/A"));
        queue.add(weightRequest);


        // get ccs
        String url_getccs = "http://192.168.1.120:8080/get_ccs811";
        final TextView ccsDataView = view.findViewById(R.id.ccs811_data_text_view);

        StringRequest ccsRequest = new StringRequest(Request.Method.GET, url_getccs, response -> {
            f_ccsResult = response;
            Log.i(TAG, "onResponse: " + f_ccsResult);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (f_ccsResult != null) {
                        f_ccsResult = f_ccsResult.substring(f_ccsResult.indexOf("<h1>") + 4, f_ccsResult.indexOf("</h1>"));
                    }

                    ccsDataView.post(new Runnable() {
                        @Override
                        public void run() {
                            ccsDataView.setText(f_ccsResult);
                        }
                    });
                }
            });

            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, error -> ccsDataView.setText("N/A"));
        queue.add(ccsRequest);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_layout, container, false);
    }

    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgView;

        public GetImageFromURL(ImageView imgView){
            this.imgView = imgView;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String url_display = url[0];
            img_bitmap = null;

            try {
                InputStream srt = new java.net.URL(url_display).openStream();
                img_bitmap = BitmapFactory.decodeStream(srt);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return img_bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imgView.setImageBitmap(img_bitmap);
        }
    }
}


