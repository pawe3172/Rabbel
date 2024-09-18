import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class App {

    static long startTime = System.currentTimeMillis();

    public static void main(String[] args) throws Exception {
        char[][] board = {
                { 'd', 'i', 'a', 'r' },
                { 'd', 'e', 'i', 'g' },
                { 'l', 'a', 'e', 'a' },
                { 'r', 'v', 'i', 'n' }
        };

        Set<Character> charSet = new HashSet<>();
        for (char[] row : board) {
            for (char c : row) {
                charSet.add(c);
            }
        }

        ArrayList<String> filteredWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("rabbel.txt"))) {

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

        ArrayList<String> result = new ArrayList<>();
        for (String word : filteredWords) {
            if (exist(board, word) && !result.contains(word)) {
                result.add(word);
            }
        }
        Collections.sort(result);
        System.out.println(result);
        System.out.println(result.size());

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
