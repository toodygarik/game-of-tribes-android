package com.garik.android.gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {

    private int cols;
    private int rows;
    private int[][] map;

    public GameModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        map = new int[rows][cols];
    }

    public boolean isAlive(int row, int col) {
        return isInsideMap(row, col) && map[row][col] != 0;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public void makeAlive(int row, int col, int color) {
        if (!isInsideMap(row, col))
            return;

        map[row][col] = color;
    }

    private boolean isInsideMap(int row, int col) {
        return !(row < 0 || row >= rows || col < 0 || col >= cols);
    }

    public void makeDead(int row, int col) {
        if (!isInsideMap(row, col))
            return;

        map[row][col] = 0;
    }

    public int willLive(int row, int col) {
        List<Integer> aliveNeighbours = new ArrayList<>();

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // not a neighbour it's the point
                if (r == row && c == col)
                    continue;
                if (isAlive(r, c))
                    aliveNeighbours.add(getColor(r, c));
            }
        }

        if (isAlive(row, col)) {
            return aliveNeighbours.size() == 2 || aliveNeighbours.size() == 3 ? getColor(row, col) : 0;
        } else {
            return aliveNeighbours.size() == 3 ? getAvgColor(aliveNeighbours) : 0;
        }
    }

    public int getColor(int row, int col) {
        return map[row][col];
    }

    public void next() {
        int[][] newMap = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newMap[r][c] = willLive(r, c);
            }
        }

        map = newMap;
    }

    public void clear() {
        map = new int[rows][cols];
    }

    private int getAvgColor(List<Integer> colors) {
        if(colors == null || colors.size() == 0)
            return 0;

        int rSum = 0;
        int gSum = 0;
        int bSum = 0;
        for (int color : colors) {
            rSum += (color >> 16) & 0xFF;
            gSum += (color >> 8) & 0xFF;
            bSum += (color) & 0xFF;
        }

        rSum /= colors.size();
        gSum /= colors.size();
        bSum /= colors.size();

        return ((rSum & 0xFF) << 16) + ((gSum & 0xFF) << 8) + ((bSum & 0xFF));
    }

    public void setupRandom(int count) {
        if (count <= 0)
            return;

        clear();

        int[] colors = {0xff0000, 0x00ff00, 0x0000ff};
        Random rnd = new Random();
        while (countAlives() < count) {
            makeAlive(rnd.nextInt(rows), rnd.nextInt(cols), colors[rnd.nextInt(3)]);
        }
    }

    private int countAlives() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (isAlive(r, c))
                    count++;
            }
        }
        return count;
    }
}
