package com.example.cs125final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String playerOneName;
    private String playerTwoName;
    private int playerbigBlind = 1;
    private Integer playerOneSum = 5000;
    private Integer playerTwoSum = 5000;
    private int playerOneLast = 0;
    private int playerTwoLast = 0;
    private Integer pot = 0;
    private int turn = 2;
    private int phase = 0;
    private int prevBet = 0;
    // code for last action 1 = bet; 2 = call; 3 = raise; 4 = check; 5 = fold;
    // Big blind 250 small 125, open to change
    // use .clear to clear edittext

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bet = findViewById(R.id.betButton);
        Button raise = findViewById(R.id.raiseButton);
        Button check = findViewById(R.id.checkButton);
        Button call = findViewById(R.id.callButton);
        Button fold = findViewById(R.id.foldButton);
        Button restart = findViewById(R.id.startoverButton);
        bet.setOnClickListener(v -> {
            System.out.println("bet/call was pressed");
        });
        raise.setOnClickListener(v -> {
            System.out.println("raise was pressed");
        });
        check.setOnClickListener(v -> {
            System.out.println("check was pressed");
        });
        call.setOnClickListener(v -> {
            System.out.println("fold was pressed");
        });
        fold.setOnClickListener(v -> {
            System.out.println("quit was pressed");
        });
        restart.setOnClickListener(v -> {
            System.out.println("restart was pressed");
        });
    }
    private void playRound() {
        boolean contin= true;
        if (playerbigBlind == 1) {
            while (contin) {
                if (turn == 2) {

                }
                //run code that starts with player 2;
                //pass back to player 1;
                //check if state is satisfied;
            }
        }
    }
    private void roundEnd(int winner, int pot) {
        if (winner == 1) {
            playerOneSum += pot;
        }
        else {
            playerTwoSum += pot;
        }
        // if showdown special case
        if (playerbigBlind == 1) {
            playerbigBlind = 2;
        }
        else {
            playerbigBlind = 1;
        }
        if (playerOneSum == 0) {
            // end game;
        }
        if (playerTwoSum == 0) {
            // end game;
        }
        //call playRound
    }
    private void updateValues(int turn) {
         TextView potV = findViewById(R.id.potVal);
         potV.setText(pot.toString());
         if (turn == 1) {
             TextView playerOneVal = findViewById(R.id.currentPlayerVal);
             playerOneVal.setText(playerOneSum.toString());
             if (playerbigBlind == 1) {
                 TextView playerInfo = findViewById(R.id.playerInfo);
                 playerInfo.setText(playerOneName + ": Big Blind");
             } else {
                 TextView playerInfo = findViewById(R.id.playerInfo);
                 playerInfo.setText(playerOneName + ": Small Blind");
             }
         }
         else {
             TextView playerTwoVal = findViewById(R.id.currentPlayerVal);
             playerTwoVal.setText(playerTwoSum.toString());

             if (playerbigBlind == 2) {
                 TextView playerInfo = findViewById(R.id.playerInfo);
                 playerInfo.setText(playerTwoName + ": Big Blind");
             } else {
                 TextView playerInfo = findViewById(R.id.playerInfo);
                 playerInfo.setText(playerTwoName + ": Small Blind");
             }
         }

    }
    private void takeAction(int turn, boolean bet, boolean raise, boolean check, boolean call) {
        if (bet == false) {
            Button betb = findViewById(R.id.betButton);
            betb.setVisibility(View.INVISIBLE);
        } else {
            Button betb = findViewById(R.id.betButton);
            betb.setVisibility(View.VISIBLE);
        }
        if (raise == false) {
            Button raiseb = findViewById(R.id.raiseButton);
            raiseb.setVisibility(View.INVISIBLE);
        } else {
            Button raiseb = findViewById(R.id.raiseButton);
            raiseb.setVisibility(View.INVISIBLE);
        }
        if (check == false) {
            Button checkb = findViewById(R.id.checkButton);
            checkb.setVisibility(View.INVISIBLE);
        } else {
            Button checkb = findViewById(R.id.checkButton);
            checkb.setVisibility(View.INVISIBLE);
        }
        if (call == false) {
            Button callb = findViewById(R.id.callButton);
            callb.setVisibility(View.INVISIBLE);
        } else {
            Button callb = findViewById(R.id.callButton);
            callb.setVisibility(View.VISIBLE);
        }
        Button betb = findViewById(R.id.betButton);
        Button raiseb = findViewById(R.id.raiseButton);
        Button checkb = findViewById(R.id.checkButton);
        Button callb = findViewById(R.id.callButton);
        Button foldb = findViewById(R.id.foldButton);
        betb.setOnClickListener(v -> {
            System.out.println("bet/call was pressed");
        });
        raiseb.setOnClickListener(v -> {
            System.out.println("raise was pressed");
        });
        checkb.setOnClickListener(v -> {
            if (turn == 2) {
                playerTwoLast = 4;
                return;
            } else {
                playerOneLast = 4;
                return;
            }
        });
        callb.setOnClickListener(v -> {
            if (turn == 2) {
                playerTwoSum -= prevBet;
                pot += prevBet;
                prevBet = 0;
                phase++;
                return;
            }
        });
        foldb.setOnClickListener(v -> {
            int winner = 0;
            if (turn == 2) {
                winner = 1;
            } else {
                winner = 2;
            }
            roundEnd(winner, pot);
        });
    }
    private void getName() {

    }
}
