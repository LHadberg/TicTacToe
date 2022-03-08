package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnReset, btnNewGame;
    private TextView txtPlayerOneScore, txtPlayerTwoScore;
    private LinearLayout llGameBoard;
    private boolean activePlayer;
    private boolean gameEnded;
    private Button[][] buttons;
    private int gridSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DEFAULT VALUES
        setDefaultValues();

        //GET COMPONENTS BY ID
        setDefaultIds();

        //CREATE GAME GRID
        llGameBoard.addView(generateGrid(gridSize));

        //BUTTON RESET HANDLER
        btnReset.setOnClickListener(view -> handleResetClick());

        //BUTTON NEW GAME HANDLER
        btnNewGame.setOnClickListener(view -> initiateNewGame());
    }

    private void setDefaultValues(){
        gridSize = 4;
        activePlayer = false;
        gameEnded = false;
        buttons = new Button[gridSize][gridSize];
    }

    private void setDefaultIds(){
        btnReset = findViewById(R.id.btnReset);
        btnNewGame = findViewById(R.id.btnNewGame);
        txtPlayerOneScore = findViewById(R.id.txtPlayerOneScore);
        txtPlayerTwoScore = findViewById(R.id.txtPlayerTwoScore);
        llGameBoard = findViewById(R.id.llGameBoard);
    }

    private void handleResetClick() {
        finish();
        startActivity(getIntent());
    }

    private LinearLayout generateGrid(int gridSize) {
        //INITIALIZE NEW LINEARLAYOUT
        LinearLayout grid = new LinearLayout(this);

        //COUNTER FOR IDS
        int counter = 420;

        //DEFINE GRID ATTRIBUTES
        grid.setOrientation(LinearLayout.VERTICAL);
        grid.setWeightSum(gridSize);

        //GRID LAYOUT PARAMS
        LinearLayout.LayoutParams gridLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        grid.setLayoutParams(gridLayoutParams);

        //GENERATE ROWs
        for (int i = 0; i < gridSize; i++) {
            //CREATE NEW ROW
            LinearLayout newRow = new LinearLayout(this);

            //DEFINE ROW ATTRIBUTES
            newRow.setId(LinearLayout.generateViewId());
            newRow.setOrientation(LinearLayout.HORIZONTAL);
            newRow.setWeightSum(gridSize);

            //ROW LAYOUT PARAMS
            LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowLayoutParams.weight = 1;
            newRow.setLayoutParams(rowLayoutParams);

            //GENERATE CELLS
            for (int j = 0; j < gridSize; j++) {
                //CREATE NEW CELL
                buttons[i][j] = new Button(this);

                //BUTTON LAYOUT PARAMS
                LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                size.weight = 1;

                buttons[i][j].setLayoutParams(size);

                //DEFINE CELL ATTRIBUTES
                buttons[i][j].setId(counter);
                buttons[i][j].setTextSize(130);

                //ON CELL CLICK
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //GAME ENDED
                        if (gameEnded) {
                            String winningPlayer = getWinningPlayerName();
                            Toast.makeText(MainActivity.this, "Player " + winningPlayer + " won the match, please start a new game.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //GET BUTTON BY ID
                        Button btn = findViewById(view.getId());

                        //SHOULD BUTTON BE CLICKABLE
                        if (!isButtonClickable(btn)) return;

                        //GAME DIDN'T END
                        if (!gameEnded) {
                            setButtonText(btn);
                            setButtonColor(btn);
                            toggleActivePlayer();
                        }
                        shouldGameEnd();
                    }
                });

                //ADD CELL TO ROW
                newRow.addView(buttons[i][j]);

                //INCREMENT COUNTER FOR CELL ID
                counter++;
            }
            //ADD ROW TO LAYOUT
            grid.addView(newRow);
        }
        return grid;
    }

    private void setButtonText(Button btn) {
        //PLAYER ONE
        if (!activePlayer) {
            btn.setText("X");
        }
        //PLAYER TWO
        if (activePlayer) {
            btn.setText("O");
        }
    }

    private void setButtonColor(Button btn) {
        //PLAYER ONE
        if (!activePlayer) {
            btn.getBackground().setColorFilter(btn.getContext().getResources().getColor(R.color.player_one), PorterDuff.Mode.MULTIPLY);
        }
        //PLAYER TWO
        if (activePlayer) {
            btn.getBackground().setColorFilter(btn.getContext().getResources().getColor(R.color.player_two), PorterDuff.Mode.MULTIPLY);
        }
    }

    private void toggleActivePlayer() {
        activePlayer = !activePlayer;
    }

    private boolean valuesAreSet(String[] array) {
        for (String s : array) {
            if (s.length() == 0) return false;
        }
        return true;
    }

    private boolean verifyAllEquals(List<String> list) {
        return new HashSet<>(list).size() <= 1;
    }

    private boolean isButtonClickable(Button btn) {
        return btn.getText().toString().length() == 0;
    }

    private String getWinningPlayerName() {
        if (activePlayer) {
            return "One";
        } else {
            return "Two";
        }
    }

    private void setWinningPlayerPoints() {
        String winningPlayer = getWinningPlayerName();
        if (winningPlayer.equals("One")) {
            int currentScore = Integer.parseInt(txtPlayerOneScore.getText().toString());
            txtPlayerOneScore.setText(currentScore + 1 + "");
        } else {
            int currentScore = Integer.parseInt(txtPlayerTwoScore.getText().toString());
            txtPlayerTwoScore.setText(currentScore + 1 + "");
        }
    }

    private void handleGameEndState() {
        gameEnded = true;
        setWinningPlayerPoints();
        Toast.makeText(this, "Player " + getWinningPlayerName() + " won the match, congratulations!", Toast.LENGTH_SHORT).show();
    }

    private void shouldGameEnd() {
        //CHECK ROWS
        if (!gameEnded) {
            for (int row = 0; row < gridSize; row++) {
                //STOP ITERATING IF GAME ENDED DURING LAST ITERATION
                if (gameEnded) return;

                //INITIALIZE ARRAY
                String[] cellsInRow = new String[gridSize];

                //FOR CELL IN ROW
                for (int cell = 0; cell < buttons.length; cell++) {
                    //STOP ITERATING IF GAME ENDED DURING LAST ITERATION
                    if (gameEnded) return;

                    //ADD CELL VALUE TO ROW
                    cellsInRow[cell] = buttons[row][cell].getText().toString();
                }
                if (valuesAreSet(cellsInRow)) {
                    if (verifyAllEquals(Arrays.asList(cellsInRow))) {
                        handleGameEndState();
                    }
                }
            }
        }

        //CHECK COLUMNS
        if (!gameEnded) {
            for (int row = 0; row < gridSize; row++) {
                //INITIALIZE ARRAY
                String[] cellsInColumn = new String[gridSize];

                //FOR COLUMN IN GRID
                for (int column = 0; column < gridSize; column++) {
                    //STOP ITERATING IF GAME ENDED DURING LAST ITERATION
                    if (gameEnded) return;

                    //ADD CELL VALUE TO COLUMN
                    cellsInColumn[column] = buttons[column][row].getText().toString();
                }
                if (valuesAreSet(cellsInColumn)) {
                    if (verifyAllEquals(Arrays.asList(cellsInColumn))) {
                        handleGameEndState();
                    }
                }
            }
        }

        //CHECK DESC DIAGONAL
        if (!gameEnded) {
            //INITIALIZE ARRAY
            String[] cellsInDescDiagonal = new String[gridSize];

            //FOR ROW IN GRID
            for (int i = 0; i < gridSize; i++) {
                //STOP ITERATING IF GAME ENDED DURING LAST ITERATION
                if (gameEnded) return;

                cellsInDescDiagonal[i] = buttons[i][i].getText().toString();
            }

            if (valuesAreSet(cellsInDescDiagonal)) {
                if (verifyAllEquals(Arrays.asList(cellsInDescDiagonal))) {
                    handleGameEndState();
                }
            }
        }

        //CHECK ASC DIAGONAL
        if (!gameEnded) {
            //INITIALIZE ARRAY
            String[] cellsInAscDiagonal = new String[gridSize];
            int counter = 0;

            //FOR ROW IN GRID
            for (int i = gridSize; i > 0; i--) {
                //STOP ITERATING IF GAME ENDED DURING LAST ITERATION
                if (gameEnded) return;
                cellsInAscDiagonal[counter] = buttons[(i - 1)][counter].getText().toString();
                counter++;
            }

            if (valuesAreSet(cellsInAscDiagonal)) {
                if (verifyAllEquals(Arrays.asList(cellsInAscDiagonal))) {
                    handleGameEndState();
                }
            }
        }
    }

    private void initiateNewGame() {
        //REMOVE EXISTING GRID
        llGameBoard.removeAllViews();

        gameEnded = false;
        buttons = new Button[gridSize][gridSize];

        //CREATE NEW GRID
        llGameBoard.addView(generateGrid(gridSize));

    }
}