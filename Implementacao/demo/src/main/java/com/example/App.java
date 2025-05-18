package com.example;

import com.example.view.MainView;

public class App {
    public static void main(String[] args) {
        try {
            MainView.menu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
