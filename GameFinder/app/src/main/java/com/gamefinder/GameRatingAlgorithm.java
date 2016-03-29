package com.gamefinder;

import java.util.HashMap;

/**
 *
 */
public class GameRatingAlgorithm {
    /**
     * Maps from each sport's name to the average score in that sport's matches
     */
    public static final HashMap<String, Double> avgScores;
    static {
        avgScores = new HashMap<>();
        avgScores.put("NFL", 0.0);
        avgScores.put("NBA", 0.0);
        avgScores.put("MLB", 0.0);
        avgScores.put("NHL", 0.0);
        avgScores.put("NCAA Football", 0.0);
        avgScores.put("NCAA Men's Basketball", 0.0);
        avgScores.put("North American Soccer", 0.0);
        avgScores.put("European Soccer", 0.0);
        avgScores.put("NASCAR Sprint Cup Series", 0.0);
        avgScores.put("PGA", 0.0);
        avgScores.put("MMA", 0.0);
    }

    public static int rateGame(String sport, int team1Fav, int team2Fav, int sport1Fav, int sport2Fav, boolean inProgress, double spread, double margin) {
        if (!avgScores.containsKey(sport)) {
            throw new UnsupportedOperationException("This sport is not supported by the algorithm.");
        }
        int rating = 0;

        if ((team1Fav == 1) && (team2Fav == 1)) {
            return 100;
        } else if (team1Fav == 1) {
            return 88 + (3 * sport1Fav);
        } else if (team2Fav == 1) {
            return 88 + (3 * sport2Fav);
        }



        if (inProgress) {

        } else {

        }

        return rating;
    }
}