package org.example;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileFilter {
    private final List<String> inputFiles = new ArrayList<>();
    private String outputPath = ".";
    private String filePrefix = "";
    private boolean appendMode = false;
    private boolean shortStats = false;
    private boolean fullStats = false;

    private StringBuilder integerBuffer = new StringBuilder();
    private StringBuilder floatBuffer = new StringBuilder();
    private StringBuilder stringBuffer = new StringBuilder();
    private final StatsCollector statsCollector = new StatsCollector();

    public FileFilter(String[] args) throws IllegalArgumentException {
        parseArguments(args);
        validateArguments();
    }

    private void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-o" -> {
                        if (i + 1 < args.length) {
                            outputPath = args[++i];
                        } else {
                            throw new IllegalArgumentException("После опции -o должен быть указан путь");
                        }
                    }
                    case "-p" -> {
                        if (i + 1 < args.length) {
                            filePrefix = args[++i];
                        } else {
                            throw new IllegalArgumentException("После опции -p должен быть указан префикс");
                        }
                    }
                    case "-a" -> appendMode = true;
                    case "-s" -> shortStats = true;
                    case "-f" -> fullStats = true;
                    default -> throw new IllegalArgumentException("Неизвестная опция: " + arg);
                }
            } else {
                inputFiles.add(arg);
            }
        }
    }

    private void validateArguments() {
        if (inputFiles.isEmpty()) {
            throw new IllegalArgumentException("Не указаны входные файлы");
        }

        if (shortStats && fullStats) {
            throw new IllegalArgumentException("Опции -s и -f нельзя использовать одновременно");
        }

        if (!shortStats && !fullStats) {
            shortStats = true;
        }
    }

    public void processFiles() {
        try {
            createOutputDirectoryIfNeeded();

            for (String inputFile : inputFiles) {
                processSingleFile(inputFile);
            }
            statsCollector.parseIntegersFile(writeToFile(getOutputFilePath("integers"),
                    integerBuffer.toString().trim()));
            statsCollector.parseFloatsFile(writeToFile(getOutputFilePath("floats"),
                    floatBuffer.toString().trim()));
            statsCollector.parseStringsFile(writeToFile(getOutputFilePath("strings"),
                    stringBuffer.toString().trim()));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка обработки файлов: " + e.getMessage(), e);
        }
    }

    private void createOutputDirectoryIfNeeded() throws IOException {
        if (!outputPath.equals(".")) {
            File dir = new File(outputPath);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Не удалось создать директорию: " + outputPath);
            }
        }
    }
    private void processSingleFile(String inputFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line.trim());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Предупреждение: Файл не найден - " + inputFile);
        } catch (IOException e) {
            System.err.println("Предупреждение: Ошибка чтения файла - " + inputFile + ": " + e.getMessage());
        }
    }

    private void processLine(String line) throws IOException {
        if (line.isEmpty()) {
            return;
        }

        try {
            long intValue = Long.parseLong(line);
            integerBuffer.append(intValue).append(System.lineSeparator());
            return;
        } catch (NumberFormatException ignored) {
            // Не целое число
        }

        try {
            double doubleValue = Double.parseDouble(line);
            floatBuffer.append(doubleValue).append(System.lineSeparator());
            return;
        } catch (NumberFormatException ignored) {
            // Не вещественное число
        }

        stringBuffer.append(line).append(System.lineSeparator());
    }

    private String writeToFile(String filePath, String content) throws IOException {
        if (!content.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filePath, appendMode))) {
                writer.write(content);
                writer.newLine();
            }
        }
        return filePath;
    }

    private String getOutputFilePath(String type) {
        Path path = Paths.get(outputPath, filePrefix + type + ".txt");
        return path.toString();
    }

    public void printStats() {
        if (shortStats) {
            statsCollector.printShortStats();
        } else if (fullStats) {
            statsCollector.printFullStats();
        }
    }

}
