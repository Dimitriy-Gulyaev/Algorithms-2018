package lesson1;

import kotlin.NotImplementedError;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
     * каждый на отдельной строке. Пример:
     * <p>
     * 13:15:19
     * 07:26:57
     * 10:00:03
     * 19:56:14
     * 13:15:19
     * 00:40:31
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 00:40:31
     * 07:26:57
     * 10:00:03
     * 13:15:19
     * 13:15:19
     * 19:56:14
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    // T = O(N logN), R = O(N)
    static public void sortTimes(String inputName, String outputName) {
        try {
            Scanner scanner = new Scanner(new File(inputName));
            ArrayList<Integer> list = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String t = scanner.nextLine();
                if (!t.matches("[0-9][0-9]:[0-9][0-9]:[0-9][0-9]"))
                    throw new IllegalArgumentException("wrong format");
                list.add(Integer.parseInt(t.replaceAll(":", "")));
            }
            Collections.sort(list);
            JavaTasks.dateWriter(outputName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void dateWriter(String outputName, ArrayList<Integer> list) throws IOException {
        FileWriter writer = new FileWriter(outputName);
        for (Integer element : list) {
            writer.write(String.format("%d%d:%d%d:%d%d%n", element / 100000, element / 10000 % 10,
                    element % 10000 / 1000, element % 1000 / 100, element % 100 / 10, element % 10));
            writer.flush();
        }
    }

    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    // T = O(N), R = O(N)
    static public void sortAddresses(String inputName, String outputName) throws IOException {
        Scanner scanner = new Scanner(new File(inputName));
        Map<String, Set<String>> map = new TreeMap<>();
        while (scanner.hasNextLine()) {
            String t = scanner.nextLine();
            if (!t.matches("[А-Я][а-я]+ [А-Я][а-я]+ - [А-Я][а-я]+ [0-9]+"))
                throw new IllegalArgumentException("wrong format");
            String[] data = t.split(" - ");
            if (map.containsKey(data[1]))
                map.get(data[1]).add(data[0]);
            else {
                Set<String> set = new TreeSet<>();
                set.add(data[0]);
                map.put(data[1], set);
            }
        }
        JavaTasks.addressWriter(outputName, map);
    }

    private static void addressWriter(String outputName, Map<String, Set<String>> map) throws IOException {
        FileWriter writer = new FileWriter(outputName);
        for (String address : map.keySet()) {
            StringBuilder temp = new StringBuilder(address).append(" - ").append(map.get(address).toArray()[0]);
            if (map.get(address).size() > 1) {
                for (int i = 1; i < map.get(address).size(); i++) {
                    temp.append(", ").append(map.get(address).toArray()[i]);
                }
            }
            writer.write(temp.toString() + "\n");
            writer.flush();
        }
    }

    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    // T = O(N*logN), R = O(N)
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        Scanner scanner = new Scanner(new File(inputName));
        List<Double> temperatures = new ArrayList<>();
        while (scanner.hasNextLine()) {
            temperatures.add(Double.parseDouble(scanner.nextLine()));
        }
        Collections.sort(temperatures);
        JavaTasks.temperaturesWriter(outputName, temperatures);
    }

    static private void temperaturesWriter(String outputName, List<Double> temperatures) throws IOException {
        FileWriter writer = new FileWriter(outputName);
        for (Double element : temperatures) {
            writer.write(element.toString() + "\n");
            writer.flush();
        }
    }

    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
