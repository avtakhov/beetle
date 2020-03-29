package com.example.beetle.album;

import com.example.beetle.runner.Move;
import com.example.beetle.runner.Vector;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class Album implements Grid {

    private int n, m;
    private Cell[][] cells;

    public Album(int n, int m) {
        this.n = n;
        this.m = m;
        cells = new Cell[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(cells[i], Cell.EMPTY);
        }
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public boolean change(int r, int c) {
        if (!isValid(new Move(r, c))) {
            return false;
        }
        cells[r][c] = getCell(r, c) == Cell.EMPTY ? Cell.WALL : Cell.EMPTY;
        if (cells[r][c] == Cell.WALL) {
            Queue<Move> q = new ArrayDeque<>();
            q.add(new Move(0, 0));
            boolean[][] visited = new boolean[n][m];
            visited[0][0] = true;
            Vector[] nei = new Vector[]{
                    new Vector(1, 0),
                    new Vector(0, 1),
                    new Vector(-1, 0),
                    new Vector(0, -1)};
            while (!q.isEmpty()) {
                Move pos = q.remove();
                for (Vector t : nei) {
                    Move to = pos.add(t);
                    if (isValid(to) && !visited[to.getR()][to.getC()] && getCell(to.getR(), to.getC()) == Cell.EMPTY) {
                        q.add(to);
                        visited[to.getR()][to.getC()] = true;
                    }
                }
            }

            if (!visited[n - 1][m - 1]) {
                cells[r][c] = Cell.EMPTY;
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < getN(); i++) {
            for (int j = 0; j < getM(); j++) {
                cells[i][j] = Cell.EMPTY;
            }
        }
    }

    @Override
    public Cell getCell(int r, int c) {
        return cells[r][c];
    }

    @Override
    public boolean isValid(Move move) {
        return 0 <= move.getR() && move.getR() < n
                && 0 <= move.getC() && move.getC() < m;
    }
}
