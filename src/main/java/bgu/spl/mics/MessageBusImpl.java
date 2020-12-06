package bgu.spl.mics;

import bgu.spl.mics.application.services.ServiceForTest;

import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

/**
 * maps:
 * 1) map between microservice and its queue.
 * 2) map between event type to the Microservices that subscribed to its type.
 * 3) map between Broadcast to the Microsevices that subscribed to its type.
 * 4) map between Event and Future object
 * 5) map between event Type and counter that measure how many times the sendEvent method
 * was called for specific type of Event. it is used for the round robin scheduler.
 *
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance=null;

	//Map that stores the events queues of the Microservices, by Microservice
	private Map<MicroService,Queue<Message>> microToQMap;
	//Map that connects between an event and an array that holds the microservices that can handle it.
	private Map<Class<? extends Event>,ArrayList<MicroService>> eventToMicroMap;
	private Map<Class<? extends Broadcast>,ArrayList<MicroService>> broadcastToMicroMap;
	private Map<Event,Future> eventToFutureMap;
	private Map<Class<? extends Event>,Integer> roundRobinMap;

	private MessageBusImpl(){
		microToQMap=new HashMap<>();
		eventToMicroMap=new HashMap<>();
		broadcastToMicroMap=new HashMap<>();
		eventToFutureMap=new HashMap<>();
		roundRobinMap=new HashMap<>();
	}

	public static MessageBusImpl getInstance(){
		if (instance==null)
			instance=new MessageBusImpl();
		return instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (eventToMicroMap) {
			if (!eventToMicroMap.containsKey(type)) {
				eventToMicroMap.put(type, new ArrayList<>());
			}
			(eventToMicroMap.get(type)).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadcastToMicroMap) {
			if (!broadcastToMicroMap.containsKey(type)) {
				broadcastToMicroMap.put(type, new ArrayList<MicroService>());
			}
			(broadcastToMicroMap.get(type)).add(m);
		}
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		synchronized (eventToFutureMap) {
			eventToFutureMap.get(e).resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (broadcastToMicroMap) {
			synchronized (microToQMap) {
				if (broadcastToMicroMap.containsKey(b.getClass())) {
					for (MicroService m : broadcastToMicroMap.get(b.getClass())) {
						microToQMap.get(m).add(b);
					}
				}
				this.microToQMap.notifyAll();
			}
		}
	}

	@Override
	public <T> Future<T>  sendEvent(Event<T> e) {
		synchronized (eventToFutureMap) {
			synchronized (eventToMicroMap) {
				synchronized (microToQMap) {
					if (eventToMicroMap.containsKey(e.getClass())) {
						Future<T> future = new Future<>();
						eventToFutureMap.put(e, future);
						Integer robinCounter = roundRobinMap.get(e.getClass());
						if (robinCounter == null) {
							roundRobinMap.put(e.getClass(), 1);
							robinCounter = 1;
						}
						ArrayList<MicroService> curr = eventToMicroMap.get(e.getClass());
						MicroService willHandle = curr.get(robinCounter % curr.size());
						microToQMap.get(willHandle).add(e);
						robinCounter++;
						roundRobinMap.put(e.getClass(), robinCounter);
						this.microToQMap.notifyAll();
						return future;
					} else {
						return null;
					}
				}
			}
		}
	}

	/**
	 * not taking care of double registration for the same microservice.
	 * if re-registration happens a new queue will be allocated.
	 */
	@Override
	public void register(MicroService m) {
		synchronized (microToQMap) {
			microToQMap.put(m, new LinkedList<Message>());
		}
	}

	@Override
	public void unregister(MicroService m) {
		synchronized (microToQMap) {
			//deletion for tests.
			if (m.getClass()== ServiceForTest.class){
				instance=null;
			}
			else if (microToQMap.containsKey(m)) {
				Queue<Message> toBeDeleted = microToQMap.get(m);
				toBeDeleted.clear();
				microToQMap.remove(m);
			}
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		synchronized (microToQMap) {
			if (!microToQMap.containsKey(m)) {
				throw new IllegalStateException("The Microservice:"+ m.getName()+" is not registered.");
			} else {
				try {
					while (microToQMap.get(m).size() == 0)
						this.microToQMap.wait();
				} catch (InterruptedException ex) {
					throw new InterruptedException();
				}
			}
//			Message e2=microToQMap.get(m).poll();
			return microToQMap.get(m).poll();
		}
	}
}
