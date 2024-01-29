package com.sawtooth.ahacentralserver.models.storageserver;

public class Space {
    private static final int DEGREE_BASIS = 10;
    public double basis;
    public int degree;

    public Space() {
        basis = 0;
        degree = 0;
    }

    public Space(double basis, int degree) {
        this.basis = basis;
        this.degree = degree;
    }

    public static Space Max(Space first, Space second) {
        if (first.degree != second.degree)
            return (first.degree > second.degree) ? first : second;
        else
            return (first.basis > second.basis) ? first : second;
    }

    public static Space Min(Space first, Space second) {
        if (first.degree != second.degree)
            return (first.degree > second.degree) ? second : first;
        else
            return (first.basis > second.basis) ? second : first;
    }

    public Space Normalize() {
        if (basis >= DEGREE_BASIS)
            while ((int) (basis / DEGREE_BASIS) != 0) {
                basis /= DEGREE_BASIS;
                degree++;
            }
        else if (basis == 0)
            degree = 1;
        else
            while ((int) (basis * DEGREE_BASIS) == 0) {
                basis *= DEGREE_BASIS;
                degree--;
            }
        return this;
    }

    public Space Add(Space addend) {
        if (basis == addend.basis && degree == addend.degree)
            basis *= 2;
        else {
            Space max = Max(this, addend), min = Min(this, addend);

            if (max.basis == 0 && max.degree == 0) {
                basis = min.basis;
                degree = min.degree;
            }
            else if (min.basis == 0 && min.degree == 0) {
                basis = max.basis;
                degree = max.degree;
            }
            else {
                basis = min.basis * Math.pow(DEGREE_BASIS, min.degree - max.degree) + max.basis;
                degree = max.degree;
            }
        }
        return this.Normalize();
    }

    public Space Negate() {
        return new Space(-basis, degree);
    }

    public Space Subtract(Space subtrahend) {
        return this.Add(subtrahend.Negate());
    }

    public Space Divide(Space divider) {
        basis /= divider.basis;
        degree -= divider.degree;
        return this.Normalize();
    }

    public float ToFloat() {
        return (float) (basis * Math.pow(DEGREE_BASIS, degree));
    }

    public Space Copy() {
        return new Space(basis, degree);
    }
}
