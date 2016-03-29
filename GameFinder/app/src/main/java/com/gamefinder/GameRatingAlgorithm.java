package com.gamefinder;

import java.util.HashMap;

/**
 *
 */
public class GameRatingAlgorithm {
    /**
     * Maps from each sport's name to the average margin in that sport's matches
     */
    public static final HashMap<String, Double> avgMargins;
    static {
        avgMargins = new HashMap<>();
        avgMargins.put("NFL", 10.0);
        avgMargins.put("NBA", 6.0);
        avgMargins.put("MLB", 2.0);
        avgMargins.put("NHL", 2.0);
        avgMargins.put("NCAA Football", 10.0);
        avgMargins.put("NCAA Men's Basketball", 6.0);
        avgMargins.put("North American Soccer", 1.0);
        avgMargins.put("European Soccer", 1.0);
        avgMargins.put("NASCAR Sprint Cup Series", 0.0);
        avgMargins.put("PGA", 0.0);
        avgMargins.put("MMA", 0.0);
    }

    /**
     * This method rates a game based on the parameters
     * There is no distinction between team 1 and team 2, they are interchangeable
     * Several of the parameters are not required depending on which sport is being rated, check the code to determine what is necessary for your sport
     * @param sport The name of the sport
     * @param team1Fav The favorite status of "team 1", [0, 1]
     * @param team2Fav The favorite status of "team 2", [0, 1]
     * @param sportFav The favorite status of the sport, [0, 1, 2, 3, 4]
     * @param inProgress Whether or not the game is currently being played, [true, false]
     * @param spread The spread/betting line for the margin of the game, used to judge closeness of games which have not yet started, no set range but make sure the input is positive
     * @param margin The current margin of the game, used to judge closeness of games which have started, no set range but make sure the input is positive
     * @param combinedWinPct The combined win percentage of the two teams, in range [0.0, 1.0]
     * @param playoffMatch Whether or not the game is a playoff game, [true, false]
     * @param ranking1 The ranking of team 1, only applicable in NCAA FB and NCAA BB, in range [1, 25]
     * @param ranking2 The ranking of team 2, only applicable in NCAA FB and NCAA BB, in range [1, 25]
     * @return
     */
    public static int rateGame(String sport, int team1Fav, int team2Fav, int sportFav,
                               boolean inProgress, double spread, double margin,
                               double combinedWinPct, boolean playoffMatch,
                               int ranking1, int ranking2) {

        // Throw an error if the sport is not supported by the algorithm
        if (!avgMargins.containsKey(sport)) {
            throw new UnsupportedOperationException("This sport is not supported by the algorithm.");
        }

        // Special case for sports where there aren't just 2 competitors, [0, 25, 50, 75, 100]
        if (sport.equals("PGA") || sport.equals("NASCAR Sprint Cup Series")) {
            return sportFav * 25;
        }

        // Special case for either or both team(s) being favorited
        // Both = 100, either = [88, 91, 94, 97, 100] depending on the sport's favorite level
        if ((team1Fav == 1) && (team2Fav == 1)) {
            return 100;
        } else if ((team1Fav == 1) || (team2Fav == 1)) {
            return 88 + (3 * sportFav);
        }

        // Base rating dependent only on the favorite level of the sport, [0, 20, 40, 60, 80]
        double rating = sportFav * 20;

        // +/- 10 based on combined win percentages of the teams, 0% = -10, 100% = +10
        rating += (combinedWinPct - .5) * 20;

        // Factor in the spread or the margin depending on the inProgress value, +/- 10
        if (inProgress && !sport.equals("MMA")) {
            rating += (avgMargins.get(sport) - margin) / avgMargins.get(sport) * 10;
        } else if (!inProgress && !sport.equals("MMA")) {
            rating += (avgMargins.get(sport) - spread) / avgMargins.get(sport) * 10;
        }

        // Multiplier for a playoff match of 1.25
        if (playoffMatch) {
            rating *= 1.25;
        }

        // Special case for sports which have rankings, up to +10 for each ranked team
        if (sport.equals("NCAA Football") || sport.equals("NCAA Men's Basketball")) {
            if (ranking1 != 0) {
                rating += 5 + ((25 - ranking1) / 24);
            }
            if (ranking2 != 0) {
                rating += 5 + ((25 - ranking2) / 24);
            }
        }

        // Make sure that none of the calculations have pushed the rating outside of [0, 100]
        if (rating > 100) {
            rating = 100;
        } else if (rating < 0) {
            rating = 0;
        }

        return (int) rating;
    }
}