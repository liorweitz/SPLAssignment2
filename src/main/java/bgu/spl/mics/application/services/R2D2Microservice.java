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
        subscribeEvent(DeactivationEvent.class,(deactivationEvent)->{deactivation(deactivationEvent);});
        subscribeBroadcast(Terminate.class,(terminate)->termination());
    }

    private void termination(){
        diary.setR2D2Terminate(System.currentTimeMillis());
        terminate();
    }

    private void deactivation(DeactivationEvent deactivationEvent) throws InterruptedException {
        Thread.sleep(duration);
        diary.setR2D2Deactivate(System.currentTimeMillis());
        sendBroadcast(new FinishDeactivation());
        complete(deactivationEvent,true);
    }
}
