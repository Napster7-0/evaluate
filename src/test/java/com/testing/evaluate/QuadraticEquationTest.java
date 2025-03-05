package com.testing.evaluate;

import com.testing.evaluate.interfaces.U;
import com.testing.evaluate.utils.QuadraticTestCase;
import com.testing.evaluate.utils.UtilsDeepseek;
import com.testing.evaluate.utils.UtilsGPT;
import com.testing.evaluate.utils.UtilsGemini;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class QuadraticEquationTest {

    // Définition des valeurs concrètes pour chaque catégorie
    private static final float VERY_LARGE_POSITIVE = 1.0E10F;  // a
    private static final float POSITIVE_REAL = 5.0F;           // b
    private static final float NEAR_ZERO_POSITIVE = 0.0001F;   // c
    private static final float ZERO = 0.0F;                    // d
    private static final float NEAR_ZERO_NEGATIVE = (float) -0.0001;  // e
    private static final float NEGATIVE_REAL = (float) -5.0;          // f
    private static final float VERY_LARGE_NEGATIVE = (float) -1.0E10; // g
    private final U utils = new UtilsGPT();

    /**
     * Convertit une combinaison de codes (1a, 2g, 3d) en valeurs concrètes
     */
    private static float mapCodeToValue(String code) {
        char valueCode = code.charAt(1);
        switch (valueCode) {
            case 'a': return VERY_LARGE_POSITIVE;
            case 'b': return POSITIVE_REAL;
            case 'c': return NEAR_ZERO_POSITIVE;
            case 'd': return ZERO;
            case 'e': return NEAR_ZERO_NEGATIVE;
            case 'f': return NEGATIVE_REAL;
            case 'g': return VERY_LARGE_NEGATIVE;
            default: throw new IllegalArgumentException("Code invalide: " + code);
        }
    }

    /**
     * Génère les cas de test à partir du fichier jenny_DE.txt
     * Essaie plusieurs emplacements pour trouver le fichier
     */
    static Stream<QuadraticTestCase> generateTestCases() {
        List<QuadraticTestCase> testCases = new ArrayList<>();

        // Liste des emplacements possibles à essayer
        String[] possiblePaths = {
                "./jenny_DE.txt",                           // Racine du projet
                "./src/test/resources/jenny_DE.txt",        // Répertoire des ressources de test
                "./target/test-classes/jenny_DE.txt"        // Répertoire de sortie de compilation
        };

        // Essayer chaque emplacement
        boolean fileFound = false;
        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                fileFound = true;
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    readTestCases(reader, testCases);
                } catch (IOException e) {
                    System.err.println("Erreur lors de la lecture du fichier " + path + ": " + e.getMessage());
                }
                break;
            }
        }

        // Si le fichier n'est pas trouvé, essayer de le charger depuis le classpath
        if (!fileFound) {
            try (InputStream is = QuadraticEquationTest.class.getResourceAsStream("/jenny_DE.txt")) {
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    readTestCases(reader, testCases);
                } else {
                    System.err.println("ATTENTION: Fichier jenny_DE.txt non trouvé dans aucun emplacement connu.");
                    System.err.println("Veuillez placer le fichier dans l'un des emplacements suivants:");
                    for (String path : possiblePaths) {
                        System.err.println("- " + path);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la lecture du fichier depuis le classpath: " + e.getMessage());
            }
        }

        return testCases.stream();
    }

    /**
     * Lit les cas de test à partir d'un BufferedReader
     */
    private static void readTestCases(BufferedReader reader, List<QuadraticTestCase> testCases) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            if (parts.length >= 3) {
                String aCode = parts[0];
                String bCode = parts[1];
                String cCode = parts[2];

                float a = mapCodeToValue(aCode);
                float b = mapCodeToValue(bCode);
                float c = mapCodeToValue(cCode);

                String description = String.format("%s %s %s", aCode, bCode, cCode);
                testCases.add(new QuadraticTestCase(a, b, c, description));
            }
        }
    }

    /**
     * Test paramétré qui prend les triplets générés et vérifie
     * que la résolution de l'équation quadratique est correcte
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("generateTestCases")
    @DisplayName("Test des combinaisons d'équations quadratiques")
    void testQuadraticEquation(QuadraticTestCase testCase) {
        float a = testCase.getA();
        float b = testCase.getB();
        float c = testCase.getC();

        List<Float> roots = utils.SolveQuadraticEq(a, b, c);

        // Vérification des racines
        if (roots != null) {
            for (float root : roots) {
                // Évaluer l'équation avec la racine trouvée
                float result = a * root * root + b * root + c;
                // La valeur doit être proche de zéro
                assertEquals(0.0, result, 1.0E-4,
                        String.format("La valeur %.4g n'est pas une racine valide pour a=%.4g, b=%.4g, c=%.4g",
                                root, a, b, c));
            }
        } else {
            // Vérifier que le discriminant est bien négatif
            float delta = b * b - 4 * a * c;
            assertTrue(delta < 0,
                    String.format("Pas de racines trouvées mais le discriminant %.4g n'est pas négatif pour a=%.4g, b=%.4g, c=%.4g",
                            delta, a, b, c));
        }
    }

    /**
     * Test avec des cas particuliers connus
     */
    @ParameterizedTest(name = "Équation: {0}x² + {1}x + {2} = 0")
    @DisplayName("Test de cas particuliers")
    @CsvFileSource(resources = "/special_cases.csv", numLinesToSkip = 1)
    void testSpecialCases(float a, float b, float c, int expectedRoots) {
        List<Float> roots = utils.SolveQuadraticEq(a, b, c);

        if (expectedRoots == 0) {
            assertNull(roots, "L'équation ne devrait pas avoir de racines réelles");
        } else {
            assertNotNull(roots, "L'équation devrait avoir au moins une racine réelle");
            assertEquals(expectedRoots, roots.size(),
                    String.format("L'équation devrait avoir %d racine(s)", expectedRoots));

            for (float root : roots) {
                float result = a * root * root + b * root + c;
                assertEquals(0.0, result, 1.0E-4,
                        String.format("La valeur %.4g n'est pas une racine valide", root));
            }
        }
    }
}