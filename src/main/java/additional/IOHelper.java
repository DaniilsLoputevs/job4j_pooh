package additional;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Класс содержащий универсальные и вспомогательные методы для работы с IO.
 *
 * @author Daniils Loputevs
 * @version 1.1
 * @since 18.02.20.
 * Last upd:  24.03.20.
 * Last JavaDoc upd:  24.03.20.
 */
public class IOHelper {

    /**
     * Преобразовать содержимое файла в List, далее работать текстом в виде List.
     *
     * @param path            - Путь файла.
     * @param listConstructor - ссылка на конструктор класса имплементирующий List<>
     * @return List<String> Все строчки из файла.
     */
    public static List<String> readFileToList(String path, Supplier<List<String>> listConstructor) {
        List<String> fileLines = new LinkedList<>();
        try (var bufferedReader = new BufferedReader(new FileReader(path))) {
            fileLines = bufferedReader.lines().collect(Collectors.toCollection(listConstructor));
        } catch (IOException e) {
            System.out.println("IOException: IOHelper - read File to List!");
            e.printStackTrace();
        }
        return fileLines;
    }

    public static List<String> readFileToList(File file, Supplier<List<String>> listConstructor) {
        return readFileToList(file.getPath(), listConstructor);
    }

    /* ########## Short form ########## */
    public static List<String> readFileToList(String path) {
        return readFileToList(path, ArrayList::new);
    }

    public static List<String> readFileToList(File file) {
        return readFileToList(file, ArrayList::new);
    }

    /**
     * Записать List в файл{@code path}.
     * * Если не нужно разделять строки >> sysSeparator = "";
     *
     * @param path         - Путь записи.
     * @param content      - List для записи.
     * @param sysSeparator - Разделитель строки.
     */
    public static void writeListToFile(String path, List<String> content, String sysSeparator) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String contentLine : content) {
                writer.write(contentLine + sysSeparator);
            }
        } catch (IOException e) {
            System.out.println("IOException: IOHelper - write List to File!");
            e.printStackTrace();
        }
    }

    public static void writeListToFile(File file, List<String> content, String sysSeparator) {
        writeListToFile(file.getPath(), content, sysSeparator);
    }

    public static void writeStringToFie(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("IOException: IOHelper - write List to File!");
            e.printStackTrace();
        }
    }

    /**
     * Переписать Текст из {@code source} в файл по {@code targetPath}.
     *
     * @param source     - Оригинал.
     * @param targetPath - Копия.
     */
    public static void copyTextToTarget(File source, String targetPath, String sysSeparator) {
        writeListToFile(targetPath, readFileToList(source.getPath(), ArrayList::new), sysSeparator);
    }

    /**
     * Сравнивает текстовое содержимое из файла{@code sourcePath} с {@code List expected}.
     *
     * @param sourcePath - Путь к файлу для сравнения.
     * @param expected   - Ожидаемое содержимое.
     * @return true/false
     */
    public static boolean compareInfoFromFileWithList(String sourcePath, List<String> expected) {
        List<String> fileLines = readFileToList(sourcePath, ArrayList::new);
        return fileLines.containsAll(expected);
    }

    /**
     * Полностью очистить текстовой файл.
     *
     * @param sourcePath - путь файла.
     */
    public static void clearFile(String sourcePath) {
        IOHelper.writeListToFile(sourcePath, List.of(""), "");
    }

    /**
     * Создать новый файл.
     * * Можно сразу сделать его директорией.
     *
     * @param path    - Path.
     * @param makeDir - Make this file Dir or not.
     * @return - Created file.
     */
    public static File createFile(String path, boolean makeDir) {
        File file = new File(path);
        if (makeDir) {
            file.mkdir();
        } else {
            IOHelper.clearFile(file.getPath());
        }
        return file;
    }

    /**
     * Получить расширение файла.
     *
     * @param file файл.
     * @return расширение.
     */
    public static String getExt(File file) {
        return file.getName().substring(file.getName().lastIndexOf(".") + 1);
    }
}
