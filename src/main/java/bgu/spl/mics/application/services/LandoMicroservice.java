package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.FinishBombing;
import bgu.spl.mics.application.messages.Terminate;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private final long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
       subscribeEvent(BombDestroyerEvent.class,(bombDestroyerEvent)->{startBombing();});
       subscribeBroadcast(Terminate.class,(terminate)->terminate());
    }

    private void startBombing() throws InterruptedException {
        Thread.sleep(duration);
        System.out.println("lando bombs");
        sendBroadcast(new FinishBombing());
    }
}
