package com.example.anotation;

import com.example.anotation.interfaces.Factory;
import com.example.anotation.interfaces.Meal;

@Factory(id = "Tiramisu", type = Meal.class)
public class Tiramisu implements Meal {
    @Override
    public float getPrice() {
        return 4.5f;
    }
}