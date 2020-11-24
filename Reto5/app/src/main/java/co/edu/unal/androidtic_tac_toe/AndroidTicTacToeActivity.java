package co.edu.unal.androidtic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.harding.tictactoe.BoardView;
import edu.harding.tictactoe.TicTacToeGame;



public class AndroidTicTacToeActivity extends AppCompatActivity {
    private int requestCode, resultCode;

    private Intent data;
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


    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT = 2;

    private int mHumanWins = 0;
    private int mComputerWins = 0;
    private int mTies = 0;

    MediaPlayer mHumanMediaPlayer, mComputerMediaPlayer;

    private boolean mSoundOn;
    private SharedPreferences mPrefs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);

        mBoardView.setOnTouchListener(mTouchListener);

        mInfoTextView = (TextView) findViewById(R.id.information);
        mHumanScore = (TextView) findViewById(R.id.scoreHuman);
        mAndroidScore = (TextView) findViewById(R.id.scoreAndroid);
        mTiesScore = (TextView) findViewById(R.id.scoreTies);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean(Setting.SOUND, true);
        mHumanWins = mPrefs.getInt("mHumanWins", 0);
        mComputerWins = mPrefs.getInt("mComputerWins", 0);
        mTies = mPrefs.getInt("mTies", 0);
        mBoardView.setBoardColor(mPrefs.getInt(Setting.BOARD_COLOR, Color.GRAY));

        String difficultyLevel = mPrefs.getString(Setting.DIFFICULTY_LEVEL,
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);

        if (savedInstanceState == null) {
            startNewGame();
        }
        else {

            mGame.setTableState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            mTurnA = savedInstanceState.getChar("mTurn");
            mTurnH = savedInstanceState.getChar("mGoFirst");
            if (!mGameOver && mTurnA == TicTacToeGame.COMPUTER_PLAYER) {
                int move = mGame.getComputerMove();
                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            }
        }

        displayScores();
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

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mHumanWins", mHumanWins);
        ed.putInt("mComputerWins", mComputerWins);
        ed.putInt("mTies", mTies);
        ed.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharArray("board", mGame.getTableState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putChar("mGoFirst", mTurnH);
        outState.putChar("mTurn", mTurnA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_QUIT_ID:
                        builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, (dialog1, id1) -> AndroidTicTacToeActivity.this.finish())
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;
            case DIALOG_ABOUT:
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Setting.class), 0);
                return true;
            case R.id.reset_scores:
                mHumanWins = 0;
                mComputerWins = 0;
                mTies = 0;
                displayScores();
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT);
                return true;

        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
this.requestCode=requestCode;
this.resultCode=resultCode;
this.data=data;
        if (requestCode == RESULT_CANCELED) {

            mSoundOn = mPrefs.getBoolean(Setting.SOUND, true);

            String difficultyLevel = mPrefs.getString(Setting.DIFFICULTY_LEVEL,
                    getResources().getString(R.string.difficulty_harder));
            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);

            String goes_first = mPrefs.getString(Setting.GOES_FIRST, "Alternate");
            if (!goes_first.equals("Alternate")) {

                for (int i = 0; i < 8; i++)
                    if (mGame.getBoardOccupant(i) != TicTacToeGame.OPEN_SPOT)
                        return;

                // All spots must be open
                startNewGame();
            }

            mBoardView.setBoardColor(mPrefs.getInt(Setting.BOARD_COLOR, Color.GRAY));
        }
    }

    // Show the scores
    private void displayScores() {
        mHumanScore.setText(Integer.toString(mHumanWins));
        mAndroidScore.setText(Integer.toString(mComputerWins));
        mTiesScore.setText(Integer.toString(mTies));
    }

    // Set up the game board.
    private void startNewGame() {

        mGame.clearBoard();
        mBoardView.invalidate();   // Redraw the board


        String goesFirst = mPrefs.getString(Setting.GOES_FIRST, "Alternate");

        if (goesFirst.equals("Alternate")) {
                if (mTurnH == TicTacToeGame.COMPUTER_PLAYER) {
                mTurnH = TicTacToeGame.HUMAN_PLAYER;
                mTurnA = TicTacToeGame.COMPUTER_PLAYER;
            }
            else {
                mTurnH = TicTacToeGame.COMPUTER_PLAYER;
                mTurnA = TicTacToeGame.HUMAN_PLAYER;
            }
        }
        else if (goesFirst.equals("Human"))
            mTurnA = TicTacToeGame.HUMAN_PLAYER;
        else
            mTurnA = TicTacToeGame.COMPUTER_PLAYER;

        if (mTurnA == TicTacToeGame.COMPUTER_PLAYER) {
            mInfoTextView.setText(R.string.first_android);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        }
        else
            mInfoTextView.setText(R.string.first_human);

        mGameOver = false;
    }
    public void menu(View v) {
        startNewGame();
    }



    public void menuQuit(View v) {
        showDialog(DIALOG_QUIT_ID);
    }
    // Make a move
    private boolean setMove(char player, int location) {

        if (player == TicTacToeGame.COMPUTER_PLAYER) {

            final int loc = location;
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, loc);
                mBoardView.invalidate();   // Redraw the board

                try {
                    if (mSoundOn)
                        mComputerMediaPlayer.start();
                }
                catch (IllegalStateException e) {};

                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mTurnA = TicTacToeGame.HUMAN_PLAYER;
                    mInfoTextView.setText(R.string.turn_human);
                }
                else
                    endGame(winner);
            }, 1000);

            return true;
        }
        else if (mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location)) {
            mTurnA = TicTacToeGame.COMPUTER_PLAYER;
            mBoardView.invalidate();   // Redraw the board
            if (mSoundOn)
                mHumanMediaPlayer.start();
            return true;
        }

        return false;
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
            String defaultMessage = getResources().getString(R.string.result_human_wins);
            mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
        }
        else if (winner == 3) {
            mComputerWins++;
            mAndroidScore.setText(Integer.toString(mComputerWins));
            mInfoTextView.setText(R.string.result_computer_wins);
        }

        mGameOver = true;
    }


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (!mGameOver && mTurnA == TicTacToeGame.HUMAN_PLAYER &&
                    setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {

                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                }
                else
                    endGame(winner);
            }

            return false;
        }
    };
}