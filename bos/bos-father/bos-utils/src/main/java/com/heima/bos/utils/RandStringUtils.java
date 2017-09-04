package com.heima.bos.utils;

import java.util.Random;

public class RandStringUtils {

    public static String getCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(getRandomString());
        }
        return sb.toString();
    }

    public static int getRandomString() {
        int num = new Random().nextInt(9);
        return num;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(getCode());
        }
    }
}
