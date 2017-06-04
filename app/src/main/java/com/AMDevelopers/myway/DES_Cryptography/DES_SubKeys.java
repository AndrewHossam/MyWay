package com.AMDevelopers.myway.DES_Cryptography;

import java.math.BigInteger;
import java.util.ArrayList;

public class DES_SubKeys {

    private ArrayList<String> SubKeys;

    private int[] KeyPermutation = {
        57, 49, 41, 33, 25, 17, 9,
        1, 58, 50, 42, 34, 26, 18,
        10, 2, 59, 51, 43, 35, 27,
        19, 11, 3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7, 62, 54, 46, 38, 30, 22,
        14, 6, 61, 53, 45, 37, 29,
        21, 13, 5, 28, 20, 12, 4
    };

    private int[] SubKeysPermutation = {
        14, 17, 11, 24, 1, 5,
        3, 28, 15, 6, 21, 10,
        23, 19, 12, 4, 26, 8,
        16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    public DES_SubKeys(String Key) {
        SubKeys = new ArrayList<>();
        String BinaryKey = new BigInteger(Key, 16).toString(2);
        if (BinaryKey.length() < 64) {
            int bits = 64 - BinaryKey.length();
            for (int i = 0; i < bits; i++) {
                BinaryKey = "0" + BinaryKey;
            }
        }
        String DESKey = "";
        for (int i = 0; i < KeyPermutation.length; i++) {
            DESKey = DESKey + BinaryKey.charAt(KeyPermutation[i] - 1);
        }
        String RoundKey = DESKey;
        String SubKey;
        for (int r = 1; r <= 16; r++) {
            StringBuilder LK = new StringBuilder(RoundKey.substring(0, 28));
            StringBuilder RK = new StringBuilder(RoundKey.substring(28));
            char MSB = LK.charAt(0);
            for (int i = 0; i < LK.length(); i++) {
                if (i == LK.length() - 1) {
                    LK.setCharAt(i, MSB);
                    break;
                }
                LK.setCharAt(i, LK.charAt(i + 1));
            }
            MSB = RK.charAt(0);
            for (int i = 0; i < RK.length(); i++) {
                if (i == RK.length() - 1) {
                    RK.setCharAt(i, MSB);
                    break;
                }
                RK.setCharAt(i, RK.charAt(i + 1));
            }
            if (r != 1 && r != 2 && r != 9 && r != 16) {
                MSB = LK.charAt(0);
                for (int i = 0; i < LK.length(); i++) {
                    if (i == LK.length() - 1) {
                        LK.setCharAt(i, MSB);
                        break;
                    }
                    LK.setCharAt(i, LK.charAt(i + 1));
                }
                MSB = RK.charAt(0);
                for (int i = 0; i < RK.length(); i++) {
                    if (i == RK.length() - 1) {
                        RK.setCharAt(i, MSB);
                        break;
                    }
                    RK.setCharAt(i, RK.charAt(i + 1));
                }
            }
            RoundKey = LK.toString() + RK.toString();
            SubKey = "";
            for (int i = 0; i < SubKeysPermutation.length; i++) {
                SubKey = SubKey + RoundKey.charAt(SubKeysPermutation[i] - 1);
            }
            SubKeys.add(new BigInteger(SubKey, 2).toString(16));
        }
    }

    public ArrayList<String> getSubKeys() {
        return SubKeys;
    }
}
