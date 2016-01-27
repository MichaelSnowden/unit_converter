package com.michaelsnowden.unit_converter;

/**
 * @author michael.snowden
 */
public class Fraction {
    private final int n;
    private final int d;

    public Fraction(double value) {
        this((int) (value * 1E5), (int) 1E5);
    }

    public Fraction(int n, int d) {
        if (d == 0) {
            throw new IllegalArgumentException("Cannot have a denominator of 0");
        }
        int gcf = gcf(n, d);
        this.n = n / gcf;
        this.d = d / gcf;
    }

    public Fraction minus(Fraction o) {
        return new Fraction(n * o.d - o.n * d, d * o.d);
    }

    public Fraction plus(Fraction o) {
        return new Fraction(n * o.d + o.n * d, d * o.d);
    }

    public Fraction times(Fraction o) {
        return new Fraction(n * o.n, d * o.d);
    }

    public Fraction dividedBy(Fraction o) {
        return new Fraction(n * o.d, d * o.n);
    }

    private int gcf(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcf(b, a % b);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(n);
        if (d != 1) {
            builder.append("/").append(d);
        }
        return builder.toString();
    }

    public Fraction negate() {
        return new Fraction(-n, d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fraction fraction = (Fraction) o;

        return n == fraction.n && d == fraction.d;
    }

    @Override
    public int hashCode() {
        int result = n;
        result = 31 * result + d;
        return result;
    }

    public int getN() {
        return n;
    }

    public int getD() {
        return d;
    }

    public double getDouble() {
        return (double) n / d;
    }

    public boolean isOne() {
        return n == 1 && d == 1;
    }

    public boolean isNotZero() {
        return n != 0;
    }

    public Fraction times(double value) {
        return new Fraction((int) Math.round(n * value), d);
    }
}