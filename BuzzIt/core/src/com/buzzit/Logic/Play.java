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

	ArrayList<Question> questions;

	Match match;

	private boolean playing = false;

	public Play(){
		//players = new Player[1];
		questions = new ArrayList<Question>();
		getFile();
		//play();
	}


	public void getFile(){
		Gdx.app.log("ACTION","getting file");
		Properties prop = new Properties();
		InputStream input = null;


		try {
			String filename = "a.properties";
			input = getClass().getClassLoader().getResourceAsStream("assets/data/" + filename);
			if(input==null){
				Gdx.app.log("ERROR","Sorry, unable to find " + filename);
				return;
			} else
				Gdx.app.log("DIRECTORY", "right place");

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

				Question q = new Question(question, wrong, correct);
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

	public void play(){
		ArrayList<Question> q = new ArrayList<Question>();
		Random rand = new Random();
		int i ;//= rand.nextInt(questions.size());

		//System.out.println(questions.get(i).question);
		do{
			i = rand.nextInt(questions.size());
			q.add(questions.get(i));
		}while(1==0);
		//String[] options = questions.get(i).generateOptions(3);

		//match = new Match(q, 1);
	}

	public String[] getOptions(){

		return match.generateOptions();
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
}