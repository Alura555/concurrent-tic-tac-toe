package com.epam.rd.autocode.concurrenttictactoe;

import java.util.Arrays;

public class TicTacToeImpl implements TicTacToe{
    public static final int SIZE_OF_TABLE = 3;
    private final char[][] table;
    private char lastMark;

    public TicTacToeImpl() {
        this.table = new char[SIZE_OF_TABLE][SIZE_OF_TABLE];
        Arrays.stream(table)
                .forEach(row -> Arrays.fill(row, ' '));
    }

    @Override
    public void setMark(int x, int y, char mark) {
        if (x < 0 || y < 0 || x >= SIZE_OF_TABLE || y >= SIZE_OF_TABLE || table[x][y] != ' ') {
            throw new IllegalArgumentException();
        }
        table[x][y] = mark;
        lastMark = mark;
    }

    @Override
    public char[][] table() {
        return Arrays.stream(table)
                .map(char[]::clone)
                .toArray(char[][]::new);
    }

    @Override
    public char lastMark() {
        return this.lastMark;
    }
}
