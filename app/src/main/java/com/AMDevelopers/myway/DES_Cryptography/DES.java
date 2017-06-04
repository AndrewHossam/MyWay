package com.AMDevelopers.myway.DES_Cryptography;

import java.math.BigInteger;
import java.util.ArrayList;

public class DES {

    public String Encryption(String PlainText, String Key) {
        String L, R, F;
        ArrayList<String> SubKeys = new DES_SubKeys(Key).getSubKeys();
        String InterPlainText = new DESInitialPermutation(PlainText).getL0R0();
        if (InterPlainText.length() < 16) {
            int bits = 16 - InterPlainText.length();
            for (int i = 0; i < bits; i++) {
                InterPlainText = "0" + InterPlainText;
            }
        }
        L = InterPlainText.substring(0, 8);
        R = InterPlainText.substring(8);
        for (int r = 1; r <= 16; r++) {
            String Temp = R;
            R = new DES_Expansion().Expand(R);
            F = (new BigInteger(R, 16).xor(new BigInteger(SubKeys.get(r - 1), 16))).toString(16);
            F = new DES_Sbox().Sbox(F);
            F = new DES_Pbox().Pbox(F);
            R = (new BigInteger(L, 16).xor(new BigInteger(F, 16))).toString(16);
            L = Temp;
        }
        if (L.length() < 8) {
            int bits = 8 - L.length();
            for (int i = 0; i < bits; i++) {
                L = "0" + L;
            }
        }
        if (R.length() < 8) {
            int bits = 8 - R.length();
            for (int i = 0; i < bits; i++) {
                R = "0" + R;
            }
        }
        String CipherText = new DESFinalPermutation().getCipherText(R + L);
        return CipherText;
    }

    public String Decryption(String CipherText, String Key) {
        String L, R, F;
        ArrayList<String> SubKeys = new DES_SubKeys(Key).getSubKeys();
        String InterPlainText = new DESInitialPermutation(CipherText).getL0R0();
        if (InterPlainText.length() < 16) {
            int bits = 16 - InterPlainText.length();
            for (int i = 0; i < bits; i++) {
                InterPlainText = "0" + InterPlainText;
            }
        }
        L = InterPlainText.substring(0, 8);
        R = InterPlainText.substring(8);
        for (int r = 1; r <= 16; r++) {
            String Temp = R;
            R = new DES_Expansion().Expand(R);
            F = (new BigInteger(R, 16).xor(new BigInteger(SubKeys.get(15 - (r - 1)), 16))).toString(16);
            F = new DES_Sbox().Sbox(F);
            F = new DES_Pbox().Pbox(F);
            R = (new BigInteger(L, 16).xor(new BigInteger(F, 16))).toString(16);
            L = Temp;
        }
        if (L.length() < 8) {
            int bits = 8 - L.length();
            for (int i = 0; i < bits; i++) {
                L = "0" + L;
            }
        }
        if (R.length() < 8) {
            int bits = 8 - R.length();
            for (int i = 0; i < bits; i++) {
                R = "0" + R;
            }
        }
        String PlainText = new DESFinalPermutation().getCipherText(R + L);
        return PlainText;
    }
}
