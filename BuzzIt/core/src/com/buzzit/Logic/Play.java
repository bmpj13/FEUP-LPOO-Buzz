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


	static private ArrayList<ArrayList<Question>> questionsByCategory;
	

	private static Play ourInstance = new Play();

	public static Play getInstance() {
		return ourInstance;
	}

	private Play() {

		questionsByCategory = new ArrayList<ArrayList<Question>>();
		for(Category cat:Category.values()){
			ArrayList<Question> questions = new ArrayList<Question>();
			questionsByCategory.add(questions);
		}

		getFile(Difficulty.MEDIUM);
	}


	public void getFile(Difficulty difficulty){
		Properties prop = new Properties();
		InputStream input = null;

		try {
			String filepath = "assets/data/a.properties";
			input = getClass().getClassLoader().getResourceAsStream(filepath);

			if (input == null) throw new IllegalArgumentException(filepath);

			//load a properties file from class path, inside static method
			prop.load(input);

			//get the property value and print it out
			int t = Integer.parseInt(prop.getProperty("numquestions"));
			for(int i = 0; i < t; i++){
				String question = prop.getProperty("question"+i);
				int answers = Integer.parseInt(prop.getProperty("numanswers" + i));
				String category = prop.getProperty("category" + i);
				Category cat = Category.getCategory(category);
				int arrayIndex = cat.getIndex();

				String correct = new String();
				ArrayList<String> wrong = new ArrayList<String>();
				for(int z =0; z < answers; z++){
					String key = prop.getProperty("answer"+i+"."+z);
					if(z == 0){
						correct = key;
					}
					else{
						wrong.add(key);
					}
				}

				Question q = new Question(question, wrong, correct, difficulty, cat);
				questionsByCategory.get(arrayIndex).add(q);
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

	private ArrayList<Question> getQuestionsFromCategory(ArrayList<Category> categoriesChosen){

		ArrayList<Question> q = new ArrayList<Question>();
		for(Category cat:categoriesChosen){
			int index = cat.getIndex();
			q.addAll(questionsByCategory.get(index));
		}
		return q;
	}

	public ArrayList<Question> play(int totalQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty){
		ArrayList<Question> q = new ArrayList<Question>();

		// gets all questions from selected categories
		ArrayList<Question> allFromCategories = getQuestionsFromCategory(categoriesChosen);

		// get randomized question indices from each category
		ArrayList<Integer> questionsScrambled = scramble(allFromCategories.size());

		// put random question from selected categories into arrayList
		for(int i=0; i< totalQuestions; i++) {
			q.add(allFromCategories.get(questionsScrambled.get(i)));
		}
		return q;
	}

}