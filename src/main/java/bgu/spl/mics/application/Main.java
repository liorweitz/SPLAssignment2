package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonInputReader;
import bgu.spl.mics.application.services.*;
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
		Ewoks.getInstance().setEwoksArray(input.getEwoks());

		LeiaMicroservice leia=new LeiaMicroservice(input.getAttacks());
		HanSoloMicroservice hansolo=new HanSoloMicroservice();
		C3POMicroservice c3po=new C3POMicroservice();
		R2D2Microservice r2d2=new R2D2Microservice(input.getR2D2());
		LandoMicroservice lando=new LandoMicroservice(input.getLando());

		Thread leiaThread=new Thread(leia);
		Thread hansoloThread=new Thread(hansolo);
		Thread c3poThread=new Thread(c3po);
		Thread r2d2Thread=new Thread(r2d2);
		Thread landoThread=new Thread(lando);


		hansoloThread.start();
		c3poThread.start();
		r2d2Thread.start();
		landoThread.start();
		leiaThread.start();





	}
}
