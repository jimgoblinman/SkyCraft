package ch.bbcag.skycraftsq;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class CollectionActivity extends AppCompatActivity {
    TextView uuid;
    ImageView item;

    private static final String COLLECTION_URL = "https://api.mojang.com/users/profiles/minecraft/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Intent resIntent = getIntent();
        String resUuid = resIntent.getStringExtra("KEY_UUID");
        String resName = resIntent.getStringExtra("KEY_NAME");
    }

    public void fetch() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, COLLECTION_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(req);
    }
}