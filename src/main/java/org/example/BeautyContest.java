package org.example;

import java.util.*;

public class BeautyContest {
    // Used to track people who have joined the contest and their score
    private final Map<Contestant, Integer> scoreboard;
    private final Map<Contestant, Integer> guesses;

    public BeautyContest(Contestant... contestants) {
        this.guesses = new HashMap<>();
        this.scoreboard = new HashMap<>();
        for (Contestant c : contestants) {
            this.scoreboard.put(c, 10);
        }
    }

    public Integer getContestantScore(Contestant c) {
        return scoreboard.getOrDefault(c, -1);
    }

    public void submitGuess(Contestant c, Integer guess) throws IllegalArgumentException {
        if (!scoreboard.containsKey(c)) {
            throw new IllegalArgumentException("Contestant is not participating in this contest");
        } else if (guesses.containsKey(c)) {
            throw new IllegalArgumentException("Contestant has already submitted a guess");
        } else if (guess < 0 || guess > 100) {
            throw new IllegalArgumentException("Guess must be between 0 and 100");
        }
        guesses.put(c, guess);
    }

    public void evaluateRound() {
        Set<Contestant> losers = new HashSet<>();
        long remainingContestants = scoreboard.values().stream().filter(x -> x > 0).count();
        if (remainingContestants <= 4) { // Remove duplicate guesses if 4 or fewer people remain
            Set<Integer> encountered = new HashSet<>();
            Set<Integer> duplicateGuesses = new HashSet<>();
            for (Contestant c : guesses.keySet()) {
                Integer guess = guesses.get(c);
                if (encountered.contains(guess)) {
                    duplicateGuesses.add(guess);
                } else {
                    encountered.add(guess);
                }
            }
            for (Contestant c : scoreboard.keySet()) {
                if (duplicateGuesses.contains(guesses.get(c))) {
                    losers.add(c);
                    guesses.remove(c);
                }
            }
        }

        int sum = 0;
        for (Integer guess : guesses.values()) {
            sum += guess;
        }
        final float average = (float) sum / guesses.size();
        final float target = average * 0.8f;

        int penalty = 1;
        if (remainingContestants <= 3) { // Double penalty if a contestant has "exactly" guessed the target
            // Page 4 of ch 51.3 shows that the "target" is rounded to an int for sake of an exact match
            final int roundedTarget = Math.round(target);
            if (guesses.containsValue(roundedTarget)) {
                penalty *= 2;
            }
        }

        // Used to prevent both contestants being considered losers in the following special scenario
        boolean evaluateClosestGuess = true;
        if (remainingContestants <= 2) {
            // If one contestant chooses 100 and the other person chooses 0
            // the person who chooses 100 wins the round (not the contestant)
            if (guesses.containsValue(0) && guesses.containsValue(100)) {
                evaluateClosestGuess = false;
                for (Contestant c : guesses.keySet()) {
                    if (guesses.get(c) == 0) {
                        losers.add(c);
                    }
                }
            }
        }

        if (evaluateClosestGuess) { // Special scenario with two contestants not encountered, evaluate who is closest
            float smallestDiff = 777;
            for (Integer guess : guesses.values()) {
                smallestDiff = Math.min(smallestDiff, Math.abs(target - guess));
            }

            for (Contestant c : guesses.keySet()) {
                final float diff = Math.abs(guesses.get(c) - target);
                if (diff != smallestDiff) {
                    losers.add(c);
                }
            }
        }

        for (Contestant loser : losers) {
            scoreboard.replace(loser, scoreboard.get(loser) - penalty);
        }
    }

    public boolean finished() {
        for (Integer score : scoreboard.values()) {
            if (score > 0) {
                return false;
            }
        }
        return true;
    }
}
