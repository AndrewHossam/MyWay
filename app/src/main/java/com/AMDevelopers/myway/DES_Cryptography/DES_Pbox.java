package com.AMDevelopers.myway.DES_Cryptography;

import java.math.BigInteger;

public class DES_Pbox {

    private int[] Pbox = {
        16, 7, 20, 21, 29, 12, 28, 17,
        1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9,
        19, 13, 30, 6, 22, 11, 4, 25
    };

    public String Pbox(String F) {
        String BinaryInput = new BigInteger(F, 16).toString(2);
        if (BinaryInput.length() < 32) {
            int bits = 32 - BinaryInput.length();
            for (int i = 0; i < bits; i++) {
                BinaryInput = "0" + BinaryInput;
            }
        }
        String PermutedInput = "";
        for (int i = 0; i < Pbox.length; i++) {
            PermutedInput = PermutedInput + BinaryInput.charAt(Pbox[i] - 1);
        }
        return new BigInteger(PermutedInput, 2).toString(16);
    }
}
