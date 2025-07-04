package org.example.model;

public class NumberStats {
    private final double min;
    private final double max;
    private final double sum;
    private final double average;

    public NumberStats(double min, double max, double sum, double average) {
        this.min = min;
        this.max = max;
        this.sum = sum;
        this.average = average;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getSum() {
        return sum;
    }

    public Double getAverage() {
        return average;
    }
}
