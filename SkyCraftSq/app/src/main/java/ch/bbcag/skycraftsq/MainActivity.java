package ch.bbcag.skycraftsq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private Button bazaar;
    private ImageView search;
    private EditText entry;
    private ProgressBar progressBar;

    private static final String MOJANG_URL = "https://api.mojang.com/users/profiles/minecraft/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bazaar = findViewById(R.id.btnBazaar);
        entry = findViewById(R.id.bzEntry);
        search = findViewById(R.id.imgSearch);
        progressBar = findViewById(R.id.bazaarProgressBar);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entry.getText().length() < 2) {
                    generateAlertDialog("Name too short");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fetch(entry.getText().toString());
            }
        });
        bazaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BazaarActivity.class));
            }
        });

        entry.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if (keyevent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    progressBar.setVisibility(View.VISIBLE); //Make the progressbar visible until fetch is complete

                    if (entry.getText().length() < 2) {
                        generateAlertDialog("Name too short");
                        return false;
                    }
                    progressBar.setVisibility(View.VISIBLE);

                    fetch(entry.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void fetch(String name) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, MOJANG_URL + name, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                try {
                    String data = res.getString("id");
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), BankActivity.class);
                    intent.putExtra("KEY_UUID", data);
                    intent.putExtra("KEY_NAME", name);
                    startActivity(intent);
                } catch (JSONException e) {
                    generateAlertDialog(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = "Unknown error occurred";

                  if (error.networkResponse.data != null) {
                    try {
                        String responseString = new String(error.networkResponse.data, "UTF-8");
                        JSONObject responseJson = new JSONObject(responseString);
                        err = responseJson.optString("errorMessage", err);
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                generateAlertDialog(err);
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void generateAlertDialog(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}