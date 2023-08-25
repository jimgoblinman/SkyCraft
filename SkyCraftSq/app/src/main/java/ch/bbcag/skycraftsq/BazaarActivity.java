package ch.bbcag.skycraftsq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Iterator;
import java.util.List;

import ch.bbcag.skycraftsq.model.BankProfile;
import ch.bbcag.skycraftsq.model.ProductItem;
import ch.bbcag.skycraftsq.model.TransactionItem;

public class BazaarActivity extends AppCompatActivity {
    ProgressBar progressBar;
    Button bazaarButton;
    ListView itemList;
    EditText bzEntry;
    TextView bzTitle;
    TextView bzOr;
    ImageView bzBack;

    private static final String BAZAAR_URL = "https://api.hypixel.net/skyblock/bazaar?key=f53aed38-f632-4f38-828c-c160ea5aaf26";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazaar);

        progressBar = findViewById(R.id.bazaarProgressBar);
        bazaarButton = findViewById(R.id.bazaarButton);
        itemList = findViewById(R.id.bazaarList);
        bzEntry = findViewById(R.id.bzEntry);
        bzTitle = findViewById(R.id.bzTitle);
        bzOr = findViewById(R.id.bzOr);
        bzBack = findViewById(R.id.bzBack);

        progressBar.setVisibility(View.VISIBLE);

        fetch(BAZAAR_URL);

        bazaarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bzBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(bzBack.getWindowToken(), 0);
                bzEntry.clearFocus();
            }
        });

        itemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean isAtTop = (firstVisibleItem == 0);
                /*if (isAtTop) {
                    setLayout(true);
                } else {
                    setLayout(false);
                }*/
            }
        });

        bzEntry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setLayout(true);
                } else {
                    setLayout(false);
                }
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)  {
                LinearLayout bzInfo = v.findViewById(R.id.bzInfos);
                TextView bzItem = v.findViewById(R.id.bzItem);

                if (bzInfo.getVisibility() == View.GONE) {
                    bzInfo.setVisibility(View.VISIBLE);
                } else {
                    bzInfo.setVisibility(View.GONE);
                }
            }
        };
        itemList.setOnItemClickListener(onItemClickListener);
    }

    private void setLayout(boolean state) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) bzEntry.getLayoutParams();
        if (!state) {
            bzTitle.setVisibility(View.GONE);
            bzOr.setVisibility(View.GONE);
            bazaarButton.setVisibility(View.GONE);

            bzBack.setVisibility(View.VISIBLE);

            layoutParams.verticalBias = 0.0f;
        } else {
            bzTitle.setVisibility(View.VISIBLE);
            bzOr.setVisibility(View.VISIBLE);
            bazaarButton.setVisibility(View.VISIBLE);

            bzBack.setVisibility(View.GONE);

            layoutParams.verticalBias = 0.5f;
        }
        bzEntry.setLayoutParams(layoutParams);
    }

    public void fetch(String URL) {
        List<ProductItem> productItemList = new ArrayList<>();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                try {
                    JSONObject productObject = res.getJSONObject("products");
                    Iterator<String> keys = productObject.keys();
                    while (keys.hasNext()) {
                        String itemName = keys.next();
                        JSONObject itemObject = productObject.getJSONObject(itemName);

                        String name = itemObject.getString("product_id");
                        String buyPrice = itemObject.getJSONObject("quick_status").getString("buyPrice");
                        String buyVolume = itemObject.getJSONObject("quick_status").getString("buyVolume");
                        String sellPrice = itemObject.getJSONObject("quick_status").getString("sellPrice");
                        String sellVolume = itemObject.getJSONObject("quick_status").getString("sellVolume");
                        productItemList.add(new ProductItem(name, buyPrice, sellPrice, buyVolume, sellVolume));
                    }

                    setLayout(productItemList);
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

    private void setLayout(List<ProductItem> productItemList) {
        final ArrayAdapter<ProductItem> itemAdapter = new ArrayAdapter<ProductItem>(getApplicationContext(), R.layout.bazaar_item) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.bazaar_item, parent, false);
                }

                ProductItem productItem = getItem(position);

                LinearLayout bzInfo = convertView.findViewById(R.id.bzInfos);

                TextView bzItem = convertView.findViewById(R.id.bzItem);
                TextView bzBuyP = convertView.findViewById(R.id.bzBuyP);
                TextView bzBuyV = convertView.findViewById(R.id.bzBuyV);
                TextView bzSellP = convertView.findViewById(R.id.bzSellP);
                TextView bzSellV = convertView.findViewById(R.id.bzSellV);

                bzItem.setText(productItem.getItemName());
                bzBuyP.setText("Buy Prise: " + productItem.getBuyPrice());
                bzSellP.setText("Sell Prise: " + productItem.getSellPrice());
                bzBuyV.setText("Buy Volume: " + productItem.getBuyVolume());
                bzSellV.setText("Sell Volume: " + productItem.getSellVolume());

                bzInfo.setVisibility(View.GONE);

                progressBar.setVisibility(View.GONE);
                return convertView;
            }
        };

        itemAdapter.addAll(productItemList);
        itemList.setAdapter(itemAdapter);
    }

    private void generateAlertDialog(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}