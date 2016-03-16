package com.gamefinder;

/**
 *
 */
public class GameRatingAlgorithm {
    public static int rateGame(int team1Fav, int team2Fav, int sport1Fav, int sport2Fav, boolean inProgress) {
        int rating = 0;

        if ((team1Fav == 1) && (team2Fav == 1)) {
            return 100;
        } else if (team1Fav == 1) {
            return 88 + (3 * sport1Fav);
        } else if (team2Fav == 1) {
            return 88 + (3 * sport2Fav);
        }



        if (inProgress) {

        }

        return rating;
    }
}