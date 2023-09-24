package ch.bbcag.skycraftsq.model;

import java.text.DecimalFormat;

public class ProductItem {
    private String itemName;
    private String buyVolume;
    private String sellVolume;
    private String buyPrice;
    private String sellPrice;

    public ProductItem() {

    }

    public ProductItem(String itemName, String buyPrice, String sellPrice, String buyVolume, String sellVolume) {
        this.itemName = formatString(itemName);
        this.buyPrice = formatPrice(Double.parseDouble(buyPrice));
        this.sellPrice = formatPrice(Double.parseDouble(sellPrice));
        this.buyVolume = buyVolume;
        this.sellVolume = sellVolume;
    }

    public String formatString(String str) {
        String[] words = str.toLowerCase().split("_");
        StringBuilder output = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
                output.append(capitalizedWord).append(" ");
            }
        }

        return output.toString().trim();
    }

    public String formatPrice(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(value);
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBuyVolume() {
        return buyVolume;
    }
    public void setBuyVolume(String buyVolume) {
        this.buyVolume = buyVolume;
    }

    public String getBuyPrice() {
        return buyPrice;
    }
    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }
    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSellVolume() {
        return sellVolume;
    }
    public void setSellVolume(String sellVolume) {
        this.sellVolume = sellVolume;
    }
}
