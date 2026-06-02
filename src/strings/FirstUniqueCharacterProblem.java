package strings;

import java.util.LinkedHashMap;
import java.util.Map;

public class FirstUniqueCharacterProblem {
    public static Character firstUniqueChar(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        Map<Character, Integer> counts = new LinkedHashMap<Character, Integer>();
        for (char c : input.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        return null;
    }
}