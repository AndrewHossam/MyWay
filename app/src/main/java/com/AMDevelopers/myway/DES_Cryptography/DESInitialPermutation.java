package com.AMDevelopers.myway.DES_Cryptography;

import java.math.BigInteger;

public class DESInitialPermutation {

    private String L0R0;

    private int[] InitialPermutation = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };

    public DESInitialPermutation(String Input) {
        String BinaryInput = new BigInteger(Input, 16).toString(2);
        if (BinaryInput.length() < 64) {
            int bits = 64 - BinaryInput.length();
            for (int i = 0; i < bits; i++) {
                BinaryInput = "0" + BinaryInput;
            }
        }
        String PermutedInput = "";
        for (int i = 0; i < InitialPermutation.length; i++) {
            PermutedInput = PermutedInput + BinaryInput.charAt(InitialPermutation[i] - 1);
        }
        L0R0 = new BigInteger(PermutedInput, 2).toString(16);
    }

    public String getL0R0() {
        return L0R0;
    }
}
