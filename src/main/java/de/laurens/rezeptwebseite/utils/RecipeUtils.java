package de.laurens.rezeptwebseite.utils;

import java.util.Random;

public class RecipeUtils {

    public static String createRandomRecipeID(int length) {
        char[] letters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toCharArray();
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        int max = letters.length;
        int min = 0;
        for (int i = 0; i < length; i++) {
            result.append(getRandom(letters));
        }
        return result.toString();
    }

    private static char getRandom(char[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


}
