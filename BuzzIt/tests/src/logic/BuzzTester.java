package logic;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RunWith(GdxTestRunner.class)
public class BuzzTester {
    private final String defaultQuestion = "Question?";
    private final String defaultCorrect  = "This is correct";
    private final com.buzzit.logic.Difficulty defaultDifficulty = com.buzzit.logic.Difficulty.MEDIUM;
    private final com.buzzit.logic.Category defaultCategory = com.buzzit.logic.Category.HISTORY;

    /***** Category.java *****/
    @Test
    public void testCategoryProperties() {
        assertEquals(0, com.buzzit.logic.Category.SPORTS.getIndex());
        assertEquals("SPORTS", com.buzzit.logic.Category.SPORTS.getName());

        assertEquals(1, com.buzzit.logic.Category.HISTORY.getIndex());
        assertEquals("HISTORY", com.buzzit.logic.Category.HISTORY.getName());

        assertEquals(2, com.buzzit.logic.Category.SCIENCE.getIndex());
        assertEquals("SCIENCE", com.buzzit.logic.Category.SCIENCE.getName());

        assertEquals(3, com.buzzit.logic.Category.MUSIC.getIndex());
        assertEquals("MUSIC", com.buzzit.logic.Category.MUSIC.getName());


        assertEquals(com.buzzit.logic.Category.SPORTS, com.buzzit.logic.Category.getCategory(com.buzzit.logic.Category.SPORTS.getName()));
        assertEquals(com.buzzit.logic.Category.HISTORY, com.buzzit.logic.Category.getCategory(com.buzzit.logic.Category.HISTORY.getName()));
        assertEquals(com.buzzit.logic.Category.SCIENCE, com.buzzit.logic.Category.getCategory(com.buzzit.logic.Category.SCIENCE.getName()));
        assertEquals(com.buzzit.logic.Category.MUSIC, com.buzzit.logic.Category.getCategory(com.buzzit.logic.Category.MUSIC.getName()));
        assertEquals(null, com.buzzit.logic.Category.getCategory("Not a category name"));
    }


    /***** Difficulty.java *****/
    @Test
    public void testDifficultyProperties() {
        assertEquals(5, com.buzzit.logic.Difficulty.EASY.getPoints());
        assertEquals(10, com.buzzit.logic.Difficulty.MEDIUM.getPoints());
        assertEquals(20, com.buzzit.logic.Difficulty.HARD.getPoints());
    }


    @Test public void testConvert() {
        assertEquals(com.buzzit.logic.Difficulty.EASY, com.buzzit.logic.Difficulty.convert(com.buzzit.logic.Difficulty.EASY.toString()));
        assertEquals(com.buzzit.logic.Difficulty.MEDIUM, com.buzzit.logic.Difficulty.convert(com.buzzit.logic.Difficulty.MEDIUM.toString()));
        assertEquals(com.buzzit.logic.Difficulty.HARD, com.buzzit.logic.Difficulty.convert(com.buzzit.logic.Difficulty.HARD.toString()));
        assertEquals(null, com.buzzit.logic.Difficulty.convert("not convertible"));
    }


    /***** Question.java *****/
    @Test
    public void testQuestionProperties() {
        com.buzzit.logic.Question question = getDefaultQuestion();

        assertEquals(defaultQuestion, question.getQuestion());
        assertEquals(defaultCorrect, question.getCorrect());
        assertEquals(defaultDifficulty, question.getDifficulty());
        assertEquals(defaultCategory, question.getCategory());

        ArrayList<String> wrong = question.getWrong();
        for (int i = 0; i < question.getWrong().size(); i++) {
            assertEquals("This is wrong" + (i + 1), wrong.get(i));
        }
    }


    @Test
    public void testOptionsUnrepeated() {
        com.buzzit.logic.Question question = getDefaultQuestion();
        String[] options = question.generateOptions();

        for (int i = 0; i < options.length; i++) {
            for (int j = i + 1; j < options.length; j++) {
                assertNotEquals(options[i], options[j]);
            }
        }
    }


    @Test
    public void testOptionsRandomized() {
        // Generate options enough times, to verify that not a option is not always in the same index
        com.buzzit.logic.Question question = getDefaultQuestion();
        String[] options = question.generateOptions();
        String testString = options[0];
        boolean different = false;

        for (int i = 0; i < 300; i++) {
            options = question.generateOptions();

            if (testString != options[0]) {
                different = true;
                break;
            }
        }

        assertTrue(different);
    }


    /***** Player.java *****/
    @Test
    public void testPlayerProperties() {
        com.buzzit.logic.Player player = new com.buzzit.logic.Player("Gamer");

        assertEquals("Gamer", player.getName());
        assertEquals(0, player.getPoints());

        player.addPoints(50);
        assertEquals(50, player.getPoints());
    }

    @Test
    public void testPlayerCompare() {
        com.buzzit.logic.Player[] players = new com.buzzit.logic.Player[3];
        players[0] = new com.buzzit.logic.Player("Player1");
        players[1] = new com.buzzit.logic.Player("Player2");
        players[2] = new com.buzzit.logic.Player("Player3");

        players[0].addPoints(10);
        players[2].addPoints(20);

        assertTrue(players[0].compareTo(players[1]) > 0);   // Player 0 has more points than 1
        assertTrue(players[0].compareTo(players[2]) < 0);   // Player 0 has less points than 2

        players[0].addPoints(10);
        assertTrue(players[0].compareTo(players[2]) == 0);  // Player 0 the same points as 2
    }

