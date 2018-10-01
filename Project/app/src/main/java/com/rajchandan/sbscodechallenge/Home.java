package com.rajchandan.sbscodechallenge;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity
{
    private Button requestBtn;
    private TextView title;
    private ConstraintLayout layout;

    private String TAG = Home.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestBtn = (Button)findViewById(R.id.request_btn);
        title = (TextView)findViewById(R.id.title_txt);
        layout = (ConstraintLayout)findViewById(R.id.apiHome);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getInfo();
            }
        });
    }

    public void getInfo()
    {
        APIHandler hand = new APIHandler();

        // Making a request to url and getting response
        hand.makeAPICalls();
        while (APIHandler.isdoneconn != true) ;

        String jsonStr = APIHandler.response;

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONArray jsonArray = jsonObj.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    if(i == 5)
                    {
                        JSONObject inside = jsonArray.getJSONObject(i);
                        String apiTitle = inside.getString("title");
                        String apiColor = inside.getString("color");

                        title.setText(apiTitle);
                        layout.setBackgroundColor(Color.parseColor(apiColor));
                    }
                    else
                        continue;
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
}
