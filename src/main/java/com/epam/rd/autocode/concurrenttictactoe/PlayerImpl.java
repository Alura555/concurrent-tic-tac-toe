package com.epam.rd.autocode.concurrenttictactoe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerImpl implements Player{
    private static final Lock LOCK = new ReentrantLock();
    private static final Condition MAKE_MOVE = LOCK.newCondition();
    public static final char GOES_FIRST_MARK = 'X';
    private static boolean gameIsStart;

    private final TicTacToe ticTacToe;
    private final char mark;
    private final PlayerStrategy strategy;

    public PlayerImpl(TicTacToe ticTacToe, char mark, PlayerStrategy strategy) {
        this.ticTacToe = ticTacToe;
        this.mark = mark;
        this.strategy = strategy;
        gameIsStart = false;
    }

    @Override
    public void run() {
        while (gameNotOver()) {
            LOCK.lock();
            try {
                if (ticTacToe.lastMark() == mark) {
                    MAKE_MOVE.await();
                }
                if (gameNotOver()
                        && ticTacToe.lastMark() != mark
                        && (mark == GOES_FIRST_MARK || gameIsStart)) {
                    Move move = strategy.computeMove(mark, ticTacToe);
                    ticTacToe.setMark(move.row, move.column, mark);
                    gameIsStart = true;
                }
                MAKE_MOVE.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
        }
    }

    private boolean gameNotOver() {
        boolean diagonal1 = true;
        boolean diagonal2 = true;
        final char[][] table = ticTacToe.table();
        for (int i = 0; i < table.length; i++) {
            boolean horizontal = true;
            boolean vertical = true;
            for (int j = 0; j < table.length - 1; j++) {
                horizontal &= table[i][j] == table[i][j + 1]
                        && table[i][j] != ' ';
                vertical &= table[j][i] == table[j + 1][i]
                        && table[j][i] != ' ';
                diagonal1 &= table[j][j] == table[j + 1][j + 1]
                        && table[j][j] != ' ';
                diagonal2 &= table[j][table.length - 1 - j] == table[j + 1][1 - j]
                        && table[j][table.length - 1 - j] != ' ';
            }
            if (horizontal || vertical || diagonal1 || diagonal2) {
                return false;
            }
        }
        return true;
    }

}
