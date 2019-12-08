package com.example.cs125final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String playerOneName;
    private String playerTwoName;
    private int playerdealer = 1;
    private int ante = 250;
    private Integer playerOneSum = 5000;
    private Integer playerTwoSum = 5000;
    private int playerOneLast = 0;
    private int playerTwoLast = 0;
    private Integer pot = 0;
    private int turn = 2;
    private int phase = 0;
    private Integer prevBet = 0;
    private Integer prevRaise = 0;
    private boolean gameon = true;
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
        raise.setVisibility(View.INVISIBLE);
        call.setVisibility(View.INVISIBLE);
        TextView potval = findViewById(R.id.potVal);
        potval.setText("       Pot: " + pot.toString());
        TextView playerval = findViewById(R.id.currentPlayerVal);
        playerval.setText("     P1 $" + playerOneSum.toString() + "         P2 $" + playerTwoSum.toString());
        TextView playinfo = findViewById(R.id.playerInfo);
        playinfo.setText("          New Game. Player 2's Turn");
        TextView questions = findViewById(R.id.questions);
        questions.setText("Bet (Min: 250)   Raise (Min: " + prevBet.toString() + ")");
        EditText input = findViewById(R.id.userInput);
        bet.setOnClickListener(v -> {
            if(input.getText().toString().isEmpty()) {
                questions.setText("    Inproper Value go again");
                return;
            }
            prevBet = Integer.parseInt(input.getText().toString());
            input.getText().clear();

            InputMethodManager inputMan = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMan.hideSoftInputFromWindow(input.getWindowToken(), 0);
            if (prevBet == null || prevBet < 250 || prevBet > playerTwoSum || prevBet > playerOneSum) {
                prevBet = 0;
                questions.setText("    Inproper Value go again");
                return;
            }


            if (turn == 1) {
                playerOneSum -= prevBet;
                pot += prevBet;
                turn = 2;
            } else {
                playerTwoSum -= prevBet;
                pot += prevBet;
                turn = 1;
            }
            updateValues(turn);
            raise.setVisibility(View.VISIBLE);
            call.setVisibility(View.VISIBLE);
            bet.setVisibility(View.INVISIBLE);
            check.setVisibility(View.INVISIBLE);

        });
        raise.setOnClickListener(v -> {
            if(input.getText().toString().isEmpty()) {
                questions.setText("    Inproper Value go again");
                return;
            }
            prevRaise = Integer.parseInt(input.getText().toString());
            input.getText().clear();

            InputMethodManager inputMan = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMan.hideSoftInputFromWindow(input.getWindowToken(), 0);
            if (prevRaise == null || prevRaise <  prevBet || prevRaise > playerTwoSum || prevRaise > playerOneSum) {
                prevRaise = 0;
                questions.setText("    Inproper Value go again");
                return;
            }

            if (turn == 1) {
                playerOneSum -= prevBet + prevRaise;
                pot += prevBet + prevRaise;
                prevBet = prevRaise;
                turn = 2;
            } else {
                playerTwoSum -= prevBet + prevRaise;
                pot += prevBet + prevRaise;
                prevBet = prevRaise;
                turn = 1;
            }
            updateValues(turn);
            raise.setVisibility(View.VISIBLE);
            call.setVisibility(View.VISIBLE);
            bet.setVisibility(View.INVISIBLE);
            check.setVisibility(View.INVISIBLE);
        });
        check.setOnClickListener(v -> {

            if (turn == 1) {
                playerOneLast = 4;
                if (playerTwoLast == 4) {
                    playerTwoLast = 0;
                    playerOneLast = 0;
                    phase++;
                    if (phase > 3) {
                        // showdown
                    }
                }
                turn = 2;
            } else {
                playerOneLast = 4;
                if (playerOneLast == 4) {
                    playerOneLast = 0;
                    playerTwoLast = 0;
                    phase++;
                    if (phase > 3) {
                        //showdown
                    }
                }
                turn = 1;
            }
            updateValues(turn);
            raise.setVisibility(View.INVISIBLE);
            call.setVisibility(View.INVISIBLE);
            bet.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
        });
        call.setOnClickListener(v -> {

            System.out.println(" was pressed");

            if (turn == 1) {
                pot += prevBet;
                playerOneSum -= prevBet;
                prevBet = 0;
                phase
                turn = 2;
            } else {
                pot += prevBet;
                playerTwoSum -= prevBet;
                prevBet = 0;
                turn = 1;
            }
            updateValues(turn);
            raise.setVisibility(View.INVISIBLE);
            call.setVisibility(View.INVISIBLE);
            bet.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
        });
        fold.setOnClickListener(v -> {

            if (turn == 1) {
                playerTwoSum += pot;
                pot = 0;
                turn = 2;
            } else {
                playerOneSum += pot;
                pot = 0;
                turn = 1;
            }
            updateValues(turn);
            raise.setVisibility(View.INVISIBLE);
            call.setVisibility(View.INVISIBLE);
            bet.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
        });
        restart.setOnClickListener(v -> {
            System.out.println(" was pressed");
        });
        //playRound();
    }
    private void playRound() {
        if (playerdealer == 1) {
            if(playerOneLast == 0 && playerTwoLast == 0) {
                takeAction(2, true, false, true, false);
            }

        } else {
            if(playerOneLast == 0 && playerTwoLast == 0) {
                takeAction(1, true, false, true, false);
            }
        }

        boolean contin = true;
        if (playerdealer == 1) {
            while (contin) {
                if (turn == 2) {
                    if (playerOneLast == 1) {
                        takeAction(2, false, true, false, true);
                    }
                    if (playerOneLast == 2) {
                        contin = false;
                    }
                    if (playerOneLast == 3) {
                        takeAction(2,false,true,false,true);
                    }
                    if (playerOneLast == 4) {
                        if (playerTwoLast == 4) {
                            contin = false;
                        }
                        takeAction(2, true, false, true, false);
                    }
                }
                if (turn == 1) {
                    if (playerTwoLast == 1) {
                        takeAction(1, false, true, false, true);
                    }
                    if (playerTwoLast == 2) {
                        contin = false;
                    }
                    if (playerTwoLast == 3) {
                        takeAction(1,false,true,false,true);
                    }
                    if (playerTwoLast == 4) {
                        if (playerOneLast == 4) {
                            contin = false;
                        }
                        takeAction(1, true, false, true, false);
                    }
                }
                //run code that starts with player 2;
                //pass back to player 1;
                //check if state is satisfied;
            }
        }
    }
    private void roundEnd(int winner, int pot) {
        phase = 0;
        if (winner == 1) {
            playerOneSum += pot;
        }
        else {
            playerTwoSum += pot;
        }
        // if showdown special case
        if (playerdealer == 1) {
            playerdealer = 2;
        }
        else {
            playerdealer = 1;
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
         if (turn == 1) {
             TextView playerinfo = findViewById(R.id.playerInfo);
             playerinfo.setText("                Player 1's Turn");
         }
         else {
             TextView playerinfo = findViewById(R.id.playerInfo);
             playerinfo.setText("                Player 2's Turn");
         }
         TextView playerval = findViewById(R.id.currentPlayerVal);
         playerval.setText("     P1 $" + playerOneSum.toString() + "         P2 $" + playerTwoSum.toString());
         TextView potval = findViewById(R.id.potVal);
         potval.setText("       Pot: " + pot.toString());
        TextView questions = findViewById(R.id.questions);
        questions.setText("Bet (Min: 250)   Raise (Min: " + prevBet.toString() + ")");

    }
    private void takeAction(int turn, boolean bet, boolean raise, boolean check, boolean call) {
        TextView questions = findViewById(R.id.questions);
        if (turn == 2) {
            questions.setText("Player Two Take Your Action.");
        } else {
            questions.setText("Player One Take Your Action.");
        }

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
        boolean stuck = true;
        while (stuck) {
            betb.setOnClickListener(v -> {
                System.out.println("bet/call was pressed");
                return;
            });
            raiseb.setOnClickListener(v -> {
                System.out.println("raise was pressed");
                return;
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
                return;
            });
        }
    }
    private void getName() {

    }
}