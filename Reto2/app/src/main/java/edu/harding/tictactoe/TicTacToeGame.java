package edu.harding.tictactoe;


import java.util.Random;


public class TicTacToeGame {
    private char mTable [];
    public static final char OPEN_SPOT = ' ';
    public static final int BOARD_SIZE = 9;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';

    private Random mRand;

    public TicTacToeGame() {
        mTable= new char[BOARD_SIZE];
        // Seed the random number generator
        mRand = new Random();


    }


    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard(){
        for(int i=0;i < BOARD_SIZE; i++)
        {
                mTable[i] = OPEN_SPOT;

        }

    }

    /** Set the given player at the given location on the game board.
     *  The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public void setMove(char player, int location){

        if (location < BOARD_SIZE && location >= 0 && mTable[location] == OPEN_SPOT)
            mTable[location] = player;
    }

    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    public int getComputerMove()
    {
        int move;
        for(int i=0;i<BOARD_SIZE;i++)
        {

            if (mTable[i] != HUMAN_PLAYER && mTable[i] != COMPUTER_PLAYER)
            {
                char curr = mTable[i];
                setMove(COMPUTER_PLAYER,i);

                if (checkForWinner() == 3) {
                    mTable[i]=OPEN_SPOT;
                    return i;
                }
                else
                    mTable[i] = curr;
            }
        }

        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mTable[i] != HUMAN_PLAYER && mTable[i]!= COMPUTER_PLAYER) {
                char curr = mTable[i];   // Save the current number
                setMove(HUMAN_PLAYER,i);
                if (checkForWinner() == 2) {
                    mTable[i]=OPEN_SPOT;
                    return i;
                }
                else
                    mTable[i] = curr;
            }
        }

        // Generate random move
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mTable[move] != OPEN_SPOT);


       return move;
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner(){
        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)
        {
            if (mTable[i] == HUMAN_PLAYER &&
                    mTable[i+1] == HUMAN_PLAYER &&
                    mTable[i+2]== HUMAN_PLAYER)
                return 2;
            if (mTable[i] == COMPUTER_PLAYER &&
                    mTable[i+1]== COMPUTER_PLAYER &&
                    mTable[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++)
        {
            if (mTable[i] == HUMAN_PLAYER &&
                    mTable[i+3] == HUMAN_PLAYER &&
                    mTable[i+6]== HUMAN_PLAYER)
                return 2;
            if (mTable[i] == COMPUTER_PLAYER &&
                    mTable[i+3] == COMPUTER_PLAYER &&
                    mTable[i+6]== COMPUTER_PLAYER)
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

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mTable[i] != HUMAN_PLAYER && mTable[i] != COMPUTER_PLAYER)
                return 0;
        }
        return 1;
    }


}