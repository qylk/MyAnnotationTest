package com.example.anotation;

import com.example.anotation.interfaces.Factory;
import com.example.anotation.interfaces.Meal;

@Factory(id = "Margherita", type = Meal.class)
public class MargheritaPizza implements Meal {
    @Override
    public float getPrice() {
        return 6f;
    }
}