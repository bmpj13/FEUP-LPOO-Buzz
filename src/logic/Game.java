package logic;

import java.applet.Applet;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Created by wnfuk_000 on 20/04/2016.
 */
public class Game {

	ArrayList<Question> questions;
	Player[] players;

	Game(){
		players = new Player[2];
		questions = new ArrayList<Question>();
	}


	public void getFile(){
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "a.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
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
		Random rand = new Random();
		int i = rand.nextInt(questions.size());

		System.out.println(questions.get(i).question);

		String[] options = questions.get(i).generateOptions(3);

		for (String s : options) {
			System.out.println(s);
		}
	}



	public static void main(String[] args) {

		Game g = new Game();

		g.getFile();
		g.play();
	}
}
