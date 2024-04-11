package com.example.clientsellingmedicine.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {
    public static String convertPrice(double number) {
        if(number == 0)
            return " 0 đ";
        long integerPart = (long) number;
        int decimalPart = (int) ((number - integerPart) * 1000);

        String formattedIntegerPart = String.format("%,d", integerPart).replace(",", ".");
        String formattedDecimalPart = String.format("%03d", decimalPart);

        return formattedIntegerPart + "." + formattedDecimalPart + " đ";
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
