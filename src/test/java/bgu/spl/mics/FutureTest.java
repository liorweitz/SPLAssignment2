package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void testGet(){
        future.resolve("hello");
        assertSame("hello",future.get());
    }


    @Test
    public void testResolve(){
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }

    @Test
    public void testIsDone(){
        assertFalse(future.isDone());
        future.resolve("");
        assertTrue(future.isDone());

    }
    @Test
    public void testGetWithTimeOut() throws InterruptedException{
        future.get(100,TimeUnit.MILLISECONDS);
        assertFalse(future.isDone());
        future.resolve("foo");
        assertEquals(future.get(100,TimeUnit.MILLISECONDS),"foo");
    }
}