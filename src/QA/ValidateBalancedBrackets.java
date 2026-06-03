package QA;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class ValidateBalancedBrackets {
    public static boolean isValid(String input) {
        if (input == null) {
            return false;
        }

        Map<Character, Character> pairs = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );

        Deque<Character> stack = new ArrayDeque<>();

        for (char c : input.toCharArray()) {
            if (pairs.containsValue(c)) {
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }
}
