import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 4*. Скопировать файл src.zip в каталог с проектом (программно),
 * разархивировать его и вывести имена всех файлов в которых встречается строка @FunctionalInterface
 */
public class Main {
    private static void copyFile(File source, File destination) throws IOException {
        Files.copy(source.toPath(), destination.toPath());
    }

    private static List<String> findWordInZip(File file, String word) {
        List<String> result = new ArrayList<>();
        List<File> files = Arrays.asList(file.listFiles());
        for (File f : files) {
            if (!f.isDirectory()) {
                if (scanFile(f, word)) {
                    result.add(f.getAbsolutePath());
                    System.out.println(f.getAbsolutePath());
                }
            } else {
                findWordInZip(f, word);
            }
        }
        return result;
    }

    private static boolean scanFile(File inFile, String word) {
        try {
            Scanner scanner = new Scanner(inFile);
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().trim().contains(word)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    ////////////////////////////

    public static File unzip(String source, String destination) {
        try {
            File tmp = new File(destination);
            if (tmp.exists()) {
                FileUtils.deleteDirectory(new File(destination));
            }
            ZipFile zipFile = new ZipFile(source);
            System.out.println("=============================");
            System.out.println("Wait, unzipping in process...");
            System.out.println("=============================");
            zipFile.extractAll(destination);

            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    ///////////////////////////

    public static void main(String[] args) {
        String javaHome = System.getProperty("java.home");
        File sourceFile = new File(javaHome + "/lib/src.zip");
        File destination = new File("./src/src.zip");
        String unzipDestination = new File("./src/unzip/").getAbsolutePath();
        String needWord = "@FunctionalInterface";
        try {
            if (!destination.exists()) {
                copyFile(sourceFile, destination);
            }
            findWordInZip(unzip(destination.getAbsolutePath(), unzipDestination), needWord);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
