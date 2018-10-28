package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;
import lesson1.JavaTasks;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */

    // T = O(firs.length() * second.length()), R = O(firs.length() * second.length())
    static public String longestCommonSubstring(String firs, String second) {
        int[][] table = new int[firs.length()][second.length()];
        int longest = 0;
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < firs.length(); i++) {
            for (int j = 0; j < second.length(); j++) {
                if (firs.charAt(i) != second.charAt(j)) {
                    continue;
                }

                table[i][j] = (i == 0 || j == 0) ? 1
                        : 1 + table[i - 1][j - 1];
                if (table[i][j] > longest) {
                    longest = table[i][j];
                    result.clear();
                }
                if (table[i][j] == longest) {
                    result.add(firs.substring(i - longest + 1, i + 1));
                }
            }
        }
        return result.isEmpty() ? "" : result.get(0);
    }

    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    static public int calcPrimesNumber(int limit) {
        throw new NotImplementedError();
    }

    /**
     * Балда
     * Сложная
     * <p>
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     * <p>
     * И Т Ы Н
     * К Р А Н
     * А К В А
     * <p>
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     * <p>
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     * <p>
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     * <p>
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */

    // T = O(N^2), R = O(N)
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        for (String word : words) {
            if (!word.matches("[A-Z]+|[А-Я]+")) throw new IllegalArgumentException("wrong format");
        }
        TreeSet<String> dictionary = new TreeSet<>(words);
        ArrayList<String[]> matrix = new ArrayList<>();

        // Считываем файл, формируем матрицу строк
        try {
            Scanner scanner = new Scanner(new File(inputName));
            while (scanner.hasNextLine()) {
                String t = scanner.nextLine();
                if (!t.matches("([А-Я] )+[А-Я]") && !t.matches("([A-Z] )+[A-Z]"))
                    throw new IllegalArgumentException("wrong format");
                matrix.add(t.split(" "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JavaAlgorithms.checkIfMatrix(matrix);
        final Set<String> result = new TreeSet<>();

        // Запускаем рекурсивный поиск совпадений по "соседям" для каждого элемента матрицы
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(0).length; j++) {
                searchRecursive(matrix, i, j, matrix.get(i)[j], result, dictionary);
            }
        }
        return result;
    }

    static private void checkIfMatrix(ArrayList<String[]> matrix) {
        for (String[] line : matrix)
            if (matrix.get(0).length != line.length)
                throw new IllegalArgumentException("wrong format");
    }

    // Рекурсивный алгоритм поиска по "соседям" с исключенными диагоналями
    static private void searchRecursive(ArrayList<String[]> matrix, int i, int j, String prefix, Set<String> result,
                                        TreeSet<String> dictionary) {

        for (int i1 = Math.max(0, i - 1); i1 < Math.min(matrix.size(), i + 2); i1++) {
            for (int j1 = Math.max(0, j - 1); j1 < Math.min(matrix.get(0).length, j + 2); j1++) {
                if (abs(i - i1) + abs(j - j1) == 2) continue;
                if (i1 != i || j1 != j) {
                    String word = prefix + matrix.get(i1)[j1];

                    if (dictionary.contains(word)) {
                        result.add(word);
                    }

                    if (dictionary.subSet(word, word + Character.MAX_VALUE).size() > 0) {
                        searchRecursive(matrix, i1, j1, word, result, dictionary);
                    }
                }
            }
        }
    }
}
