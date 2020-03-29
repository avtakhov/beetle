package com.example.beetle.album;


import com.example.beetle.runner.Move;

public interface Grid {

    Cell getCell(int r, int c);

    default Cell getCell(Move move) {
        return getCell(move.getR(), move.getC());
    }

    int getM();

    int getN();

    boolean isValid(Move move);

    boolean change(int r, int c);

    void clear();

}
