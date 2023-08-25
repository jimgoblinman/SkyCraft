package ch.bbcag.skycraftsq;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import ch.bbcag.skycraftsq.model.BankProfile;
import ch.bbcag.skycraftsq.model.TransactionItem;

public class BankTransactions extends AppCompatActivity {
    private List<TransactionItem> transactionList = new ArrayList<>();

    ImageView back;
    TextView title;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transactions);

        back = findViewById(R.id.transactionBack);
        title = findViewById(R.id.bankTitle);
        listView = findViewById(R.id.transactionList);

        Intent intent = getIntent();
        //Log.e("Testing", intent.getStringExtra("KEY_PROFILE_ID"));
        title.setText("Profile ");
        String getTransactions = intent.getStringExtra("KEY_TRANSACTIONS");

        Gson gson = new Gson();
        TransactionItem[] transactions = gson.fromJson(getTransactions, TransactionItem[].class);
        transactionList = Arrays.asList(transactions);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTransactions();
    }

    public void setTransactions() {
        final ArrayAdapter<TransactionItem> bankAdapter = new ArrayAdapter<TransactionItem>(getApplicationContext(), R.layout.transaction_item) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);
                }

                TransactionItem transactionItem = getItem(position);

                TextView bIDate = convertView.findViewById(R.id.bIDate);
                TextView bIName = convertView.findViewById(R.id.bIName);
                TextView bICoins = convertView.findViewById(R.id.bICoins);
                TextView bIAction = convertView.findViewById(R.id.bIAction);
                ImageView bIIcon = convertView.findViewById(R.id.bIIcon);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    bIDate.setText(transactionItem.getDate());
                }
                bIName.setText(transactionItem.getName());
                bICoins.setText(String.valueOf(transactionItem.getCoins()));
                bIAction.setText(transactionItem.getAction());
                bIIcon.setImageResource(transactionItem.getIcon());

                if (transactionItem.getCoins().substring(0,1).equals("-")) {
                    bICoins.setTextColor(Color.RED);
                }

                return convertView;
            }
        };

        bankAdapter.addAll(transactionList);
        listView.setAdapter(bankAdapter);
    }
}