package pl.com.bottega.photostock.sales.misc;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WordsInFile {

    public static void main(String[] args) {
        String input = "/home/maciuch/tmp/test.txt";
        String output = "/home/maciuch/tmp/map.txt";
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
                PrintStream out = new PrintStream(new FileOutputStream(output));
        ) {
            process(in, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void process(BufferedReader in, PrintStream out) throws IOException {
        Map<String, Integer> map = mapWords(in);
        writeMap(out, map);
    }

    private static Map<String, Integer> mapWords(BufferedReader in) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        String line;
        while ((line = in.readLine()) != null) {
            line = line.toLowerCase();
            String[] words = line.split("\\s+");
            putIntoMap(words, map);
        }
        return map;
    }

    private static void putIntoMap(String[] words, Map<String, Integer> map) {
        for (String word : words) {
            int count = map.getOrDefault(word, 0);
            map.put(word, count + 1);
        }
    }

    private static void writeMap(PrintStream out, Map<String, Integer> map) {
        map.entrySet().forEach((e) -> {
            out.printf("%s %d", e.getKey(), e.getValue());
            out.println();
        });
    }

}
