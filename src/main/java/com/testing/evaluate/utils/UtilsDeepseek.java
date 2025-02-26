package com.testing.evaluate.utils;

import com.testing.evaluate.interfaces.U;
import java.util.ArrayList;
import java.util.List;

public class UtilsDeepseek implements U {

    @Override
    public float SquareRoot(float x) {
        if (x < 0) {
            throw new IllegalArgumentException("x must be non-negative");
        }
        if (x == 0) {
            return 0;
        }

        float guess = x / 2.0f;
        final float tolerance = 1e-6f;
        float absoluteTolerance = tolerance * Math.abs(x);

        // Utilisation de la méthode Newton-Raphson pour la racine carrée
        while (Math.abs(guess * guess - x) > absoluteTolerance) {
            guess = (guess + x / guess) / 2.0f;
        }

        return guess;
    }

    @Override
    public List<Float> SolveQuadraticEq(float a, float b, float c) {
        List<Float> solutions = new ArrayList<>();

        if (a == 0) {
            // Équation linéaire: bx + c = 0
            if (b == 0) {
                if (c == 0) {
                    // Infinité de solutions (liste vide comme convention)
                } else {
                    // Aucune solution
                }
            } else {
                solutions.add(-c / b);
            }
        } else {
            // Équation quadratique: ax² + bx + c = 0
            float delta = b * b - 4 * a * c;

            if (delta < 0) {
                // Aucune solution réelle
            } else if (delta == 0) {
                // Une solution réelle
                solutions.add(-b / (2 * a));
            } else {
                // Deux solutions réelles avec calcul optimisé
                float sqrtDelta = SquareRoot(delta);
                float q;

                if (b > 0) {
                    q = -0.5f * (b + sqrtDelta);
                } else {
                    q = -0.5f * (b - sqrtDelta);
                }

                solutions.add(q / a);
                solutions.add(c / q);
            }
        }

        return solutions;
    }
}