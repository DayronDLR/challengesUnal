package edu.harding.tictactoe;


import java.util.Random;


public class TicTacToeGame {
    private char mTable[];
    public static final char OPEN_SPOT = ' ';
    public static final int BOARD_SIZE = 9;
    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public enum DifficultyLevel {Easy, Harder, Expert}
    private Random mRand;
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel mDifficultyLevel) {
        this.mDifficultyLevel = mDifficultyLevel;
    }




    public TicTacToeGame() {
        mTable = new char[BOARD_SIZE];
        // Seed the random number generator
        mRand = new Random();


    }

    /**
     * Clear the board of all X's and O's by setting all spots to OPEN_SPOT.
     */
    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mTable[i] = OPEN_SPOT;

        }

    }

    /**
     * Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *  @param player   - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     * @return
     */
    public boolean setMove(char player, int location) {

        if (location >= 0 && location < BOARD_SIZE &&
                mTable[location] == OPEN_SPOT) {
            mTable[location] = player;
            return true;
        }
        return false;
    }


    public char getBoardOccupant(int location) {
        if (location >= 0 && location < BOARD_SIZE) {
            return mTable[location];
        }
        return'?';
    }
    public int getComputerMove() {

            int move = -1;

            if (mDifficultyLevel == DifficultyLevel.Easy) {
                move = getRandomMove();
            }
            else if (mDifficultyLevel == DifficultyLevel.Harder) {
                move = getWinningMove();
                if (move == -1)
                    move = getRandomMove();
            }
            else if (mDifficultyLevel == DifficultyLevel.Expert) {

                move = getWinningMove();
                if (move == -1)
                    move = getBlockingMove();
                if (move == -1)
                    move = getRandomMove();
            }

            return move;
        }


    //
    private int getBlockingMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            char curr = mTable[i];

            if (curr != HUMAN_PLAYER && curr != COMPUTER_PLAYER) {

                setMove(HUMAN_PLAYER, i);

                if (checkForWinner() == 2) {
                    mTable[i] = OPEN_SPOT;
                    return i;
                } else
                    mTable[i] = OPEN_SPOT;
            }
        }
        return -1;
    }

    //
    private int getWinningMove() {


        for (int i = 0; i < BOARD_SIZE; i++) {
            char curr = mTable[i];

            if (curr != HUMAN_PLAYER && curr != COMPUTER_PLAYER) {
                setMove(COMPUTER_PLAYER, i);
                if (checkForWinner() == 3) {
                    mTable[i] = OPEN_SPOT;
                    return i;
                } else
                    mTable[i] = OPEN_SPOT;
            }
        }

        return -1;
    }

    //
    private int getRandomMove() {
        int move;
        do {
            move = mRand.nextInt(BOARD_SIZE);
        }
        while (mTable[move] == HUMAN_PLAYER || mTable[move] == COMPUTER_PLAYER);
        return move;
    }


    /**
     * Check for a winner and return a status value indicating who has won.
     *
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner() {
        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3) {
            if (mTable[i] == HUMAN_PLAYER &&
                    mTable[i + 1] == HUMAN_PLAYER &&
                    mTable[i + 2] == HUMAN_PLAYER)
                return 2;
            if (mTable[i] == COMPUTER_PLAYER &&
                    mTable[i + 1] == COMPUTER_PLAYER &&
                    mTable[i + 2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mTable[i] == HUMAN_PLAYER &&
                    mTable[i + 3] == HUMAN_PLAYER &&
                    mTable[i + 6] == HUMAN_PLAYER)
                return 2;
            if (mTable[i] == COMPUTER_PLAYER &&
                    mTable[i + 3] == COMPUTER_PLAYER &&
                    mTable[i + 6] == COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mTable[0] == HUMAN_PLAYER &&
                mTable[4] == HUMAN_PLAYER &&
                mTable[8] == HUMAN_PLAYER) ||
                (mTable[2] == HUMAN_PLAYER &&
                        mTable[4] == HUMAN_PLAYER &&
                        mTable[6] == HUMAN_PLAYER))
            return 2;
        if ((mTable[0] == COMPUTER_PLAYER &&
                mTable[4] == COMPUTER_PLAYER &&
                mTable[8] == COMPUTER_PLAYER) ||
                (mTable[2] == COMPUTER_PLAYER &&
                        mTable[4] == COMPUTER_PLAYER &&
                        mTable[6] == COMPUTER_PLAYER))
            return 3;


        for (int i = 0; i < BOARD_SIZE; i++) {

            if (mTable[i] != HUMAN_PLAYER && mTable[i] != COMPUTER_PLAYER)
                return 0;
        }
        return 1;
    }
    public char[] getTableState() {
        return mTable;
    }

    public void setTableState(char[] board) {
        mTable = board.clone();
    }

}