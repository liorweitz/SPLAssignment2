package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishAttack;
import bgu.spl.mics.application.messages.NoMoreAttacks;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.passiveObjects.Diary;
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
    private long finishAttacks;
	
    public C3POMicroservice() {
        super("C3PO");
        ewokSupplier=Ewoks.getInstance();
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class,(attackEvent)->{act(attackEvent);});
        subscribeBroadcast(NoMoreAttacks.class, (noMoreAttacks)->diary.setC3POFinish(finishAttacks));
        subscribeBroadcast(Terminate.class,(terminate)->{diary.setC3POTerminate(System.currentTimeMillis());terminate();});
    }

    private void act(AttackEvent attackEvent) throws InterruptedException {
        acquireEwoks(attackEvent.getSerials());
        attack(attackEvent.getDuration());
        BroadcastFinish(attackEvent);
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

    private void BroadcastFinish(AttackEvent attackEvent) {
        Ewoks.getInstance().release(attackEvent.getSerials());
        sendBroadcast(new FinishAttack());
        int prevAttacks;
        diary.increaseTotalAttacks();
        finishAttacks=System.currentTimeMillis();
        complete(attackEvent,true);
    }
}
