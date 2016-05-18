package com.buzzit.Logic;

import java.io.FileInputStream;
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

	public Play(){
		//players = new Player[1];
		questions = new ArrayList<Question>();
		getFile();
		//play();
	}


	public void getFile(){
		System.out.println("alo");
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "a.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			//input = new FileInputStream("C:/Users/wnfuk_000/git/LPOO-Buzz/BuzzIt/a.properties");
			if(input==null){
				System.out.println("Sorry, unable to find " + filename);
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

				Question q = new Question(question, wrong, correct);
				questions.add(q);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("ei");
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

	/*public static void main(String[] args) {

		Play g = new Play();

		g.getFile();
		g.play();
	}*/
}