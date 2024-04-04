package com.example.clientsellingmedicine.utils;

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
}
