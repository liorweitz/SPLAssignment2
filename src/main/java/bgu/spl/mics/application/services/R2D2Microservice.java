package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private final long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class,(deactivationEvent)->{deactivation();});
        subscribeBroadcast(Terminate.class,(terminate)->terminate());
    }

    private void deactivation() throws InterruptedException {
        Thread.sleep(duration);
        System.out.println("R2D2 Deactivating...");
        sendBroadcast(new FinishDeactivation());
    }
}
