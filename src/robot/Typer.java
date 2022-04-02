package robot;

import board.SudukoBoard;
import board.mode.Mode;
import settings.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class Typer {

    private final Scanner scanner;

    private final Robot robot;

    private final SudukoBoard board;
    private final int[][] keys = new int[9][9];

    private boolean activateRobot;

    public Typer(SudukoBoard board) throws AWTException {
        this.scanner = new Scanner(System.in);
        this.robot = new Robot();
        this.board = board;
    }

    public void initiateMode() {

        sendModeMessage(true);

        while (Settings.MODE == null) {

            switch (scanner.nextLine().toLowerCase()) {
                case "0" -> Settings.MODE = Mode.HAS_SCREENSHOT;
                case "1" -> Settings.MODE = Mode.TAKE_SCREENSHOT;
                default -> sendModeMessage(false);
            }

            System.out.println("Mode selected: " + Settings.MODE);

        }

    }

    public void initiate() {

        if (Settings.MODE == Mode.TAKE_SCREENSHOT) {
            activateRobot = true;
            solve();
            scanner.close();
            return;
        }

        sendToggleMessage();

        while (scanner.hasNext()) {
            switch (scanner.nextLine().toLowerCase()) {
                case "toggle" -> toggleRobot();
                case "board" -> board.displayBoard();
                case "solve" -> {
                    if (activateRobot)
                        solve();
                }
                default -> System.err.println("Input not registered");
            }
        }

    }

    private void solve() {

        if (!board.canSolve()) {
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

        long start = System.currentTimeMillis();

        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys.length; j++) {
                if (i > 0 && j % 9 == 0) {
                    pressKey(Settings.ROBOT_DOWN);
                    for (int k = 1; k <= 9; k++)
                        pressKey(Settings.ROBOT_LEFT);
                }
                if (j != 0)
                    pressKey(Settings.ROBOT_RIGHT);
                pressKey(keys[i][j]);
            }
        }

        board.displayBoard();
        System.out.println("Sudoku solved in " + (System.currentTimeMillis() - start) + " ms");

        System.exit(0);
    }

    private void pressKey(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
        robot.delay(Settings.ROBOT_DELAY);
        System.out.println("Robot pressed " + KeyEvent.getKeyText(key));
    }

    private void convertKeys() {
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard().length; j++) {
                try {
                    keys[i][j] = KeyEvent.class.getField("VK_" + board.getBoard()[i][j]).getInt(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.err.println("Fault converting " + board.getBoard()[i][j] + ".");
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Board digits has been converted to keys");
    }

    private void sendModeMessage(boolean welcomeMessage) {
        if (welcomeMessage)
            System.out.println("Welcome to Sudoku solver made by Hassan K. The application is under development. How should the application run?:");
        for (int i = 0; i < Mode.values().length; i++)
            System.out.println("Type " + i + " to enable " + Mode.values()[i]);
    }

    private void sendToggleMessage() {
        System.out.println("Type 'toggle' to " + (activateRobot ? "disable" : "enable") + " the robot.");
    }

    private void toggleRobot() {
        activateRobot = !activateRobot;
        System.out.println("Robot has been " + (activateRobot ? "enabled, type 'solve' to start." : "disabled") + ".");
        sendToggleMessage();
    }

}
