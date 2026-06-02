package strings;

public class PalindromeProblem {
    public static boolean isPalindrome(String input) {
        if (input == null) {
            return false;
        }

        int left = 0;
        int right = input.length() - 1;

        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(input.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(input.charAt(right))) {
                right--;
            }

            char leftChar = Character.toLowerCase(input.charAt(left));
            char rightChar = Character.toLowerCase(input.charAt(right));

            if (leftChar != rightChar) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }
}
