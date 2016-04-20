package logic;

import java.applet.Applet;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by wnfuk_000 on 20/04/2016.
 */
public class Game {

	ArrayList<Question> questions;
	Player[] players;

	/*Game(){
        players = new Player[2];
    }*/

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
			
			int t = Integer.parseInt(prop.getProperty("teste"));
			System.out.println(t);
			//System.out.println(prop.getProperty("dbuser"));
			//System.out.println(prop.getProperty("dbpassword"));

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

	
	
	public static void main(String[] args) {
		
		Game g = new Game();
		g.getFile();
	}
}
