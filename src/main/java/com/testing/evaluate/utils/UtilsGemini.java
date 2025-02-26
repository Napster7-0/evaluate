package com.testing.evaluate.utils;


import com.testing.evaluate.interfaces.U;
import java.util.ArrayList;
import java.util.List;

public class UtilsGemini implements U {

    @Override
    public float SquareRoot(float x) {
        if (x < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of a negative number.");
        }

        // Algorithme de Newton-Raphson pour une convergence rapide
        float guess = x / 2;
        float tolerance = 0.0001f; // Tolérance pour la précision

        while (Math.abs(guess * guess - x) > tolerance) {
            guess = (guess + x / guess) / 2;
        }

        return guess;
    }

    @Override
    public List<Float> SolveQuadraticEq(float a, float b, float c) {
        List<Float> solutions = new ArrayList<>();

        float delta = b * b - 4 * a * c;

        if (delta >= 0) {
            float sqrtDelta = SquareRoot(delta); // Réutilisation de notre fonction SquareRoot
            solutions.add((-b + sqrtDelta) / (2 * a));
            if (delta > 0) {
                solutions.add((-b - sqrtDelta) / (2 * a));
            }
        }

        return solutions;
    }
}