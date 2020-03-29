package com.example.beetle.runner;

public class Move {
    private int r, c;

    public Move(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public Move add(Vector t) {
        return new Move(r + t.getDx(), c + t.getDy());
    }
}
