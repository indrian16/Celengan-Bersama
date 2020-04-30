package io.indrian.celenganbersama.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RupiahCurrencyUtils {

    public static String toRupiah(Double amount) {

        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols rupiahSymbol = new DecimalFormatSymbols();
        rupiahSymbol.setCurrencySymbol("Rp. ");
        rupiahSymbol.setMonetaryDecimalSeparator(',');
        rupiahSymbol.setGroupingSeparator('.');

        decimalFormat.setDecimalFormatSymbols(rupiahSymbol);
        return decimalFormat.format(amount);
    }
}
