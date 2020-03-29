package com.example.beetle.album;


import com.example.beetle.runner.Move;

public interface Grid {

    Cell getCell(int r, int c);

    int getM();

    int getN();

    boolean isValid(Move move);

    boolean change(int r, int c);

}
