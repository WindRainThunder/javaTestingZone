package QA;

import java.util.*;
import java.util.stream.Collectors;

public class MostFrequentElement {
    public <T> List<T> mostFrequent(List<T> input) {
        if (input == null || input.isEmpty()) {
            return Collections.emptyList();
        }

        T best = null;
        int bestCount = 0;

        Map<T, Integer> counts = new HashMap<>();


        for (T item : input) {
            int count = counts.getOrDefault(item, 0) + 1;
            counts.put(item, count);

            if (count > bestCount) {
                best = item;
                bestCount = count;
            }
        }

        final int maxCount = bestCount;

        return counts.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }
}
