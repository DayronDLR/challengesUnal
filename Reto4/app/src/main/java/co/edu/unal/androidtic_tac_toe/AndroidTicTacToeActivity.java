package co.edu.unal.androidtic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.harding.tictactoe.BoardView;
import edu.harding.tictactoe.TicTacToeGame;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    private boolean mGameOver = false;
    //Represents the Class TicTacToeGame
    private TicTacToeGame mGame;
    // Represents the game board
    private BoardView mBoardView;
    // Buttons making up the board
    private Button mBoardButtons[];
    //Turn
    private char mTurnA = TicTacToeGame.COMPUTER_PLAYER;
    private char mTurnH = TicTacToeGame.HUMAN_PLAYER;
    // Various text displayed
    private TextView mInfoTextView, mTiesScore, mAndroidScore, mHumanScore;
    //Counts
    private int countT = 0, countH = 0, countA = 0;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT = 2;

    private int mHumanWins = 0;
    private int mComputerWins = 0;
    private int mTies = 0;

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        mInfoTextView = (TextView) findViewById(R.id.information);
        mAndroidScore = (TextView) findViewById(R.id.scoreAndroid);
        mHumanScore = (TextView) findViewById(R.id.scoreHuman);
        mTiesScore = (TextView) findViewById(R.id.scoreTies);


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

        mBoardView.invalidate();   // Redraw the board

        if (mTurnH == TicTacToeGame.COMPUTER_PLAYER) {
            mTurnH = TicTacToeGame.HUMAN_PLAYER;
            mTurnA = TicTacToeGame.COMPUTER_PLAYER;
            mInfoTextView.setText(R.string.first_android);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        }
        else {
            mTurnH = TicTacToeGame.COMPUTER_PLAYER;
            mTurnA = TicTacToeGame.HUMAN_PLAYER;
            mInfoTextView.setText(R.string.first_human);
        }
        mGameOver = false;
    }
    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beepx);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beepo);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }
    private void endGame(int winner) {
        if (winner == 1) {
            mTies++;
            mTiesScore.setText(Integer.toString(mTies));
            mInfoTextView.setText(R.string.result_tie);
        }
        else if (winner == 2) {
            mHumanWins++;
            mHumanScore.setText(Integer.toString(mHumanWins));
            mInfoTextView.setText(R.string.result_human_wins);
        }
        else if (winner == 3) {
            mComputerWins++;
            mAndroidScore.setText(Integer.toString(mComputerWins));
            mInfoTextView.setText(R.string.result_computer_wins);
        }

        mGameOver = true;
    }
    private boolean setMove(char player, int location) {

        if (player == TicTacToeGame.COMPUTER_PLAYER) {


            final int loc = location;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, loc);
                    mBoardView.invalidate();   // Redraw the board
                    mComputerMediaPlayer.start();

                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mTurnA = TicTacToeGame.HUMAN_PLAYER;
                        mInfoTextView.setText(R.string.turn_human);
                    }
                    else
                        endGame(winner);
                }
            }, 1000);
            return true;
        }
        else if (mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location)) {
            mTurnA = TicTacToeGame.COMPUTER_PLAYER;
            mBoardView.invalidate();   // Redraw the board
            mHumanMediaPlayer.start();
            return true;
        }

        return false;
    }

//



    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (!mGameOver && mTurnA == TicTacToeGame.HUMAN_PLAYER &&
                    setMove(TicTacToeGame.HUMAN_PLAYER, pos))	{

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                }
                else
                    endGame(winner);

            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };



}