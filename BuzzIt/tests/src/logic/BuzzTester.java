package logic;

import com.buzzit.Logic.*;

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
    private final Difficulty defaultDifficulty = Difficulty.MEDIUM;
    private final Category defaultCategory = Category.HISTORY;

    /***** Category.java *****/
    @Test
    public void testCategoryProperties() {
        assertEquals(0, Category.SPORTS.getIndex());
        assertEquals("SPORTS", Category.SPORTS.getName());

        assertEquals(1, Category.HISTORY.getIndex());
        assertEquals("HISTORY", Category.HISTORY.getName());

        assertEquals(2, Category.SCIENCE.getIndex());
        assertEquals("SCIENCE", Category.SCIENCE.getName());

        assertEquals(3, Category.MUSIC.getIndex());
        assertEquals("MUSIC", Category.MUSIC.getName());


        assertEquals(Category.SPORTS, Category.getCategory(Category.SPORTS.getName()));
        assertEquals(Category.HISTORY, Category.getCategory(Category.HISTORY.getName()));
        assertEquals(Category.SCIENCE, Category.getCategory(Category.SCIENCE.getName()));
        assertEquals(Category.MUSIC, Category.getCategory(Category.MUSIC.getName()));
        assertEquals(null, Category.getCategory("Not a category name"));
    }


    /***** Difficulty.java *****/
    @Test
    public void testDifficultyProperties() {
        assertEquals(5, Difficulty.EASY.getPoints());
        assertEquals(10, Difficulty.MEDIUM.getPoints());
        assertEquals(20, Difficulty.HARD.getPoints());
    }


    @Test public void testConvert() {
        assertEquals(Difficulty.EASY, Difficulty.convert(Difficulty.EASY.toString()));
        assertEquals(Difficulty.MEDIUM, Difficulty.convert(Difficulty.MEDIUM.toString()));
        assertEquals(Difficulty.HARD, Difficulty.convert(Difficulty.HARD.toString()));
        assertEquals(null, Difficulty.convert("not convertible"));
    }


    /***** Question.java *****/
    @Test
    public void testQuestionProperties() {
        Question question = getDefaultQuestion();

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
        Question question = getDefaultQuestion();
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
        Question question = getDefaultQuestion();
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
        Player player = new Player("Gamer");

        assertEquals("Gamer", player.getName());
        assertEquals(0, player.getPoints());

        player.addPoints(50);
        assertEquals(50, player.getPoints());
    }

    @Test
    public void testPlayerCompare() {
        Player[] players = new Player[3];
        players[0] = new Player("Player1");
        players[1] = new Player("Player2");
        players[2] = new Player("Player3");

        players[0].addPoints(10);
        players[2].addPoints(20);

        assertTrue(players[0].compareTo(players[1]) > 0);   // Player 0 has more points than 1
        assertTrue(players[0].compareTo(players[2]) < 0);   // Player 0 has less points than 2

        players[0].addPoints(10);
        assertTrue(players[0].compareTo(players[2]) == 0);  // Player 0 the same points as 2
    }

    @Test
    public void testPlayerSorting() {
        Player[] players = new Player[3];
        players[0] = new Player("Player1");
        players[1] = new Player("Player2");
        players[2] = new Player("Player3");

        players[0].addPoints(10);
        players[2].addPoints(20);

        Player[] clone = players.clone();

        Arrays.sort(clone, Collections.<Player>reverseOrder());

        assertEquals(clone[0], players[2]);
        assertEquals(clone[1], players[0]);
        assertEquals(clone[2], players[1]);
    }


    /***** Match.java *****/
    @Test public void testMatchProperties() {
        Player player = new Player("player");
        ArrayList<Category> categoriesChosen = new ArrayList<Category>();
        categoriesChosen.add(Category.MUSIC); categoriesChosen.add(Category.SPORTS);
        Match match = new Match(2, categoriesChosen, Difficulty.MEDIUM, player);
        Question question1 = match.getCurrentQuestion();

        assertEquals(2, match.getTotalQuestions());
        assertEquals(player, match.getPlayer());
        assertEquals(0, match.getQuestionIndex());
        assertTrue(match.isCorrect(question1.getCorrect()));
        assertFalse(match.isCorrect(question1.getWrong().get(0)));

        match.nextQuestion();
        assertEquals(1, match.getQuestionIndex());

        Question question2 = match.getCurrentQuestion();
        assertNotEquals(question1, question2);
    }


    @Test
    public void testMatchCategoriesChosen() {
        Player player = new Player("player");
        ArrayList<Category> categoriesChosen = new ArrayList<Category>();

        // One category
        categoriesChosen.add(Category.HISTORY);
        Match match = new Match(2, categoriesChosen, Difficulty.MEDIUM, player);
        assertTrue(questionInCategories(match, categoriesChosen));

        // Two categories
        categoriesChosen.add(Category.SPORTS);
        match = new Match(4, categoriesChosen, Difficulty.MEDIUM, player);
        assertTrue(questionInCategories(match, categoriesChosen));

        // Three categories
        categoriesChosen.add(Category.MUSIC);
        match = new Match(4, categoriesChosen, Difficulty.MEDIUM, player);
        assertTrue(questionInCategories(match, categoriesChosen));
    }

    @Test
    public void testDifferentQuestions() {
        Player player = new Player("player");
        ArrayList<Category> categoriesChosen = new ArrayList<Category>();
        categoriesChosen.add(Category.MUSIC); categoriesChosen.add(Category.SPORTS);
        Match match = new Match(4, categoriesChosen, Difficulty.MEDIUM, player);
        ArrayList<Question> questions = match.getQuestions();
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
            ArrayList<Integer> shuffled = Play.scramble(i);
            assertTrue(inBounds(shuffled));
        }
    }

    @Test
    public void testScrambleRandomized() {
        boolean different = false;
        ArrayList<Integer> shuffled = Play.scramble(100);
        int testNumber = shuffled.get(0);

        for (int i = 0; i < 300; i++) {
            shuffled = Play.scramble(100);

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
    private boolean questionInCategories(Match match, ArrayList<Category> categories) {
        ArrayList<Question> questions = match.getQuestions();

        for (Question q : questions) {
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

    private Question getDefaultQuestion() {
        ArrayList<String> wrong = new ArrayList<String>();
        wrong.add("This is wrong1");
        wrong.add("This is wrong2");
        wrong.add("This is wrong3");

        return new Question(defaultQuestion, wrong, defaultCorrect, defaultDifficulty, defaultCategory);
    }
}
