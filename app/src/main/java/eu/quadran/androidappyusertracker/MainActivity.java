package eu.quadran.androidappyusertracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient httpClient = new OkHttpClient();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Displaying default okhttp request
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        String response = "";
        try {
            response = sendGETSync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView) findViewById(R.id.textView3);
        textView.setText(response.substring(0,500) + "...");

        //System.out.println(power(2));
    }

    //public int power(int n){return n*n;}

    /* Called when the user taps the next button */
    public void switchActivity(View view) {
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String sendGETSync() throws IOException{

        Request request = new Request.Builder()
                .url("https://world.openfoodfacts.org/api/v0/product/737628064502.json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
        catch (Exception e){
            throw new IOException("Unexpected error : " + e.getMessage());
        }

    }

    void doGetRequest(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() { }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();

                        final Context context = getApplicationContext();
                        final CharSequence text = res.substring(0,50) + "...";
                        final int duration = Toast.LENGTH_SHORT;

                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        });
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void makeRequest(View view) throws IOException {
        doGetRequest("https://world.openfoodfacts.org/api/v0/product/737628064502.json");
    }
}
