package com.testing.evaluate;

import com.testing.evaluate.interfaces.U;
import com.testing.evaluate.utils.UtilsDeepseek;
import com.testing.evaluate.utils.UtilsGPT;
import com.testing.evaluate.utils.UtilsGemini;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UtilsSquareRootTests {
    // Remplacez cette ligne par l'implémentation que nous souhaitons tester
    // Par exemple: private final U utils = new UtilsDeepseek();
    private final U utils = new UtilsGemini();
    private static final float TOLERANCE = 0.0001F;

    @Test
    @DisplayName("Calcule correctement la racine carrée d'un nombre positif parfait (4.0)")
    void testSquareRootOfPerfectSquare() {
        float input = 4.0F;
        float expected = 2.0F;
        assertEquals(expected, utils.SquareRoot(input), TOLERANCE,
                "La racine carrée de " + input + " devrait être " + expected);
    }

    @Test
    @DisplayName("Calcule correctement la racine carrée d'un nombre positif standard (7.0)")
    void testSquareRootOfStandardPositiveNumber() {
        float input = 7.0F;
        float expected = 7.0F;
        float sqrt = utils.SquareRoot(input);
        assertEquals(expected, sqrt*sqrt, TOLERANCE,
                "La valeur d'entrée " + input + " devrait rester " + expected);
    }

    @Test
    @DisplayName("Calcule correctement la racine carrée d'un nombre décimal (2.25)")
    void testSquareRootOfDecimalNumber() {
        float input = 2.25F;
        float expected = 2.25F;
        float sqrt = utils.SquareRoot(input);
        assertEquals(expected, sqrt*sqrt, TOLERANCE,
                "La valeur d'entrée " + input + " devrait rester " + expected);
    }

    @Test
    @DisplayName("Gère correctement les nombres très petits (0.0001)")
    void testSquareRootOfVerySmallNumber() {
        float input = 0.0001F;
        float expected = 0.01F;
        assertEquals(expected, utils.SquareRoot(input), TOLERANCE,
                "La racine carrée de " + input + " devrait être " + expected);
    }

    @Test
    @DisplayName("Gère correctement les grands nombres positifs (1.0E10)")
    void testSquareRootOfLargePositiveNumber() {
        float input = 1.0E10F;
        float expected = 1.0E5F;
        assertEquals(expected, utils.SquareRoot(input), TOLERANCE,
                "La racine carrée de " + input + " devrait être " + expected);
    }

    @Test
    @DisplayName("Gère correctement le cas limite zéro (0)")
    void testSquareRootOfZero() {
        float input = 0.0F;
        float expected = 0.0F;
        assertEquals(expected, utils.SquareRoot(input), TOLERANCE,
                "La valeur d'entrée " + input + " devrait rester " + expected);
    }

    @Test
    @DisplayName("Gère correctement un nombre très proche de zéro positif (1.0E-10)")
    void testSquareRootOfNumberCloseToZero() {
        float input = 1.0E-10F;
        float expected = 1.0E-5F;
        assertEquals(expected, utils.SquareRoot(input), TOLERANCE,
                "La racine carrée de " + input + " devrait être " + expected);
    }

    @Test
    @DisplayName("Gère correctement un nombre très proche de zéro négatif (-1.0E-10)")
    void testSquareRootOfNegativeNumberCloseToZero() {
        float input = -1.0E-10F;
        assertThrows(IllegalArgumentException.class, () -> utils.SquareRoot(input),
                "La racine carrée d'un nombre négatif devrait lancer une exception");
    }

    @Test
    @DisplayName("Gère correctement un nombre très grand négatif (-1.0E10)")
    void testSquareRootOfLargeNegativeNumber() {
        float input = -1.0E10F;
        assertThrows(IllegalArgumentException.class, () -> utils.SquareRoot(input),
                "La racine carrée d'un nombre négatif devrait lancer une exception");
    }



    // Version paramétrisée des tests ci-dessus
    @ParameterizedTest(name = "Racine carrée de {0} devrait être {1}")
    @CsvSource({
            "4.0, 2.0",
            "7.0, 2.6457513110645907",  // Correction de la valeur attendue
            "2.25, 1.5",                // Correction de la valeur attendue
            "0.0001, 0.01",
            "1.0E10, 1.0E5",
            "0.0, 0.0",
            "1.0E-10, 1.0E-5"
    })
    void testSquareRootWithVariousInputs(float input, float expected) {
        assertEquals(expected, utils.SquareRoot(input), TOLERANCE,
                "La racine carrée de " + input + " devrait être " + expected);
    }
}