import board.SudukoBoard;

public class Launch {

    private static final int[][] BOARD = new int[][] {
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

    private final static int SIZE = 9;

    public static void main(String[] args) {
        SudukoBoard board = new SudukoBoard(BOARD, SIZE);

        System.out.println("Board unsolved:");
        board.displayBoard();

        System.out.println();
        System.out.println();

        System.out.println(board.canSolve() ? "Board solved:" : "Board cannot be solved.");
        board.displayBoard();

        System.out.println("Board size: " + calcBoardSize());

    }

    private static int calcBoardSize() {
        int num = 0;
        for (int i = 0; i < Launch.BOARD.length; i++) {
            for (int j = 0; j < Launch.BOARD.length; j++) {
                num++;
            }
        }
        return num;
    }

}
