package com.example.anotation;


import com.example.annotation.MealFactory;

public class Test {
    public static void main(String[] args) {
        System.out.println(new MealFactory().create("Tiramisu").getPrice());
    }
}
