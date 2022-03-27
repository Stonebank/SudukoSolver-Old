package robot;

import board.SudukoBoard;
import settings.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Scanner;

public class Typer {

    public static void main(String[] args) throws AWTException {
        int[][] current_board = new int[][] {
                { 9, 1, 3, 4, 2, 7, 0, 8, 0 },
                { 6, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 2, 0, 0, 0, 0, 3, 0, 7, 0 },
                { 0, 0, 0, 1, 0, 2, 0, 0, 8 },
                { 0, 6, 2, 5, 0, 0, 0, 0, 3 },
                { 5, 3, 8, 7, 0, 0, 2, 9, 0 },
                { 3, 4, 0, 8, 7, 0, 0, 6, 0 },
                { 0, 0, 6, 0, 4, 9, 8, 1, 5 },
                { 8, 0, 1, 2, 0, 0, 0, 0, 0 }
        };
        new Typer(current_board);
    }

    private final Robot robot;

    private final int[][] board;
    private final int[][] keys = new int[9][9];

    private final SudukoBoard sudukoBoard;

    private boolean activateRobot;

    public Typer(int[][] board) throws AWTException {
        this.robot = new Robot();
        this.board = board;

        Scanner scanner = new Scanner(System.in);

        sendToggleMessage();

        this.sudukoBoard = new SudukoBoard(board, 9);

        while (scanner.hasNext()) {
            switch (scanner.nextLine().toLowerCase()) {
                case "toggle" -> toggleRobot();
                case "board" -> sudukoBoard.displayBoard();
                case "solve" -> {
                    if (activateRobot)
                        test();
                }
                default -> System.err.println("Input not registered");
            }
        }

    }

    private void test() {

        if (!sudukoBoard.canSolve()) {
            System.err.println("Sudoku board not solvable.");
            System.exit(0);
            return;
        }

        try {
            System.out.println("Starting in " + Settings.ROBOT_START_DELAY + " ms.");
            Thread.sleep(Settings.ROBOT_START_DELAY);
        } catch (InterruptedException e) {
            System.err.println("Robot was interrupted.");
            e.printStackTrace();
        }

        convertKeys();
        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys.length; j++) {
                pressKey(keys[i][j]);
                pressKey(KeyEvent.VK_SPACE);
                if (i > 0 && j % 9 == 0)
                    pressKey(KeyEvent.VK_ENTER);
            }
        }

        sudukoBoard.displayBoard();
    }

    private void pressKey(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
        robot.delay(Settings.ROBOT_DELAY);
        System.out.println("Robot pressed " + KeyEvent.getKeyText(key));
    }

    private void convertKeys() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                try {
                    keys[i][j] = KeyEvent.class.getField("VK_" + board[i][j]).getInt(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.err.println("Fault converting " + board[i][j] + ".");
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Board digits has been converted to keys");
    }

    private void sendToggleMessage() {
        System.out.println("Type 'toggle' to " + (activateRobot ? "disable" : "enable") + " the robot.");
    }

    private void toggleRobot() {
        activateRobot = !activateRobot;
        System.out.println("Robot has been " + (activateRobot ? "enabled, type 'solve' to start." : "disabled") + ".");
    }

}
