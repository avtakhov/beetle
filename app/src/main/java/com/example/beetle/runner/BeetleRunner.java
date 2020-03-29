package com.example.beetle.runner;

import com.example.beetle.album.Cell;
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
        Vector[] nei = {
                new Vector(1, 0),
                new Vector(0, 1),
                new Vector(-1, 0),
                new Vector(0, -1)
        };
        int[][] count = new int[grid.getN()][grid.getM()];
        Move cur = new Move(0, 0);
        while (cur.getR() + 1 != grid.getN() || cur.getC() + 1 != grid.getM()) {
            count[cur.getR()][cur.getC()]++;
            Move bestMove = null;
            int bestCount = Integer.MAX_VALUE;
            for (Vector vector : nei) {
                Move move = cur.add(vector);
                if (grid.isValid(move) && count[move.getR()][move.getC()] < bestCount && grid.getCell(move) == Cell.EMPTY) {
                    bestCount = count[move.getR()][move.getC()];
                    bestMove = move;
                }
            }
            res.add(cur = bestMove);
        }
        return res;
    }
}
