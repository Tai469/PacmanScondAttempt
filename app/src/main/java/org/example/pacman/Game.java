package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 *
 * This class should contain all your game logic
 */

public class Game
{
    //context is a reference to the activity
    private Context context;
    //how many points do we have
    private int points = 0, numCoins = 10;
    //bitmap of the pacman
    private Bitmap pacBitmap, coinBitmap;
    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    //constructor
    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;

        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.euro);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize gold coins also here
    public ArrayList<GoldCoin> getGoldCoins()
    {
        if(coins.size() > 0)
        {
            return coins;
        }
        for (int idx = 0; idx < numCoins; idx++)
        {
            coins.add(getCoin());
        }
        return coins;
    }

    public void newGame()
    {
        coins.clear();
        pacx = 50;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        gameView.invalidate(); //redraw screen
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void movePacmanRight(int pixels)
    {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.getWidth() < w)
        {
            pacx = pacx + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanLeft(int pixels)
    {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.getWidth() < h && pacx + pixels >= 0)
        {
            pacx = pacx - pixels;
            if (pacx <0)
            {
                pacx = 0;
            }
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanUp(int pixels)
    {
        //still within our boundaries?
        if (pacy + pixels + pacBitmap.getHeight() < h && pacy + pixels >= 0)
        {
            pacy = pacy - pixels;
            if (pacy <0)
            {
                pacy = 0;
            }
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanDown(int pixels)
    {
        //still within our boundaries?
        if (pacy + pixels + pacBitmap.getHeight() < h)
        {
            pacy = pacy + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {
        double x1 = this.pacx;
        double y1 = this.pacy;

        for (int idx = 0; idx < coins.size(); idx++)
        {
            if(!coins.get(idx).isTaken())
            {
                double x2 = coins.get(idx).getWidth();
                double y2 = coins.get(idx).getHeight();

                double distance = Math.hypot(x1 - x2, y1 - y2);
                //Log.d("Distance", "The distance is : " + distance + " against coin [" + idx + "]");
                if (distance < 50)
                {
                    coins.get(idx).taken();
                    this.points = this.points + 1;
                    this.pointsView.setText("Points: " + this.points);
                    if(isEveryCoinSelected())
                    {
                        // TODO: 27/09/2018
                        Log.d("GameOver", "doCollisionCheck: on Coins taken and it seems to be 10 of 10! So congratulation ....");
                    }
                    return;
                }
            }
        }
    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getCoinBitmap()
    {
        return coinBitmap;
    }

    private GoldCoin getCoin()
    {
        while(true)
        {
            GoldCoin coin = new GoldCoin(this.h, this.w, this.coinBitmap.getWidth());
            if(isCoinValid(coin))
            {
                return coin;
            }
        }
    }

    private boolean isCoinValid(GoldCoin coin)
    {
        for (int idx = 0; idx < coins.size(); idx++)
        {
            double x1 = coin.getHeight();
            double y1 = coin.getWidth();

            double x2 = coins.get(idx).getHeight();
            double y2 = coins.get(idx).getWidth();

            double distance = Math.hypot(x1-x2, y1-y2);
            //double tmpDistance = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
            //Log.d("Distance", "The distance is : " + distance + " against coin [" + idx + "]");
            if(distance < 100)
            {
                return false;
            }
        }
        return true;
    }

    private boolean isEveryCoinSelected()
    {
        for (int idx = 0; idx < coins.size(); idx++) {
            if(!coins.get(idx).isTaken())
            {
                return false;
            }
        }
        Toast.makeText(this.context,"Congratulation You Won !!!",Toast.LENGTH_LONG).show();
        return true;
    }
}
