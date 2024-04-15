package com.example.clientsellingmedicine.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {

    // convert Price  1000000 -> 1,000,000 đ
    public static String convertPrice(int number) {
        if(number == 0)
            return " 0 đ";
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number) + " đ";
    }

    // convert currency 1,000,000 đ -> 1000000
    public static Integer convertCurrencyFormat(String currency) {
        currency = currency.replace("đ", "");
        currency = currency.replace(".", "");

        return Integer.parseInt(currency.trim());
    }

    public static Date convertToDate(String inputDate) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat("MMM d, yyyy, hh:mm:ss a");
        try {
            return inputFormatter.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
