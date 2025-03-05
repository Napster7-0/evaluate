package com.testing.evaluate.utils;

public class QuadraticTestCase {
    private float a;
    private float b;
    private float c;
    String description;

    public QuadraticTestCase(float a, float b, float c, String description) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("a=%.4g, b=%.4g, c=%.4g: %s", a, b, c, description);
    }
    public float getA(){
        return this.a;
    }
    public float getB(){
        return this.b;
    }
    public float getC(){
        return this.c;
    }
}
