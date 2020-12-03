package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
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
    protected void initialize() {

    }

    public void sendAttacks(){
        for(Attack attack:attacks){
            eventFutures.add(sendEvent(new AttackEvent(attack.getSerials(),attack.getDuration())));
        }
    }
}
