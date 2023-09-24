package ch.bbcag.skycraftsq.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import ch.bbcag.skycraftsq.R;

public class TransactionItem {
    private String action;
    private String amount;
    private long timestamp;

    private String initiatorName;

    public TransactionItem() {

    }

    public TransactionItem(String action, String amount, long timestamp, String initiatorName) {
        this.action = action;
        this.amount = formatPrice(Double.parseDouble(amount));
        this.timestamp = timestamp;
        this.initiatorName = initiatorName;
    }

    public String formatPrice(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String result = decimalFormat.format(value);;

        if (action.equals("WITHDRAW")) {
            result = "-" + result;
        }
        return result;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getInitiatorName() {
        return initiatorName;
    }
    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime dateTime =  LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Zurich"));
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;

        return dateTime.format(dtf).toString();
    }

    public String getName() {
        if (initiatorName.equals("Bank Interest")) {
            return initiatorName;
        }
        return initiatorName.substring(2);
    }

    public String getCoins() {
        return amount;
    }

    public int getIcon() {
        int id = 0;

        switch (action) {
            case "DEPOSIT": id = R.drawable.in_dark; break;
            case "WITHDRAW": id = R.drawable.out; break;
        }

        return id;
    }

    @Override
    public String toString() {
        return "amount: " + amount;
    }
}