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
	//ArrayList<ArrayList<Question>> questionsByDifficulties;

	private static Play ourInstance = new Play();

	public static Play getInstance() {
		return ourInstance;
	}

	private Play(){
		questions = new ArrayList<Question>();
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

				Question q = new Question(question, wrong, correct, difficulty);
				questions.add(q);
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


	public ArrayList<Question> play(int numQuestions){
		ArrayList<Question> q = new ArrayList<Question>();
		ArrayList<Integer> indices = scramble(questions.size());
		Gdx.app.log("UEUEUEUEUEUEUEUE - QUESTIONS SIZE",  new Integer(questions.size()).toString());
		for(int i = 0; i < numQuestions; i++){
			q.add(questions.get(indices.get(i)));
		}
		for(int i=0; i< indices.size(); i++){
			Gdx.app.log("UEUEUEUEUEUEUEUE - INDICES", indices.get(i).toString());
		}
		//match = new Match(q, 1, 1);
		return q;
	}

}