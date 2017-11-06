package pl.com.bottega.photostock.sales.misc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordsMap {

    public static Map<String, List<Integer>> wordsMap(String text) {
        Map<String, List<Integer>> map = new HashMap<>();
        int wordStartIndex = -1;
        text = text + ' ';
        for (int index = 0; index < text.length(); index++) {
            char c = text.charAt(index);
            if (Character.isWhitespace(c)) {
                if (wordStartIndex != -1) {
                    String word = text.substring(wordStartIndex, index);
                    putWord(map, word, wordStartIndex);
                    wordStartIndex = -1;
                }
            } else if (wordStartIndex == -1)
                wordStartIndex = index;
        }
        return map;
    }

    public static Map<String, List<Integer>> wordsMapV2(String text) {
        String[] words = text.split(" ");
        Map<String, List<Integer>> map = new HashMap<>();
        int wordIndex = 0;
        for (String word : words) {
            if (!word.isEmpty()) {
                wordIndex = text.indexOf(word, wordIndex);
                putWord(map, word, wordIndex);
                wordIndex += 1;
            }
        }
        return map;
    }

    private static void putWord(Map<String, List<Integer>> map, String word, int index) {
        List<Integer> currentIndexes = map.get(word);
        if (currentIndexes == null) {
            currentIndexes = new LinkedList<>();
            map.put(word, currentIndexes);
        }
        currentIndexes.add(index);
    }

    public static void main(String[] args) {
        System.out.println(wordsMap("ala ma kota ala"));
        System.out.println(wordsMap("   ala     ma kota ala   "));
        System.out.println(wordsMap("     "));
        System.out.println(wordsMap(""));

        System.out.println(wordsMapV2("ala ma kota ala"));
        System.out.println(wordsMapV2("   ala     ma kota ala   "));
        System.out.println(wordsMapV2("     "));
        System.out.println(wordsMapV2(""));
        System.out.println(wordsMapV2("aa aaa aa"));
    }

}
