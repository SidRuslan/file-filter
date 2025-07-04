package org.example;

import org.example.model.NumberStats;
import org.example.model.StringStats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatsCollector {
    private final List<Long> integers = new ArrayList<>();
    private final List<Double> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public void parseIntegersFile(String pathToFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                integers.add(Long.parseLong(line));
            }
        } catch (IOException e) {
            System.err.println("Предупреждение: Ошибка чтения файла - " + pathToFile + ": " + e.getMessage());
        }
    }

    public void parseFloatsFile(String pathToFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                floats.add(Double.parseDouble(line));
            }
        } catch (IOException e) {
            System.err.println("Предупреждение: Ошибка чтения файла - " + pathToFile + ": " + e.getMessage());
        }
    }

    public void parseStringsFile(String pathToFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
        } catch (IOException e) {
            System.err.println("Предупреждение: Ошибка чтения файла - " + pathToFile + ": " + e.getMessage());
        }
    }

    public void printShortStats() {
        System.out.println("Краткая статистика:");
        System.out.println("Целые числа: " + integers.size());
        System.out.println("Вещественные числа: " + floats.size());
        System.out.println("Строки: " + strings.size());
    }

    public void printFullStats() {
        System.out.println("Полная статистика:");

        if (!integers.isEmpty()) {
            NumberStats intStats = calculateNumberStats(integers);
            System.out.println("Целые числа - Количество: " + integers.size() +
                    ", Минимальное: " + intStats.getMin().longValue() +
                    ", Максимальное: " + intStats.getMax().longValue() +
                    ", Сумма: " + intStats.getSum().longValue() +
                    ", Среднее: " + intStats.getAverage());
        } else {
            System.out.println("Целые числа не найдены");
        }

        if (!floats.isEmpty()) {
            NumberStats floatStats = calculateNumberStats(floats);
            System.out.println("Вещественные числа - Количество: " + floats.size() +
                    ", Минимальное: " + floatStats.getMin() +
                    ", Максимальное: " + floatStats.getMax() +
                    ", Сумма: " + floatStats.getSum() +
                    ", Среднее: " + floatStats.getAverage());
        } else {
            System.out.println("Вещественные числа не найдены");
        }

        if (!strings.isEmpty()) {
            StringStats stringStats = calculateStringStats(strings);
            System.out.println("Строки - Количество: " + strings.size() +
                    ", Длина самой короткой: " + stringStats.getMinLength() +
                    ", Длина самой длинной: " + stringStats.getMaxLength());
        } else {
            System.out.println("Строки не найдены");
        }
    }

    private NumberStats calculateNumberStats(List<? extends Number> numbers) {
        if (numbers.isEmpty()) {
            return new NumberStats(0, 0, 0, 0);
        }

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;

        for (Number num : numbers) {
            double value = num.doubleValue();
            if (value < min) min = value;
            if (value > max) max = value;
            sum += value;
        }

        return new NumberStats(min, max, sum, sum / numbers.size());
    }

    private StringStats calculateStringStats(List<String> strings) {
        if (strings.isEmpty()) {
            return new StringStats(0, 0);
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (String str : strings) {
            int length = str.length();
            if (length < min) min = length;
            if (length > max) max = length;
        }

        return new StringStats(min, max);
    }
}
