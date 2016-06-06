package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;


public class Play {
	static private int MAX_NUMBER_HIGHSCORE_ENTRIES = 10;
	static private ArrayList<Player> highScores;
	static private ArrayList<Question>[][] questions;

	final String easyPath = "data/easy.properties";
	final String mediumPath = "data/medium.properties";
	final String hardPath = "data/hard.properties";
	private final String highScoresPath = "data/highscores.properties";


	/**
	 * Initialize instance
	 */
	private static Play ourInstance = new Play();

	/**
	 *
	 * @return Play instance
	 */
	public static Play getInstance() {
		return ourInstance;
	}

	/**
	 * Initializes variables and gets information from files
	 */
	private Play() {
		highScores = new ArrayList<>();
		questions = new ArrayList[Difficulty.values().length][Category.values().length];
		for(Category category : Category.values()){
			ArrayList<Question> q = new ArrayList<>();
			questions[Difficulty.EASY.getIndex()][category.getIndex()] = q;

			q = new ArrayList<>();
			questions[Difficulty.MEDIUM.getIndex()][category.getIndex()] = q;

			q = new ArrayList<>();
			questions[Difficulty.HARD.getIndex()][category.getIndex()] = q;
		}


		readFile(Difficulty.EASY, questions[Difficulty.EASY.getIndex()], easyPath);
		readFile(Difficulty.MEDIUM, questions[Difficulty.MEDIUM.getIndex()], mediumPath);
		readFile(Difficulty.HARD, questions[Difficulty.HARD.getIndex()], hardPath);
		readHighScores();
	}

	/**
	 * Gets questions from 'filepath' file
	 * @param difficulty Difficulty of questions from the file
	 * @param questionsByCategory BiDimensional Array to keep questions organized by category
	 * @param filepath Path of file
	 */
	public void readFile(Difficulty difficulty, ArrayList<Question>[] questionsByCategory, String filepath) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			FileHandle file = Gdx.files.internal(filepath);
			input = file.read();

			if (input == null) throw new IllegalArgumentException(filepath);

			//load a properties file from class path, inside static method
			prop.load(input);

			//get the property value and store it
			int numQuestions = Integer.parseInt(prop.getProperty("numquestions"));
			for(int i = 0; i < numQuestions; i++){

				String question = prop.getProperty("question" + i);
				int answers = Integer.parseInt(prop.getProperty("numanswers" + i));
				String categoryName = prop.getProperty("category" + i);
				Category category = Category.getCategory(categoryName);
				int categoryIndex = category.getIndex();

				String correct = new String();
				ArrayList<String> wrong = new ArrayList<>();
				for(int j = 0; j < answers; j++) {
					String key = prop.getProperty("answer" + i + "." + j);
					if (j == 0) {
						correct = key;
					} else {
						wrong.add(key);
					}
				}

				Question q = new Question(question, wrong, correct, difficulty, category);
				questionsByCategory[categoryIndex].add(q);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * Reads highScores from file
	 */
	public void readHighScores() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			FileHandle file = Gdx.files.internal(highScoresPath);
			input = file.read();

			if (input == null) throw new IllegalArgumentException(highScoresPath);

			//load a properties file from class path, inside static method
			prop.load(input);

			//get the property value and store it
			int numberPlayers = Integer.parseInt(prop.getProperty("numPlayers"));
			for(int i = 0; i < numberPlayers; i++) {
				String name = prop.getProperty("name" + i);
				int points =  Integer.parseInt(prop.getProperty("points" + i));

				Player player = new Player(name);
				player.setPoints(points);
				highScores.add(player);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			Gdx.app.log("EXCEPTION","IO Exception");
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Saves highScores onto file
	 */
	public void saveHighScores(){
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			FileHandle file = Gdx.files.internal(highScoresPath);
			output = file.write(true);

			if (output == null) throw new IllegalArgumentException(highScoresPath);

			prop.setProperty("numPlayers", Integer.toString(highScores.size()));
			for(int i = 0; i < highScores.size(); i++){
				prop.setProperty("name" + i,highScores.get(i).getName());
				prop.setProperty("points" + i, Integer.toString(highScores.get(i).getPoints()));
			}

			prop.store(output, "HighScores for BuzzIt");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Compares the points of this match to the previous bests
	 * @param player Player to compare points
	 * @return Returns index of position if that score has fewer points; -1 if it's not better than any preiou score
	 */
	static public boolean isHighScore(Player player) {

		if (highScores.size() < MAX_NUMBER_HIGHSCORE_ENTRIES)
			return true;
		if (player.getPoints() > highScores.get(highScores.size() - 1).getPoints())
			return true;

		return false;
	}

	/**
	 * Updates ArrayList highScores if it is a new highscore
	 * @param player Player to add to ArrayList
	 * @return Returns true if it set a new highScore; false if not
	 */
	static public boolean addHighScore(Player player) {

		if (isHighScore(player)) {

			highScores.add(player);
			Collections.sort(highScores, Collections.reverseOrder());

			if (highScores.size() > MAX_NUMBER_HIGHSCORE_ENTRIES)
				highScores.remove(highScores.size() - 1);

			return true;
		}

		return false;
	}

	/**
	 * Randomizes order of numbers
	 * @param numIndices max number to randomize
	 * @return	ArrayList containing random order of numbers from 0 to numIndices
	 */
	static public ArrayList<Integer> scramble(int numIndices){
		Random rand = new Random();
		ArrayList<Integer> indices = new ArrayList<>();

		for (int t = 0; t < numIndices; t++) {
			indices.add(t);
		}

		int j, temp;
		for (int i = indices.size()-1; i > 0; i--){
			j = rand.nextInt(i+1);
			temp = indices.get(j);
			indices.set(j, indices.get(i));
			indices.set(i, temp);
		}

		return indices;
	}

	/**
	 * Gets all questions from the categories chosen and the difficulty
	 * @param categoriesChosen ArrayList containing categories the user chose
	 * @param difficulty Enum of chosen difficulty
	 * @return Return all questions from given categories
	 */
	private static ArrayList<Question> getQuestionsFromCategory(ArrayList<Category> categoriesChosen, Difficulty difficulty){

		ArrayList<Question> q = new ArrayList<>();
		for(Category cat:categoriesChosen){
			q.addAll(Play.questions[difficulty.getIndex()][cat.getIndex()]);
		}
		return q;
	}

	/**
	 * Gets random questions given the parameters
	 * @param totalQuestions Number of questions to answer
	 * @param categoriesChosen Categories of questions to be chosen
	 * @param difficulty Difficulty of questions
	 * @return ArrayList of Question
	 */
	public ArrayList<Question> play(int totalQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty){
		ArrayList<Question> q = new ArrayList<>();

		// gets all questions from selected categories
		ArrayList<Question> allFromCategories = getQuestionsFromCategory(categoriesChosen, difficulty);

		// get randomized question indices from each category
		ArrayList<Integer> questionsScrambled = scramble(allFromCategories.size());

		// put random question from selected categories into arrayList
		for(int i=0; i< totalQuestions; i++) {
			q.add(allFromCategories.get(questionsScrambled.get(i)));
		}
		return q;
	}


	public ArrayList<Player> getHighScores() {
		return highScores;
	}

	public static boolean playable(int numQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty) {
		return numQuestions <= getQuestionsFromCategory(categoriesChosen, difficulty).size();
	}
}