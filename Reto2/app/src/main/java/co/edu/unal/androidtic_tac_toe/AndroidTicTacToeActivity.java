package co.edu.unal.androidtic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.harding.tictactoe.TicTacToeGame;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    private boolean mGameOver = false;
    //Reference
    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button mBoardButtons[];
    //Turn
    private char mTurn = TicTacToeGame.COMPUTER_PLAYER;
    // Various text displayed
    private TextView mInfoTextView,mTiesScore,mAndroidScore,mHumanScore;
    //Counts
    private int countT = 0,countH = 0,countA = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

        mInfoTextView = (TextView) findViewById(R.id.information);
        mAndroidScore = (TextView) findViewById(R.id.scoreAndroid);
        mHumanScore = (TextView) findViewById(R.id.scoreHuman);
        mTiesScore = (TextView) findViewById(R.id.scoreTies);

        mGame = new TicTacToeGame();
        startNewGame();

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }*/
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;
    }*/
    //

    public void menuNewGame(View v)
    {
        startNewGame();

    }
    private void startNewGame() {

        mGame.clearBoard();
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        switch(mTurn)
        {
            case TicTacToeGame.HUMAN_PLAYER:
                mTurn = TicTacToeGame.COMPUTER_PLAYER;
                mInfoTextView.setText(R.string.first_android);
                int move = mGame.getComputerMove();
                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                mInfoTextView.setText(R.string.turn_human);
            break;

            case TicTacToeGame.COMPUTER_PLAYER:
                mTurn = TicTacToeGame.HUMAN_PLAYER;
                mInfoTextView.setText(R.string.first_human);
                break;
        }
        mGameOver = false;
    }

    private void setMove(char player, int location) {

        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER) {
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        } else {
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
        }
    }

    //
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {

            this.location = location;
        }

        public void onClick(View view) {


            if (!mGameOver && mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();

                }
                if(winner==0)
                {
                    mInfoTextView.setText(R.string.turn_human);
                }
                else {
                    if (winner == 1) {
                        countT++;
                        mTiesScore.setText(Integer.toString(countT));
                        mInfoTextView.setText(R.string.result_tie);
                    } else if (winner == 2) {
                        countH++;
                        mHumanScore.setText(Integer.toString(countH));
                        mInfoTextView.setText(R.string.result_human_wins);
                    } else if (winner == 3) {
                        countA++;
                        mAndroidScore.setText(Integer.toString(countA));
                        mInfoTextView.setText(R.string.result_computer_wins);
                    }
                    mGameOver = true;
                }
            }

        }


    }


}