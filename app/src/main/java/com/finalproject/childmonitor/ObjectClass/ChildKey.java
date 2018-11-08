package com.finalproject.childmonitor.ObjectClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChildKey {

    public static List<String> childKeyList = new ArrayList<>();
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ2345678901";

    public static String getToken() {
        StringBuilder token = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }
}
