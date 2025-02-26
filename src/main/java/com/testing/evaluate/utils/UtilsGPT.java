package com.testing.evaluate.utils;

import com.testing.evaluate.interfaces.U;
import java.util.ArrayList;
import java.util.List;

public class UtilsGPT implements U {

    @Override
    public float SquareRoot(float x) {
        if (x < 0) {
            throw new IllegalArgumentException("Le nombre doit être positif");
        }

        if (x == 0) {
            return 0;
        }

        float epsilon = 1e-6f;
        float guess = x;

        while (Math.abs(guess * guess - x) > epsilon) {
            guess = (guess + x / guess) / 2;
        }

        return guess;
    }

    @Override
    public List<Float> SolveQuadraticEq(float a, float b, float c) {
        List<Float> solutions = new ArrayList<>();

        if (a == 0) {
            if (b != 0) {
                solutions.add(-c / b);
            }
            return solutions;
        }

        float discriminant = b * b - 4 * a * c;

        if (discriminant > 0) {
            float sqrtDiscriminant = SquareRoot(discriminant);
            solutions.add((-b + sqrtDiscriminant) / (2 * a));
            solutions.add((-b - sqrtDiscriminant) / (2 * a));
        } else if (discriminant == 0) {
            solutions.add(-b / (2 * a));
        }

        return solutions;
    }
}
