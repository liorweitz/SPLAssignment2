package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonInputReader;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException {
		Input input= JsonInputReader.getInputFromJson("C:\\Users\\liorw\\IdeaProjects\\SPL211\\src\\main\\java\\bgu\\spl\\mics\\application\\input.json");
		System.out.println(input.getEwoks());



	}
}
