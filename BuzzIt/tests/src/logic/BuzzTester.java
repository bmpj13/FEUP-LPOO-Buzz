package logic;

import com.buzzit.Logic.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;

@RunWith(GdxTestRunner.class)
public class BuzzTester {
    private final String defaultQuestion = "Question?";
    private final String defaultCorrect  = "This is correct";
    private final Difficulty defaultDifficulty = Difficulty.MEDIUM;
    private final Category defaultCategory = Category.HISTORY;


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
    }


    @Test
    public void testDifficultyProperties() {
        assertEquals(5, Difficulty.EASY.getPoints());
        assertEquals(10, Difficulty.MEDIUM.getPoints());
        assertEquals(20, Difficulty.HARD.getPoints());
    }


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
        String[] options = question.generateOptions(4);

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
        String[] options = question.generateOptions(4);
        String testString = options[0];
        boolean different = false;

        for (int i = 0; i < 300; i++) {
            options = question.generateOptions(4);

            if (testString != options[0]) {
                different = true;
                break;
            }
        }

        assertTrue(different);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOutOfBoundsGeneration() {
        Question question = getDefaultQuestion();
        question.generateOptions(5);
    }


    @Test
    public void testPlayer() {
        Player player = new Player("Gamer");

        assertEquals("Gamer", player.getName());
        assertEquals(0, player.getPoints());

        player.addPoints(50);
        assertEquals(50, player.getPoints());
    }


    @Test
    public void testMatchCategoriesChosen() {
        ArrayList<Category> categoriesChosen = new ArrayList<Category>();

        // One category
        categoriesChosen.add(Category.HISTORY);
        Match match = new Match(2, categoriesChosen, Difficulty.MEDIUM);
        assertTrue(questionInCategories(match, categoriesChosen));

        // Two categories
        categoriesChosen.add(Category.SPORTS);
        match = new Match(4, categoriesChosen, Difficulty.MEDIUM);
        assertTrue(questionInCategories(match, categoriesChosen));

        // Three categories
        categoriesChosen.add(Category.MUSIC);
        match = new Match(4, categoriesChosen, Difficulty.MEDIUM);
        assertTrue(questionInCategories(match, categoriesChosen));
    }


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
