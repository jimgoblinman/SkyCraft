package ch.bbcag.skycraftsq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.bbcag.skycraftsq.model.BankProfile;
import ch.bbcag.skycraftsq.model.TransactionItem;

public class BankActivity extends AppCompatActivity {
    private String action;
    private String amount;
    private long timestamp;
    private String initiatorName;

    private List<BankProfile> bankList = new ArrayList<>();

    ProgressBar progressBar;
    ListView profileList;
    ImageView back;

    private static final String BANKING_URL = "https://api.hypixel.net/skyblock/profiles?key=f53aed38-f632-4f38-828c-c160ea5aaf26&uuid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        progressBar = findViewById(R.id.bankProgressBar);
        profileList = findViewById(R.id.bankList);
        back = findViewById(R.id.bankBack);

        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        fetch(BANKING_URL + intent.getStringExtra("KEY_UUID"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BankTransactions.class);

                BankProfile selected = (BankProfile)parent.getItemAtPosition(position);
                intent.putExtra("KEY_PROFILE_ID", selected.getId());

                Gson gson = new Gson();
                String transactionJson = gson.toJson(selected.getTransactions());
                intent.putExtra("KEY_TRANSACTIONS", transactionJson);

                startActivity(intent);
            }
        };
        profileList.setOnItemClickListener(onItemClickListener);
    }

    public void fetch(String URL) {
        final ArrayAdapter<BankProfile> bankAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_element);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                try {
                    JSONArray profilesArray = res.getJSONArray("profiles");

                    int id = 1;
                    for (int i = 0; i < profilesArray.length(); i++) {
                        JSONObject profileObject = profilesArray.getJSONObject(i);

                        if (profileObject.has("banking")) {
                            JSONObject bankingObject = profileObject.getJSONObject("banking");

                            List<TransactionItem> transactionList = new ArrayList<>();

                            JSONArray transactionsArray = bankingObject.getJSONArray("transactions");
                            for (int j = 0; j < transactionsArray.length(); j++) {
                                JSONObject transactionObject = transactionsArray.getJSONObject(j);

                                String action = transactionObject.getString("action");
                                String amount = transactionObject.getString("amount");
                                long timestamp = transactionObject.getLong("timestamp");
                                String initiatorName = transactionObject.getString("initiator_name");

                                transactionList.add(new TransactionItem(action, amount, timestamp, initiatorName));
                            }
                            bankList.add(new BankProfile(id++, transactionList));
                        }
                    }
                    bankAdapter.addAll(bankList);
                    profileList.setAdapter(bankAdapter);

                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    generateAlertDialog(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                generateAlertDialog(error.toString());
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void generateAlertDialog(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}