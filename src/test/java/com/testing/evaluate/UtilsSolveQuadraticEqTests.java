package com.testing.evaluate;

import com.testing.evaluate.interfaces.U;
import com.testing.evaluate.utils.UtilsDeepseek;
import com.testing.evaluate.utils.UtilsGPT;
import com.testing.evaluate.utils.UtilsGemini;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class UtilsSolveQuadraticEqTests {
    // Remplacez cette ligne par l'implémentation que vous souhaitez tester
    private final U utils = new UtilsGPT();
    private static final double TOLERANCE = 0.0001;
    @Test
    @DisplayName("Cas où Δ > 0 : équation avec deux solutions distinctes")
    void testQuadraticEquationWithTwoDistinctSolutions() {
        // x² - 3x + 2 = 0, solutions: x = 1 et x = 2
        float a = 1f;
        float b = -3f;
        float c = 2f;

        List<Float> solutions = utils.SolveQuadraticEq(a, b, c);

        assertEquals(2, solutions.size(), "L'équation devrait avoir exactement deux solutions");
        assertTrue(
                (Math.abs(solutions.get(0) - 1.0) < TOLERANCE && Math.abs(solutions.get(1) - 2.0) < TOLERANCE) ||
                        (Math.abs(solutions.get(0) - 2.0) < TOLERANCE && Math.abs(solutions.get(1) - 1.0) < TOLERANCE),
                "Les solutions devraient être 1.0 et 2.0"
        );
    }

    @Test
    @DisplayName("Cas où Δ = 0 : équation avec une solution double")
    void testQuadraticEquationWithOneDoubleSolution() {
        // x² - 4x + 4 = 0, solution: x = 2 (double)
        float a = 1f;
        float b = -4f;
        float c = 4f;

        List<Float> solutions = utils.SolveQuadraticEq(a, b, c);

        assertEquals(1, solutions.size(), "L'équation devrait avoir exactement une solution (double)");
        assertEquals(2.0, solutions.get(0), TOLERANCE, "La solution devrait être 2.0");
    }

    @Test
    @DisplayName("Cas où Δ < 0 : équation sans solution réelle")
    void testQuadraticEquationWithNoRealSolution() {
        // x² + x + 1 = 0, pas de solution réelle
        float a = 1f;
        float b = 1f;
        float c = 1f;

        // Selon l'implémentation, cela peut retourner une liste vide ou lancer une exception
        try {
            List<Float> solutions = utils.SolveQuadraticEq(a, b, c);
            assertTrue(solutions.isEmpty(), "La liste de solutions devrait être vide");
        } catch (IllegalArgumentException e) {
            // C'est également acceptable si l'implémentation choisit de lancer une exception
            assertTrue(true, "L'exception est une réponse valide pour Δ < 0");
        }
    }

    @Test
    @DisplayName("Cas où 'a' est nul mais 'b' n'est pas nul (équation linéaire)")
    void testLinearEquation() {
        // 0x² + 2x - 4 = 0, solution: x = 2
        float a = 0f;
        float b = 2f;
        float c = -4f;

        List<Float> solutions = utils.SolveQuadraticEq(a, b, c);

        assertEquals(1, solutions.size(), "L'équation linéaire devrait avoir une solution");
        assertEquals(2.0, solutions.get(0), TOLERANCE, "La solution devrait être 2.0");
    }

    @Test
    @DisplayName("Cas où 'a' et 'b' sont nuls mais 'c' n'est pas nul (pas de solution)")
    void testNoSolutionWhenAAndBZero() {
        // 0x² + 0x + 5 = 0, pas de solution
        float a = 0f;
        float b = 0f;
        float c = 5f;

        // Cette situation devrait lancer une exception
        assertThrows(IllegalArgumentException.class, () -> {
            utils.SolveQuadraticEq(a, b, c);
        }, "Devrait lancer une exception car 0 = 5 est impossible");
    }

    @Test
    @DisplayName("Cas où 'a', 'b' et 'c' sont tous nuls (infinité de solutions)")
    void testInfiniteSolutionsWhenAllCoefficientsZero() {
        // 0x² + 0x + 0 = 0, infinité de solutions
        float a = 0f;
        float b = 0f;
        float c = 0f;

        // Selon l'implémentation, elle pourrait:
        // 1. Retourner une liste spéciale (ex. null ou une liste avec une valeur spéciale)
        // 2. Lancer une exception particulière
        try {
            List<Float> solutions = utils.SolveQuadraticEq(a, b, c);
            // Si l'implémentation retourne une liste, vérifiez si c'est une réponse valide selon votre API
            assertNotNull(solutions, "La méthode ne devrait pas retourner null");
        } catch (IllegalArgumentException e) {
            // C'est également acceptable si l'implémentation choisit de lancer une exception
            assertTrue(true, "L'exception est une réponse valide pour le cas d'infinité de solutions");
        }
    }

    @Test
    @DisplayName("Cas avec des coefficients à virgule flottante")
    void testQuadraticEquationWithFloatingPointCoefficients() {
        // 2.5x² - 7.5x + 5.0 = 0, solutions: x = 1.0 et x = 2.0
        float a = 2.5f;
        float b = -7.5f;
        float c = 5.0f;

        List<Float> solutions = utils.SolveQuadraticEq(a, b, c);

        assertEquals(2, solutions.size(), "L'équation devrait avoir exactement deux solutions");
        assertTrue(
                (Math.abs(solutions.get(0) - 1.0) < TOLERANCE && Math.abs(solutions.get(1) - 2.0) < TOLERANCE) ||
                        (Math.abs(solutions.get(0) - 2.0) < TOLERANCE && Math.abs(solutions.get(1) - 1.0) < TOLERANCE),
                "Les solutions devraient être 1.0 et 2.0"
        );
    }

    @Test
    @DisplayName("Cas avec des valeurs extrêmes")
    void testQuadraticEquationWithExtremeValues() {
        // 0.00001x² + 10000x - 0.5 = 0
        float a = 0.00001f;
        float b = 10000f;
        float c = -0.5f;

        List<Float> solutions = utils.SolveQuadraticEq(a, b, c);

        assertEquals(2, solutions.size(), "L'équation devrait avoir exactement deux solutions");
        // Une solution sera très proche de 0 et l'autre sera très grande négative
        boolean hasSmallPositiveSolution = false;
        boolean hasLargeNegativeSolution = false;

        for (Float solution : solutions) {
            if (Math.abs(solution - 0.00005) < 0.00001) {
                hasSmallPositiveSolution = true;
            }
            if (solution < -1000000000) {
                hasLargeNegativeSolution = true;
            }
        }

        assertTrue(hasSmallPositiveSolution && hasLargeNegativeSolution,
                "Les solutions devraient inclure une très petite valeur positive et une très grande valeur négative");
    }
}