    @Test
    public void testPlayerSorting() {
        com.buzzit.logic.Player[] players = new com.buzzit.logic.Player[3];
        players[0] = new com.buzzit.logic.Player("Player1");
        players[1] = new com.buzzit.logic.Player("Player2");
        players[2] = new com.buzzit.logic.Player("Player3");

        players[0].addPoints(10);
        players[2].addPoints(20);

        com.buzzit.logic.Player[] clone = players.clone();

        Arrays.sort(clone, Collections.<com.buzzit.logic.Player>reverseOrder());

        assertEquals(clone[0], players[2]);
        assertEquals(clone[1], players[0]);
        assertEquals(clone[2], players[1]);
    }


    /***** Match.java *****/
    @Test public void testMatchProperties() {
        com.buzzit.logic.Player player = new com.buzzit.logic.Player("player");
        ArrayList<com.buzzit.logic.Category> categoriesChosen = new ArrayList<com.buzzit.logic.Category>();
        categoriesChosen.add(com.buzzit.logic.Category.MUSIC); categoriesChosen.add(com.buzzit.logic.Category.SPORTS);
        com.buzzit.logic.Match match = new com.buzzit.logic.Match(2, categoriesChosen, com.buzzit.logic.Difficulty.MEDIUM, player);
        com.buzzit.logic.Question question1 = match.getCurrentQuestion();

        assertEquals(2, match.getTotalQuestions());
        assertEquals(player, match.getPlayer());
        assertEquals(0, match.getQuestionIndex());
        assertTrue(match.isCorrect(question1.getCorrect()));
        assertFalse(match.isCorrect(question1.getWrong().get(0)));

        match.nextQuestion();
        assertEquals(1, match.getQuestionIndex());

        com.buzzit.logic.Question question2 = match.getCurrentQuestion();
        assertNotEquals(question1, question2);
    }


    @Test
    public void testMatchCategoriesChosen() {
        com.buzzit.logic.Player player = new com.buzzit.logic.Player("player");
        ArrayList<com.buzzit.logic.Category> categoriesChosen = new ArrayList<com.buzzit.logic.Category>();

        // One category
        categoriesChosen.add(com.buzzit.logic.Category.HISTORY);
        com.buzzit.logic.Match match = new com.buzzit.logic.Match(2, categoriesChosen, com.buzzit.logic.Difficulty.MEDIUM, player);
        assertTrue(questionInCategories(match, categoriesChosen));

        // Two categories
        categoriesChosen.add(com.buzzit.logic.Category.SPORTS);
        match = new com.buzzit.logic.Match(4, categoriesChosen, com.buzzit.logic.Difficulty.MEDIUM, player);
        assertTrue(questionInCategories(match, categoriesChosen));

        // Three categories
        categoriesChosen.add(com.buzzit.logic.Category.MUSIC);
        match = new com.buzzit.logic.Match(4, categoriesChosen, com.buzzit.logic.Difficulty.MEDIUM, player);
        assertTrue(questionInCategories(match, categoriesChosen));
    }

    @Test
    public void testDifferentQuestions() {
        com.buzzit.logic.Player player = new com.buzzit.logic.Player("player");
        ArrayList<com.buzzit.logic.Category> categoriesChosen = new ArrayList<com.buzzit.logic.Category>();
        categoriesChosen.add(com.buzzit.logic.Category.MUSIC); categoriesChosen.add(com.buzzit.logic.Category.SPORTS);
        com.buzzit.logic.Match match = new com.buzzit.logic.Match(4, categoriesChosen, com.buzzit.logic.Difficulty.MEDIUM, player);
        ArrayList<com.buzzit.logic.Question> questions = match.getQuestions();
        boolean different = false;

        outer_loop:
        for (int i = 0; i < questions.size(); i++) {
            for (int j = i + 1; j < questions.size(); j++) {
                if (questions.get(i) == questions.get(j)) {
                    different = true;
                    break outer_loop;
                }
            }
        }

        assertFalse(different);
    }


    /***** Play.java *****/
    @Test
    public void testScrambleBounds() {
        for (int i = 0; i < 100; i++) {
            ArrayList<Integer> shuffled = com.buzzit.logic.Play.scramble(i);
            assertTrue(inBounds(shuffled));
        }
    }

    @Test
    public void testScrambleRandomized() {
        boolean different = false;
        ArrayList<Integer> shuffled = com.buzzit.logic.Play.scramble(100);
        int testNumber = shuffled.get(0);

        for (int i = 0; i < 300; i++) {
            shuffled = com.buzzit.logic.Play.scramble(100);

            if (testNumber != shuffled.get(i)) {
                different = true;
                break;
            }
        }

        assertTrue(different);
    }




    private boolean inBounds(ArrayList<Integer> shuffled) {
        int lowerbound = 0;
        int upperbound = shuffled.size();

        for (Integer number : shuffled) {
            if (! (number >= lowerbound && number < upperbound))
                return false;
        }

        return true;
    }




    /* Other functions */
    private boolean questionInCategories(com.buzzit.logic.Match match, ArrayList<com.buzzit.logic.Category> categories) {
        ArrayList<com.buzzit.logic.Question> questions = match.getQuestions();

        for (com.buzzit.logic.Question q : questions) {
            int i = 0;
            for (i = 0; i < categories.size(); i++) {
                if (q.getCategory() == categories.get(i)) {
                    break;
                }
            }

            if (i == categories.size())
                return false;
        }

        return true;
    }

    private com.buzzit.logic.Question getDefaultQuestion() {
        ArrayList<String> wrong = new ArrayList<String>();
        wrong.add("This is wrong1");
        wrong.add("This is wrong2");
        wrong.add("This is wrong3");

        return new com.buzzit.logic.Question(defaultQuestion, wrong, defaultCorrect, defaultDifficulty, defaultCategory);
    }
}
