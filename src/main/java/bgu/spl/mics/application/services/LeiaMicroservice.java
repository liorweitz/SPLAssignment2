package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private int successfulAttacks;
	private ArrayList<Future> eventFutures;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		successfulAttacks=0;
		eventFutures=new ArrayList<>();
    }

    @Override
    protected synchronized void initialize() {
        subscribeBroadcast(FinishAttack.class,(finishAttack)->{FinishedAttackRoutine();});
        subscribeBroadcast(FinishDeactivation.class,(finishDeactivation)->{deactivationFinished();});
        subscribeBroadcast(FinishBombing.class,(finishBombing)->{diary.setLeiaTerminate(System.currentTimeMillis());termination();});
        try {
            wait(5000);
        }catch(InterruptedException ex){}
        sendInitialAttacks();
    }

    private void termination() {
        sendBroadcast(new Terminate());
        terminate();
    }

    private void deactivationFinished() {
        eventFutures.add(sendEvent(new BombDestroyerEvent()));
    }

    private void FinishedAttackRoutine() {
        successfulAttacks++;
        if (successfulAttacks==attacks.length){
            eventFutures.add(sendEvent(new DeactivationEvent()));
        }
    }

    public void sendInitialAttacks() {
        for(Attack attack:attacks){
            eventFutures.add(sendEvent(new AttackEvent(attack.getSerials(),attack.getDuration())));
        }
        sendBroadcast(new NoMoreAttacks());
    }
}
