package co.edu.unal.androidtic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView mInfoTextView, mTiesScore, mAndroidScore, mHumanScore;
    //Counts
    private int countT = 0, countH = 0, countA = 0;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT = 2;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about_dialog:
                showDialog(DIALOG_ABOUT);
                return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected = -1;
                if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Easy)
                    selected = 0;
                else if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Harder)
                    selected = 1;
                else if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Expert)
                    selected = 2;
                builder.setSingleChoiceItems(levels, selected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                        switch (item) {
                            case 0:
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                break;
                            case 1:
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                break;
                            case 2:
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                break;
                        }
                        // TODO: Set the diff level of mGame based on which item was selected.


                        // Display the selected difficulty level
                        Toast.makeText(getApplicationContext(), levels[item],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;
            case DIALOG_ABOUT:
                // Show an about dialog box

                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);

                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;
        }

        return dialog;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);


    }
    //


    public void menu(View v) {
        startNewGame();
    }

    public void menuDifficulty(View v) {
        showDialog(DIALOG_DIFFICULTY_ID);
    }

    public void menuQuit(View v) {
        showDialog(DIALOG_QUIT_ID);
    }

    private void startNewGame() {

        mGame.clearBoard();
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        switch (mTurn) {
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
            if (winner == 0) {
                mInfoTextView.setText(R.string.turn_human);
            } else {
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