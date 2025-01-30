import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class App {

    static long startTime = System.currentTimeMillis();
    public static final int BOARDSIZE = 4;

    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        char[][] board = new char[BOARDSIZE][BOARDSIZE];

        for (int i = 0; i < BOARDSIZE; i++) {
            System.out.print("Bokstäver för rad " + (i + 1) + ": ");
            String letters = "";

            while (true) {
                letters = input.next().toLowerCase().trim().replaceAll(",", "");
                if (letters.length() == BOARDSIZE && letters.matches("[a-zåäö]+")) {
                    break;
                }
                System.out.print("Ange endast " + BOARDSIZE + " antal bokstäver från a till ö: ");
            }

            char[] lettersAsChar = letters.toCharArray();
            for (int j = 0; j < BOARDSIZE; j++) {
                board[i][j] = lettersAsChar[j];
            }
        }

        Set<Character> charSet = new HashSet<>();
        for (char[] row : board) {
            for (char c : row) {
                charSet.add(c);
            }
        }

        ArrayList<String> filteredWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("wordlist.txt"))) {

            String text = br.readLine();
            String[] words = text.toLowerCase().split(",");
            for (String word : words) {
                word = word.trim().replace("\"", "").replace("[", "").replace("]", "");

                boolean matches = true;
                for (char c : word.toCharArray()) {
                    if (!charSet.contains(c)) {
                        matches = false;
                        break;
                    }
                }
                if ((word.length() > 2) && matches) {
                    filteredWords.add(word);
                }
            }
        }

        catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        TreeMap<Integer, TreeSet<String>> result = new TreeMap<>();
        for (String word : filteredWords) {
            if (exist(board, word)) {
                if (!result.containsKey(word.length()))
                    result.put(word.length(), new TreeSet<String>());
                result.get(word.length()).add(word);
            }
        }

        int antalOrd = 0;
        for (Map.Entry<Integer, TreeSet<String>> entry : result.entrySet()) {
            System.out.println(entry.getKey() + " bokstäver: " + entry.getValue());
            antalOrd += entry.getValue().size();
        }
        System.out.println("Antal ord: " + antalOrd);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(duration + "ms");

    }

    static boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        boolean[][] visited = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == word.charAt(0) && search(board, i, j, word, 0, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean search(char[][] board, int i, int j, String word, int index, boolean[][] visited) {
        if (index == word.length())
            return true;

        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != word.charAt(index)
                || visited[i][j]) {
            return false;
        }

        visited[i][j] = true;

        int[] dRow = { 0, 1, 0, -1, -1, 1, 1, -1 };
        int[] dCol = { -1, 0, 1, 0, -1, -1, 1, 1 };
        for (int k = 0; k < 8; k++) {
            if (search(board, i + dRow[k], j + dCol[k], word, index + 1, visited)) {
                return true;
            }
        }

        visited[i][j] = false;
        return false;
    }

}
