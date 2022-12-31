package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeautyContestTest {

    @Test
    void initializesNewContestantsWithTenPoints() {
        Contestant c = new Contestant("Montgomery Watts");
        BeautyContest contest = new BeautyContest(c);
        assertEquals(10, contest.getContestantScore(c));
    }

    @Test
    void returnsNegativeOneForContestantNotAddedToContest() {
        BeautyContest contest = new BeautyContest();
        Contestant c = new Contestant("John Doe");
        assertEquals(-1, contest.getContestantScore(c));
    }

    @Test
    void throwsExceptionIfGuessIsItOfBounds() {
        Contestant c = new Contestant("John");
        BeautyContest contest = new BeautyContest(c);

        Throwable belowZeroThrowable = assertThrows(
                IllegalArgumentException.class,
                () -> contest.submitGuess(c, -1),
                "submitGuess failed to throw an exception when receiving a guess less than 0");
        assertEquals("Guess must be between 0 and 100", belowZeroThrowable.getMessage());

        Throwable aboveOneHundredThrowable = assertThrows(
                IllegalArgumentException.class,
                () -> contest.submitGuess(c, 101),
                "submitGuess failed to throw an exception when receiving a guess greater than 100");
        assertEquals("Guess must be between 0 and 100", aboveOneHundredThrowable.getMessage());
    }

    @Test
    void acceptsGuessWithinBounds() {
        Contestant billy = new Contestant("Billy");
        Contestant johnny = new Contestant("Johnny");
        Contestant jackie = new Contestant("Jackie");
        BeautyContest contest = new BeautyContest(billy, johnny, jackie);

        contest.submitGuess(billy, 0);
        contest.submitGuess(johnny, 50);
        contest.submitGuess(jackie, 100);
    }

    @Test
    void onlyAllowsContestantToGuessOnce() {
        Contestant c = new Contestant("John");
        BeautyContest contest = new BeautyContest(c);

        contest.submitGuess(c, 50);
        Throwable multiGuessThrowable = assertThrows(
                IllegalArgumentException.class,
                () -> contest.submitGuess(c, 50),
                "submitGuess failed to throw an exception when receiving more than one guess from a contestant");
        assertEquals("Contestant has already submitted a guess", multiGuessThrowable.getMessage());
    }

    @Test
    void contestantCannotSubmitGuessWithoutJoiningContest() {
        BeautyContest contest = new BeautyContest();
        Contestant c = new Contestant("John");
        Throwable outsiderThrowable = assertThrows(
                IllegalArgumentException.class,
                () -> contest.submitGuess(c, 50),
                "submitGuess failed to throw an exception when receiving a guess from a contestant who hasn't joined the contest");
        assertEquals("Contestant is not participating in this contest", outsiderThrowable.getMessage());
    }

    @Test
    void twoPersonRuleWorks() {
        Contestant loser = new Contestant("Loser");
        Contestant winner = new Contestant("Winner");
        BeautyContest contest = new BeautyContest(loser, winner);

        contest.submitGuess(loser, 0);
        contest.submitGuess(winner, 100);
        contest.evaluateRound();

        assertEquals(9, contest.getContestantScore(loser));
        assertEquals(10, contest.getContestantScore(winner));
    }

    // The following tests assert that the results are the same as what's shown in the manga.
    @Test
    void scenarioFromFirstRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 30);
        contest.submitGuess(blondie, 32);
        contest.submitGuess(king, 29);
        contest.submitGuess(lady, 40);
        contest.submitGuess(pompadour, 33);

        contest.evaluateRound();

        assertEquals(10, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromSecondRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 16);
        contest.submitGuess(blondie, 17);
        contest.submitGuess(king, 14);
        contest.submitGuess(lady, 21);
        contest.submitGuess(pompadour, 15);

        contest.evaluateRound();

        assertEquals(10, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromThirdRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 3);
        contest.submitGuess(blondie, 7);
        contest.submitGuess(king, 5);
        contest.submitGuess(lady, 11);
        contest.submitGuess(pompadour, 7);

        contest.evaluateRound();

        assertEquals(10, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromFourthRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 0);
        contest.submitGuess(blondie, 2);
        contest.submitGuess(king, 1);
        contest.submitGuess(lady, 4);
        contest.submitGuess(pompadour, 0);

        contest.evaluateRound();

        assertEquals(10, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromFifthRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 0);
        contest.submitGuess(blondie, 100);
        contest.submitGuess(king, 0);
        contest.submitGuess(lady, 1);
        contest.submitGuess(pompadour, 0);

        contest.evaluateRound();

        assertEquals(9, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(10, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromSixthRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 0);
        contest.submitGuess(blondie, 25);
        contest.submitGuess(king, 17);
        contest.submitGuess(lady, 100);
        contest.submitGuess(pompadour, 5);

        contest.evaluateRound();

        assertEquals(9, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(10, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromSeventhRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 0);
        contest.submitGuess(blondie, 100);
        contest.submitGuess(king, 10);
        contest.submitGuess(lady, 30);
        contest.submitGuess(pompadour, 4);

        contest.evaluateRound();

        assertEquals(9, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(10, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromEighthRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 36);
        contest.submitGuess(blondie, 20);
        contest.submitGuess(king, 20);
        contest.submitGuess(lady, 10);
        contest.submitGuess(pompadour, 34);

        contest.evaluateRound();

        assertEquals(10, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(10, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromNinthRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 2);
        contest.submitGuess(blondie, 6);
        contest.submitGuess(king, 10);
        contest.submitGuess(lady, 8);
        contest.submitGuess(pompadour, 20);

        contest.evaluateRound();

        assertEquals(9, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(10, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromTenthRound() {
        Contestant oldMan = new Contestant("Old Man");
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        Contestant pompadour = new Contestant("Pompadour");
        BeautyContest contest = new BeautyContest(oldMan, blondie, king, lady, pompadour);

        contest.submitGuess(oldMan, 0);
        contest.submitGuess(blondie, 1);
        contest.submitGuess(king, 2);
        contest.submitGuess(lady, 7);
        contest.submitGuess(pompadour, 0);

        contest.evaluateRound();

        assertEquals(10, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(oldMan));
        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(lady));
        assertEquals(9, contest.getContestantScore(pompadour));
    }

    @Test
    void scenarioFromDuplicateRuleExplanation() {
        Contestant guessesSeven = new Contestant("Blondie");
        Contestant alsoGuessesSeven = new Contestant("King of Diamonds");
        Contestant doesNotGuessSeven = new Contestant("Lady");
        BeautyContest contest = new BeautyContest(guessesSeven, alsoGuessesSeven, doesNotGuessSeven);

        contest.submitGuess(guessesSeven, 7);
        contest.submitGuess(alsoGuessesSeven, 7);
        contest.submitGuess(doesNotGuessSeven, 32);

        contest.evaluateRound();

        assertEquals(9, contest.getContestantScore(guessesSeven));
        assertEquals(9, contest.getContestantScore(alsoGuessesSeven));
        assertEquals(10, contest.getContestantScore(doesNotGuessSeven));
    }

    @Test
    void scenarioFromExactMatchExplanation() {
        Contestant guessesFour = new Contestant("Blondie");
        Contestant guessesFifty = new Contestant("King of Diamonds");
        Contestant guessesExactMatch = new Contestant("Lady");
        BeautyContest contest = new BeautyContest(guessesFour, guessesFifty, guessesExactMatch);

        contest.submitGuess(guessesFour, 4);
        contest.submitGuess(guessesFifty, 50);
        contest.submitGuess(guessesExactMatch, 19);

        contest.evaluateRound();

        assertEquals(8, contest.getContestantScore(guessesFour));
        assertEquals(8, contest.getContestantScore(guessesFifty));
        assertEquals(10, contest.getContestantScore(guessesExactMatch));
    }

    @Test
    void scenarioFromEleventhRound() {
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        BeautyContest contest = new BeautyContest(blondie, king, lady);

        contest.submitGuess(blondie, 1);
        contest.submitGuess(king, 1);
        contest.submitGuess(lady, 1);

        contest.evaluateRound();

        assertEquals(9, contest.getContestantScore(blondie));
        assertEquals(9, contest.getContestantScore(king));
        assertEquals(9, contest.getContestantScore(lady));
    }

    @Test
    void scenarioFromTwelfthRound() {
        Contestant blondie = new Contestant("Blondie");
        Contestant king = new Contestant("King of Diamonds");
        Contestant lady = new Contestant("Lady");
        BeautyContest contest = new BeautyContest(blondie, king, lady);

        contest.submitGuess(blondie, 23);
        contest.submitGuess(king, 1);
        contest.submitGuess(lady, 62);

        contest.evaluateRound();

        assertEquals(10, contest.getContestantScore(blondie));
        assertEquals(8, contest.getContestantScore(king));
        assertEquals(8, contest.getContestantScore(lady));
    }
}