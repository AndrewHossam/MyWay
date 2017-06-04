package com.AMDevelopers.myway.DES_Cryptography;

import java.math.BigInteger;

public class DES_Expansion {

    private int[] Expansion = {
        32, 1, 2, 3, 4, 5,
        4, 5, 6, 7, 8, 9,
        8, 9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };

    public String Expand(String Ri) {
        String BinaryInput = new BigInteger(Ri, 16).toString(2);
        if (BinaryInput.length() < 32) {
            int bits = 32 - BinaryInput.length();
            for (int i = 0; i < bits; i++) {
                BinaryInput = "0" + BinaryInput;
            }
        }
        String PermutedInput = "";
        for (int i = 0; i < Expansion.length; i++) {
            PermutedInput = PermutedInput + BinaryInput.charAt(Expansion[i] - 1);
        }
        return new BigInteger(PermutedInput, 2).toString(16);
    }
}
