package com.company;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        // write your code here
        try {
            JsonObjects jsonObject = new JsonObjects();
            jsonObject.createKeyValuePair();
        } catch (Exception e) {
            e.printStackTrace();

        }


    }
}
