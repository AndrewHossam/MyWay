package com.AMDevelopers.myway;

import com.AMDevelopers.myway.DES_Cryptography.DES;

import java.math.BigInteger;

public class Encryption {

    public String Encrypt(String Data) {
        String PaddedData = "";
        for (int i = 0; i < Data.length(); i++) {
            for (int j = 0; j < 16; j++) {
                PaddedData = PaddedData + (char) (32 + ((int) (Math.random() * ((127 - 32) + 1))));
            }
            PaddedData = PaddedData + Data.charAt(i);
        }
        for (int i = 0; i < 16; i++) {
            PaddedData = PaddedData + (char) (32 + ((int) (Math.random() * ((127 - 32) + 1))));
        }
        String MidCipher = "";
        for (int i = 0; i < PaddedData.length(); i++) {
            MidCipher = MidCipher + Integer.toHexString((int) PaddedData.charAt(i));
        }
        String Cipher = CBC_Encryption(MidCipher);
        return Cipher;
    }

    private String CBC_Encryption(String Plain) {
        int pad = (16 - (Plain.length() % 16)) % 16;
        for (int i = 0; i < pad; i++) {
            Plain = "0" + Plain;
        }
        int Blocks = Plain.length() / 16;
        String[] P = new String[Blocks];
        String[] C = new String[Blocks];
        for (int i = 0; i < Blocks; i++) {
            P[i] = Plain.substring(0, 16);
            Plain = Plain.substring(16);
        }
        for (int i = 0; i < Blocks; i++) {
            if (i == 0) {
                String xor = (new BigInteger("9").xor(new BigInteger(P[0], 16))).toString(16);
                C[0] = new DES().Encryption(xor, "1234567890123456");
            } else {
                String xor = (new BigInteger(C[i - 1], 16).xor(new BigInteger(P[i], 16))).toString(16);
                C[i] = new DES().Encryption(xor, "1234567890123456");
            }
        }
        String Cipher = "";
        for (int i = 0; i < Blocks; i++) {
            if (C[i].length() < 16) {
                int bits = 16 - C[i].length();
                for (int j = 0; j < (16 - C[i].length()); j++) {
                    C[i] = "0" + C[i];
                }
            }
            Cipher = Cipher + C[i];
        }
        return Cipher;
    }
}
