package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import jdk.nashorn.internal.codegen.CompilerConstants;

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
	
    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {

//        subscribeEvent(AttackEvent.class,lambda.call(AttackEvent a));
        subscribeEvent(AttackEvent.class,(attackEvent)->{act(attackEvent);}); //how the compiler know the argument is appropriate?
    }

    private void act(AttackEvent attackEvent){
        acquireEwoks(attackEvent);
        attack(attackEvent);
        BroadcastFinish(attackEvent);
    }

    private void acquireEwoks(AttackEvent attackEvent) {

    }

    private void attack(AttackEvent attackEvent) {

    }

    private void BroadcastFinish(AttackEvent attackEvent) {

    }
}
