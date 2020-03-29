package com.example.beetle.runner;

import com.example.beetle.album.Cell;
import com.example.beetle.album.Grid;
import com.example.beetle.base.Pair;

import java.util.ArrayList;
import java.util.List;

public class BeetleRunner implements Runner {

    private final Grid grid;

    public BeetleRunner(Grid grid) {
        this.grid = grid;
    }

    private final static Vector[] nei = {
            new Vector(1, 0),
            new Vector(0, 1),
            new Vector(-1, 0),
            new Vector(0, -1)
    };

    @Override
    public List<Pair<Move, Vector>> run() {
        ArrayList<Pair<Move, Vector>> res = new ArrayList<>();
        int[][] count = new int[grid.getN()][grid.getM()];
        Move cur = new Move(0, 0);
        while (cur.getR() + 1 != grid.getN() || cur.getC() + 1 != grid.getM()) {
            count[cur.getR()][cur.getC()]++;
            Vector bestVector = null;
            int bestCount = Integer.MAX_VALUE;
            for (Vector vector : nei) {
                Move move = cur.add(vector);
                if (grid.isValid(move) && count[move.getR()][move.getC()] < bestCount && grid.getCell(move) == Cell.EMPTY) {
                    bestCount = count[move.getR()][move.getC()];
                    bestVector = vector;
                }
            }
            assert bestVector != null;
            res.add(new Pair<>(cur = cur.add(bestVector), bestVector));
        }
        return res;
    }
}
