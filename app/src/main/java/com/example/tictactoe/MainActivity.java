package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnReset, btnNewGame;
    private TextView txtPlayerOneScore, txtPlayerTwoScore, txtPlayerOneLabel, txtPlayerTwoLabel;
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
        gridSize = 4;
        activePlayer = false;
        gameEnded = false;
        buttons = new Button[gridSize][gridSize];

        //GET COMPONENTS BY ID
        btnReset = findViewById(R.id.btnReset);
        btnNewGame = findViewById(R.id.btnNewGame);
        txtPlayerOneScore = findViewById(R.id.txtPlayerOneScore);
        txtPlayerTwoScore = findViewById(R.id.txtPlayerTwoScore);
        txtPlayerOneLabel = findViewById(R.id.txtPlayerOneLabel);
        txtPlayerTwoLabel = findViewById(R.id.txtPlayerTwoLabel);
        llGameBoard = findViewById(R.id.llGameBoard);

        //INITIAL HEADER STYLE
        txtPlayerOneLabel.setTypeface(Typeface.DEFAULT_BOLD);

        //CREATE GAME GRID
        View grid = generateGrid(gridSize);
        llGameBoard.addView(grid);

        //BUTTON RESET HANDLER
        btnReset.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });

        //BUTTON NEW GAME HANDLER
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
                buttons[i][j].setOnClickListener(view -> {
                    //GET BUTTON BY ID
                    Button btn = findViewById(view.getId());

                    //SHOULD BUTTON BE CLICKABLE
                    boolean isClickable = btn.getText().toString().length() == 0;
                    if(!isClickable) return;

                    setButtonText(btn);
                    setButtonColor(btn);
                    shouldGameEnd();

                    //GAME DIDN'T END
                    if(!gameEnded){
                        setHeaderType();
                        toggleActivePlayer();
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
    private void setHeaderType(){
        //PLAYER ONE
        if(activePlayer){
            txtPlayerOneLabel.setTypeface(Typeface.DEFAULT_BOLD);
            txtPlayerTwoLabel.setTypeface(null, Typeface.NORMAL);
        }
        //PLAYER TWO
        if(!activePlayer){
            txtPlayerTwoLabel.setTypeface(Typeface.DEFAULT_BOLD);
            txtPlayerOneLabel.setTypeface(null, Typeface.NORMAL);
        }
    }
    private void setButtonText(Button btn){
        //PLAYER ONE
        if(!activePlayer){
            btn.setText("X");
        }
        //PLAYER TWO
        if(activePlayer){
            btn.setText("O");
        }
    }
    private void setButtonColor(Button btn){
        //PLAYER ONE
        if(!activePlayer){
            btn.getBackground().setColorFilter(btn.getContext().getResources().getColor(R.color.player_one), PorterDuff.Mode.MULTIPLY);
        }
        //PLAYER TWO
        if(activePlayer){
            btn.getBackground().setColorFilter(btn.getContext().getResources().getColor(R.color.player_two), PorterDuff.Mode.MULTIPLY);
        }
    }
    private void toggleActivePlayer(){
        activePlayer = !activePlayer;
    }
    private void shouldGameEnd(){
        //CHECK ROWS
        for (int i = 0; i < gridSize; i++) {

        }
        //CHECK COLUMNS

        //CHECK DIAGONALS
    }
}