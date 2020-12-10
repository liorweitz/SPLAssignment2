package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
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
		Input input= JsonInputReader.getInputFromJson("C:\\Users\\liorw\\IdeaProjects\\SPL211\\input.json");
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

		try {
			hansoloThread.join();
			c3poThread.join();
			r2d2Thread.join();
			landoThread.join();
			leiaThread.join();
		} catch (InterruptedException e) {}

		GsonBuilder builder=new GsonBuilder();
		Gson gson=builder.create();
		FileWriter writer=new FileWriter("C:\\Users\\liorw\\IdeaProjects\\SPL211\\output.json");
		writer.write(gson.toJson(Diary.getInstance()));
		writer.close();


	}
}
