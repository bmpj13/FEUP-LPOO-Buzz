package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;


public class Play {


	//static private ArrayList<ArrayList<Question>> questionsByCategory;
	static private ArrayList<Question>[][] questions;

	private final String easyPath = "data/easy.properties";
	private final String mediumPath = "data/medium.properties";
	private final String hardPath = "data/hard.properties";

	private Thread easyThread, mediumThread, hardThread;

	/**
	 * Constructor
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
		questions = new ArrayList[Difficulty.values().length][Category.values().length];
		for(Category cat:Category.values()){
			ArrayList<Question> q = new ArrayList<>();
			questions[Difficulty.EASY.getIndex()][cat.getIndex()] = q;

			q = new ArrayList<>();
			questions[Difficulty.MEDIUM.getIndex()][cat.getIndex()] = q;

			q = new ArrayList<>();
			questions[Difficulty.HARD.getIndex()][cat.getIndex()] = q;
		}

//		easyThread = new Thread(){
//			public void run(){
//				getFile(Difficulty.EASY, Play.questions[Difficulty.EASY.getIndex()], easyPath);
//			}
//		};
//		mediumThread = new Thread(){
//			public void run(){
//				getFile(Difficulty.MEDIUM, Play.questions[Difficulty.MEDIUM.getIndex()], mediumPath);
//			}
//		};
//		hardThread = new Thread(){
//			public void run(){
//				getFile(Difficulty.HARD, Play.questions[Difficulty.HARD.getIndex()], hardPath);
//			}
//		};
//		easyThread.start();
//		mediumThread.start();
//		hardThread.start();
//
//		try {
//				easyThread.join(500);
//				mediumThread.join(500);
//				hardThread.join(500);
//		} catch(InterruptedException ex){
//			ex.printStackTrace();
//			Gdx.app.log("EXCEPTION","Interrupted Exception");
//		}

		getFile(Difficulty.EASY, questions[Difficulty.EASY.getIndex()], easyPath);
		getFile(Difficulty.MEDIUM, questions[Difficulty.MEDIUM.getIndex()], mediumPath);
		getFile(Difficulty.HARD, questions[Difficulty.HARD.getIndex()], hardPath);
	}

	/**
	 * Gets questions from a .properties file
	 * @param difficulty Difficulty of questions from the file
	 * @param questionsByCategory BiDimensional Array to keep questions organized by category
	 * @param filepath Path of file
     */
	public void getFile(Difficulty difficulty, ArrayList<Question>[] questionsByCategory, String filepath){
		Properties prop = new Properties();
		InputStream input = null;

		try {
			FileHandle file = Gdx.files.internal(filepath);
			input = file.read();

			if (input == null) throw new IllegalArgumentException(filepath);

			//load a properties file from class path, inside static method
			prop.load(input);

			//get the property value and store it
			int t = Integer.parseInt(prop.getProperty("numquestions"));
			for(int i = 0; i < t; i++){
				String question = prop.getProperty("question"+i);
				int answers = Integer.parseInt(prop.getProperty("numanswers" + i));
				String category = prop.getProperty("category" + i);
				Category cat = Category.getCategory(category);
				int categoryIndex = cat.getIndex();

				String correct = new String();
				ArrayList<String> wrong = new ArrayList<String>();
				for(int z =0; z < answers; z++) {
					String key = prop.getProperty("answer" + i + "." + z);
					if (z == 0) {
						correct = key;
					} else {
						wrong.add(key);
					}
				}
				Question q = new Question(question, wrong, correct, difficulty, cat);
				questionsByCategory[categoryIndex].add(q);
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
	 * Randomizes order of numbers
	 * @param numIndices max number to randomize
	 * @return	ArrayList containing random order of numbers from 0 to numIndices
     */
	static public ArrayList<Integer> scramble(int numIndices){
		Random rand = new Random();
		ArrayList<Integer> indices = new ArrayList<Integer>();

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
     * @return
     */
	private ArrayList<Question> getQuestionsFromCategory(ArrayList<Category> categoriesChosen, Difficulty difficulty){

		ArrayList<Question> q = new ArrayList<Question>();
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

}