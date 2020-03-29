package com.example.beetle.runner;

import com.example.beetle.album.Grid;

import java.util.ArrayList;
import java.util.List;

public class BeetleRunner implements Runner {

    private final Grid grid;

    public BeetleRunner(Grid grid) {
        this.grid = grid;
    }

    @Override
    public List<Move> run() {
        ArrayList<Move> res = new ArrayList<>();
        for (int i = 0; i < grid.getN(); i++) {
            res.add(new Move(i, 0));
        }
        for (int i = 0; i < grid.getM(); i++) {
            res.add(new Move(0, i));
        }
        return res;
    }
}
