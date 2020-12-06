package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishAttack;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    Ewoks ewokSupplier;
	
    public C3POMicroservice() {
        super("C3PO");
        ewokSupplier=Ewoks.getInstance();
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class,(attackEvent)->{act(attackEvent);}); //how the compiler know the argument is appropriate?
        subscribeBroadcast(Terminate.class,(terminate)->terminate());
    }

    private void act(AttackEvent attackEvent) throws InterruptedException {
        acquireEwoks(attackEvent.getSerials());
        attack(attackEvent.getDuration());
        BroadcastFinish(attackEvent.getSerials());
    }

    private void acquireEwoks(List<Integer> serials) throws InterruptedException {
        if (serials!=null)
            Ewoks.getInstance().acquireEwoks(serials);
    }

    private void attack(int duration) {
        try {
            Thread.sleep(duration);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void BroadcastFinish(List<Integer> serials) {
        Ewoks.getInstance().release(serials);
        sendBroadcast(new FinishAttack());
        System.out.println(getName()+ " done the mission");
    }
}
