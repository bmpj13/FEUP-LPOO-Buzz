package com.buzzit.logic;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;


/**
 * Created by wnfuk_000 on 20/04/2016.
 */
public class Play {

	static private ArrayList<Question> questions;
	static private ArrayList<ArrayList<Question>> questionsByCategory;

	private static Play ourInstance = new Play();

	public static Play getInstance() {
		return ourInstance;
	}

	private Play(){
		//questions = new ArrayList<Question>();
		questionsByCategory = new ArrayList< ArrayList<Question>>();
		getFile(Difficulty.MEDIUM);
	}


	public void getFile(Difficulty difficulty){
		Gdx.app.log("ACTION","getting file");
		Properties prop = new Properties();
		InputStream input = null;

		try {
			String filename = "a.properties";
			input = getClass().getClassLoader().getResourceAsStream("assets/data/" + filename);
			if(input==null){
				Gdx.app.log("ERROR","Sorry, unable to find " + filename);
				return;
			}
			//load a properties file from class path, inside static method
			prop.load(input);

			//get the property value and print it out
			int t = Integer.parseInt(prop.getProperty("numquestions"));
			for(int i = 0; i < t; i++){
				String question = prop.getProperty("question"+i);
				int answers = Integer.parseInt(prop.getProperty("numanswers" + i));
				String category = prop.getProperty("category" + i);
				int arrayIndex = Category.getIndex(category);

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

				Question q = new Question(question, wrong, correct, difficulty, category);
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
		for(int t = 0; t < numIndices; t++){
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

	private ArrayList<ArrayList<Integer>> getRandomsByCategory(){

		ArrayList<ArrayList<Integer>> q = new ArrayList<>();
		for(int i = 0; i < questionsByCategory.size(); i++){
			ArrayList<Integer> indices = scramble(questionsByCategory.get(i).size());
			q.add(indices);
		}
		return q;
	}

	public ArrayList<Question> play(int totalQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty){
		ArrayList<Question> q = new ArrayList<>();
		Random rand = new Random();
		//get randomized question indices from each category
		ArrayList<ArrayList<Integer>> questionsScrambled = getRandomsByCategory();
		//for keeping track of indices already used from each category
		ArrayList<Integer> categoryIndex = new ArrayList<>();

		for(int i=0; i< totalQuestions; i++) {

			//choose a category at random from those chosen
			int arrayIndex = rand.nextInt(categoriesChosen.size());

			//get indice from a random question from the category above
			String categoryRand = categoriesChosen.get(arrayIndex).getCategory();
			int categoryindice = Category.getIndex(categoryRand);
			int indiceRand = questionsScrambled.get(categoryindice).get(categoryIndex.get(categoryindice));

			//add to ArrayList
			q.add(questionsByCategory.get(categoryindice).get(indiceRand));

			//keep track of questions already used
			categoryIndex.set(categoryindice, categoryIndex.get(categoryindice)+1);

		}
		return q;
	}

}