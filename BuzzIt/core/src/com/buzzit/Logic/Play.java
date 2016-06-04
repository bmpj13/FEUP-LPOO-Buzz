package com.buzzit.Logic;

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


	static private ArrayList<ArrayList<Question>> questionsByCategory;

	private static Play ourInstance = new Play();

	public static Play getInstance() {
		return ourInstance;
	}

	private Play(){

		questionsByCategory = new ArrayList<>();
		for(Category cat:Category.values()){
			ArrayList<Question> questions = new ArrayList<>();
			questionsByCategory.add(questions);
		}
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
				Category cat = Category.getCategory(category);
				int arrayIndex = Category.getIndex(cat);

				String correct = new String();
				ArrayList<String> wrong = new ArrayList<>();
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

	private ArrayList<Question> getQuestionsFromCategory(ArrayList<Category> categoriesChosen){

		ArrayList<Question> q = new ArrayList<>();
		for(Category cat:categoriesChosen){
			int index = Category.getIndex(cat);
			q.addAll(questionsByCategory.get(index));
		}
		return q;
	}

	public ArrayList<Question> play(int totalQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty){
		ArrayList<Question> q = new ArrayList<>();

		//gets all questions from selected categories
		ArrayList<Question> allFromCategories = getQuestionsFromCategory(categoriesChosen);

		//get randomized question indices from each category
		ArrayList<Integer> questionsScrambled = scramble(allFromCategories.size());

		//put random question from selected categories into arrayList
		for(int i=0; i< totalQuestions; i++) {
			q.add(allFromCategories.get(questionsScrambled.get(i)));
		}
		return q;
	}

}