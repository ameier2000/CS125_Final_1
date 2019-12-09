package com.example.cs125final;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
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
    private int firstplayer = 1;
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
        Button pOne = findViewById(R.id.playerOneWinner);
        Button pTwo = findViewById(R.id.playerTwoWinner);
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


        pot = 500;
        playerOneSum -= 250;
        playerTwoSum -= 250;
        updateValues(turn);
        bet.setOnClickListener(v -> {
            if(input.getText().toString().isEmpty()) {
                questions.setText("            Improper Value Go Again");
                return;
            }
            prevBet = Integer.parseInt(input.getText().toString());
            input.getText().clear();

            InputMethodManager inputMan = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMan.hideSoftInputFromWindow(input.getWindowToken(), 0);
            if (prevBet == null || prevBet < 250 || prevBet > playerTwoSum || prevBet > playerOneSum) {
                prevBet = 0;
                questions.setText("             Improper Value go Again");
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
                questions.setText("           Improper Value Go Again");
                return;
            }
            prevRaise = Integer.parseInt(input.getText().toString());
            input.getText().clear();

            InputMethodManager inputMan = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMan.hideSoftInputFromWindow(input.getWindowToken(), 0);
            if (prevRaise == null || prevRaise <  prevBet || prevRaise > playerTwoSum || prevRaise > playerOneSum) {
                prevRaise = 0;
                questions.setText("           Improper Value Go Again");
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
            boolean regular = true;

            if (turn == 1) {
                playerOneLast = 4;
                if (playerTwoLast == 4) {
                    playerTwoLast = 0;
                    playerOneLast = 0;
                    phase++;
                    if (phase > 3) {

                        // showdown
                        raise.setVisibility(View.INVISIBLE);
                        call.setVisibility(View.INVISIBLE);
                        bet.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.INVISIBLE);
                        fold.setVisibility(View.INVISIBLE);
                        pOne.setVisibility(View.VISIBLE);
                        pTwo.setVisibility(View.VISIBLE);
                        regular = false;

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
                        raise.setVisibility(View.INVISIBLE);
                        call.setVisibility(View.INVISIBLE);
                        bet.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.INVISIBLE);
                        fold.setVisibility(View.INVISIBLE);
                        pOne.setVisibility(View.VISIBLE);
                        pTwo.setVisibility(View.VISIBLE);
                        regular = false;
                    }
                }
                turn = 1;
            }
            updateValues(turn);
            if (regular) {
                raise.setVisibility(View.INVISIBLE);
                call.setVisibility(View.INVISIBLE);
                bet.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
            }
        });
        call.setOnClickListener(v -> {

            System.out.println(" was pressed");

            if (turn == 1) {
                pot += prevBet;
                playerOneSum -= prevBet;
                prevBet = 0;
                phase++;
                if (phase > 3) {
                    raise.setVisibility(View.INVISIBLE);
                    call.setVisibility(View.INVISIBLE);
                    bet.setVisibility(View.INVISIBLE);
                    check.setVisibility(View.INVISIBLE);
                    fold.setVisibility(View.INVISIBLE);
                    pOne.setVisibility(View.VISIBLE);
                    pTwo.setVisibility(View.VISIBLE);
                    return;
                }
                turn = 2;
            } else {
                pot += prevBet;
                playerTwoSum -= prevBet;
                prevBet = 0;
                phase++;
                if (phase > 3) {
                    raise.setVisibility(View.INVISIBLE);
                    call.setVisibility(View.INVISIBLE);
                    bet.setVisibility(View.INVISIBLE);
                    check.setVisibility(View.INVISIBLE);
                    fold.setVisibility(View.INVISIBLE);
                    pOne.setVisibility(View.VISIBLE);
                    pTwo.setVisibility(View.VISIBLE);
                    return;
                }
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
            phase = 0;
            if (playerOneSum < 250) {
                raise.setVisibility(View.INVISIBLE);
                call.setVisibility(View.INVISIBLE);
                bet.setVisibility(View.INVISIBLE);
                check.setVisibility(View.INVISIBLE);
                fold.setVisibility(View.INVISIBLE);
                pOne.setVisibility(View.INVISIBLE);
                pTwo.setVisibility(View.INVISIBLE);
                questions.setText("Congratulations Player 2, You Are The Winner");
                playinfo.setVisibility(View.INVISIBLE);
                return;
            }
            if (playerTwoSum < 250) {
                raise.setVisibility(View.INVISIBLE);
                call.setVisibility(View.INVISIBLE);
                bet.setVisibility(View.INVISIBLE);
                check.setVisibility(View.INVISIBLE);
                fold.setVisibility(View.INVISIBLE);
                pOne.setVisibility(View.INVISIBLE);
                pTwo.setVisibility(View.INVISIBLE);
                questions.setText("Congratulations Player 1, You Are The Winner");
                playinfo.setVisibility(View.INVISIBLE);
                return;
            }
            raise.setVisibility(View.INVISIBLE);
            call.setVisibility(View.INVISIBLE);
            bet.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
            if (firstplayer == 1) {
                firstplayer = 2;
                turn = 2;
            } else {
                firstplayer = 1;
                turn = 1;
            }
            pot = 500;
            playerOneSum -= 250;
            playerTwoSum -= 250;
            updateValues(turn);
        });
        pOne.setOnClickListener(v -> {
            playerOneSum += pot;
            pot = 0;
            updateValues(turn);
            phase = 0;
            if (playerTwoSum < 250) {
                raise.setVisibility(View.INVISIBLE);
                call.setVisibility(View.INVISIBLE);
                bet.setVisibility(View.INVISIBLE);
                check.setVisibility(View.INVISIBLE);
                fold.setVisibility(View.INVISIBLE);
                pOne.setVisibility(View.INVISIBLE);
                pTwo.setVisibility(View.INVISIBLE);
                questions.setText("Congratulations Player 1, You Are The Winner");
                playinfo.setVisibility(View.INVISIBLE);
                return;
            }
            raise.setVisibility(View.INVISIBLE);
            call.setVisibility(View.INVISIBLE);
            bet.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
            fold.setVisibility(View.VISIBLE);
            pOne.setVisibility(View.INVISIBLE);
            pTwo.setVisibility(View.INVISIBLE);
            pot = 500;
            playerOneSum -= 250;
            playerTwoSum -= 250;
            if (firstplayer == 1) {
                firstplayer = 2;
                turn = 2;
            } else {
                firstplayer = 1;
                turn = 1;
            }
            updateValues(turn);
        });
        pTwo.setOnClickListener(v -> {
            playerTwoSum += pot;
            updateValues(turn);
            phase = 0;
            pot = 0;
            if (playerOneSum < 250) {
                raise.setVisibility(View.INVISIBLE);
                call.setVisibility(View.INVISIBLE);
                bet.setVisibility(View.INVISIBLE);
                check.setVisibility(View.INVISIBLE);
                fold.setVisibility(View.INVISIBLE);
                pOne.setVisibility(View.INVISIBLE);
                pTwo.setVisibility(View.INVISIBLE);
                questions.setText("Congratulations Player 2, You Are The Winner");
                playinfo.setVisibility(View.INVISIBLE);
                return;
            }
            raise.setVisibility(View.INVISIBLE);
            call.setVisibility(View.INVISIBLE);
            bet.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
            fold.setVisibility(View.VISIBLE);
            pOne.setVisibility(View.INVISIBLE);
            pTwo.setVisibility(View.INVISIBLE);
            pot = 500;
            playerOneSum -= 250;
            playerTwoSum -= 250;
            if (firstplayer == 1) {
                firstplayer = 2;
                turn = 2;
            } else {
                firstplayer = 1;
                turn = 1;
            }
            updateValues(turn);
        });
    }

    private void updateValues(int turn) {

         if (turn == 1) {
             TextView playerinfo = findViewById(R.id.playerInfo);
             playerinfo.setText("                      Player 1's Turn");
         }
         else {
             TextView playerinfo = findViewById(R.id.playerInfo);
             playerinfo.setText("                      Player 2's Turn");
         }

         doInBackground();
         TextView playerval = findViewById(R.id.currentPlayerVal);
         playerval.setText("                      P1 $" + playerOneSum.toString() + "         P2 $" + playerTwoSum.toString());
         TextView potval = findViewById(R.id.potVal);
         potval.setText("       Pot: " + pot.toString());
         TextView questions = findViewById(R.id.questions);
         questions.setText("          Bet (Min: 250)   Raise (Min: " + prevBet.toString() + ")");

    }
    public void doInBackground() {
        TextView c = findViewById(R.id.chuckNorris);
        c.setText("Getting new quote...");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.kanye.rest";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String joke = response.getString("quote");
                            c.setText(joke + " - Kanye West");
                        } catch (Exception e) {
                            c.setText(e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        c.setText("error");

                    }
                });
        queue.add(jsonObjectRequest);
        }


    }
