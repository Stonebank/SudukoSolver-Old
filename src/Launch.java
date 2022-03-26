import board.SudukoBoard;

public class Launch {

    private static final int[][] BOARD = new int[][] {
            { 0, 0, 0, 9, 6, 0, 5, 0, 4 },
            { 0, 2, 0, 1, 0, 0, 0, 6, 0 },
            { 5, 0, 0, 0, 0, 0, 8, 0, 9 },
            { 0, 3, 2, 0, 0, 0, 0, 5, 1 },
            { 1, 9, 6, 7, 5, 3, 0, 0, 2 },
            { 7, 0, 5, 0, 0, 0, 0, 9, 0 },
            { 9, 8, 4, 5, 0, 1, 0, 0, 6 },
            { 2, 0, 0, 0, 0, 9, 1, 0, 8},
            { 0, 0, 0, 8, 2, 7, 9, 0, 0}
    };

    private final static int SIZE = 9;

    public static void main(String[] args) {
        SudukoBoard board = new SudukoBoard(BOARD, SIZE);

        System.out.println("Board unsolved:");
        board.displayBoard();

        System.out.println();
        System.out.println();

        System.out.println(board.canSolve() ? "Board solved:" : "Board cannot be solved.");
        board.displayBoard();

    }

}
