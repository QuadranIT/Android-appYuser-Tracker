package eu.quadran.androidappyusertracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NextActivity extends AppCompatActivity {

    private final OkHttpClient httpClient = new OkHttpClient();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);


        //Displaying default okhttp request
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        String response = "";
        try {
            response = sendGETSync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setText(response.substring(0,500) + "...");
    }


    /** Called when the user taps the previous button */
    public void switchActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String sendGETSync() throws IOException{

        Request request = new Request.Builder()
                .url("https://world.openfoodfacts.org/api/v0/product/737628064502.json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body

            return response.body().string();

            // product.ingredients_text_with_allergens_en
        }
        catch (Exception e){
            throw new IOException("Unexpected error : " + e.getMessage());
        }

    }
}
