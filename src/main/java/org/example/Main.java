package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            FileFilter fileFilter = new FileFilter(args);
            fileFilter.processFiles();
            fileFilter.printStats();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
    }
}