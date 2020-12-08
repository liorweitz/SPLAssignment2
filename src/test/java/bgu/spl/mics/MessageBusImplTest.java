package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BroadcastForTest;
import bgu.spl.mics.application.services.ServiceForTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * unregister is not tested. also, awaitMessage method is tested implicitly, meaning it is
 * used almost in every test. so it is not tested explicitly.
 */
public class MessageBusImplTest {
    private ServiceForTest m1;
    private ServiceForTest m2;
    private ServiceForTest m3;

    MessageBusImpl MB;


    @BeforeEach
    public void setUp() {
        m1 = new ServiceForTest("test1");
        m2 = new ServiceForTest("test2");
        m3=new ServiceForTest("test3");
        MB=MessageBusImpl.getInstance();
    }

    @AfterEach
    public void TearDown(){
        MB.unregister(m1);
    }

    /**
     * Here we will register a ServiceForTest and also subscribe him to a broadcast.
     * then a message will be ascribed to his queue and we will check if the message he gets
     * and the original message are the same.
     * so, this Test check the validity of the methods register and subscribe event.
     * the validity of the returned future is checked in complete.
     * @throws InterruptedException
     */

    @Test
    public void registerTest() throws InterruptedException {
        AttackEvent e1=new AttackEvent();
        MB.register(m1);
        MB.subscribeEvent(e1.getClass(),m1);
        MB.sendEvent(e1);
        Event e2= (Event) MB.awaitMessage(m1);
        assertTrue(e1.equals(e2));

    }

    /**
     * m1 send broadcast. the Test checks if m2, m3 received it.
     * @throws InterruptedException
     */
    @Test
    public void subscribeBroadcastTest() throws InterruptedException {
        BroadcastForTest b1=new BroadcastForTest();
        MB.register(m2);
        MB.register(m3);
        MB.subscribeBroadcast(b1.getClass(),m2);
        MB.subscribeBroadcast(b1.getClass(),m3);
        MB.sendBroadcast(b1);
        BroadcastForTest b2= (BroadcastForTest) MB.awaitMessage(m2);
        BroadcastForTest b3= (BroadcastForTest) MB.awaitMessage(m3);
        assertTrue(b1.equals(b2));
        assertTrue(b1.equals(b3));
    }

    /**
     * Then register m1 and subscribe him to AttackEvent Events. then  AttackEvent Event is being subscribed
     * to the MessageBus and because m1 is the only Microservice it should get it to its queue. the test will check it.
     * @throws InterruptedException
     */
    @Test
    public void sendEventTest() throws InterruptedException {
        AttackEvent e1=new AttackEvent();
        MB.register(m1);
        MB.subscribeEvent(e1.getClass(), m1);
        MB.sendEvent(e1);
        AttackEvent e2= (AttackEvent) MB.awaitMessage(m1);
        assertTrue(e1.equals(e2));
    }

    /**
     * This Test register m1,m2 and subscribe them to a Broadcast kind of an broadcastForTest
     * then it sendBroadcast with the parameter of a broadcastForTest broadcast. it checks
     * if m1,m2 gets it.
     */
    @Test
    public void sendBroadcastTest() throws InterruptedException {
        BroadcastForTest b1=new BroadcastForTest();
        MB.register(m1);
        MB.register(m2);
        MB.subscribeBroadcast(b1.getClass(),m1);
        MB.subscribeBroadcast(b1.getClass(),m2);
        MB.sendBroadcast(b1);
        BroadcastForTest b2= (BroadcastForTest) MB.awaitMessage(m1);
        BroadcastForTest b3= (BroadcastForTest) MB.awaitMessage(m2);
        assertTrue(b1.equals(b2));
        assertTrue(b1.equals(b3));
    }

    /**
     * thie Test checks if a returned future is equal to the resolved one.
     */
    @Test
    public void completeTest(){
        MB.register(m1);
        MB.subscribeEvent(AttackEvent.class,m1);
        AttackEvent e1=new AttackEvent();
        AttackEvent e2=new AttackEvent();
        Future<Boolean> future1=MB.sendEvent(e1);
        Future<Boolean> future2=MB.sendEvent(e2);
        MB.complete(e1, true);
        MB.complete(e2, false);
        assertTrue(future1.get().equals(true));
        assertTrue(future2.get().equals(false));
    }
}
