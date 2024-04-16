package com.example.clientsellingmedicine.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertToDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH);

        try {
            Date date = inputFormat.parse(inputDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date string";
        }
    }
}
