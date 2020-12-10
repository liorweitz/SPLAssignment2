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
       subscribeEvent(BombDestroyerEvent.class,(bombDestroyerEvent)->{startBombing(bombDestroyerEvent);});
       subscribeBroadcast(Terminate.class,(terminate)->{diary.setLandoTerminate(System.currentTimeMillis());terminate();});
    }

    private void startBombing(BombDestroyerEvent bombDestroyerEvent) throws InterruptedException {
        Thread.sleep(duration);
        sendBroadcast(new FinishBombing());
        complete(bombDestroyerEvent,true);
    }
}
