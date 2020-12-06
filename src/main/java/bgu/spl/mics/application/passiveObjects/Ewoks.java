package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */

/**
 * the Ewoks objects will be stored in an arrayList. the index of the cell will match
 * the serial number of the Ewok. for that we will store a "degenerate" id=0 Ewok in index zero.
 */
public class Ewoks {
    private static Ewoks instance=null;
    private ArrayList<Ewok> ewoksArray;

    private Ewoks(){
        ewoksArray=new ArrayList<>();
    }

    public static Ewoks getInstance(){
        if (instance==null)
            instance=new Ewoks();
        return instance;
    }

    public void setEwoksArray(int numOfEwoks) {
        ewoksArray.add(new Ewok());
        for (int i=1;i<=numOfEwoks;i++)
            ewoksArray.add(new Ewok(i, true));
    }
    public synchronized void acquireEwoks(List<Integer> serials) throws InterruptedException {
        for (int serial:serials){
                while (!ewoksArray.get(serial).isAvailable())
                    wait();
            ewoksArray.get(serial).acquire();
        }
    }

    public synchronized void release(List<Integer> serials){
        for (int serial:serials){
            ewoksArray.get(serial).release();
        }
        notifyAll();
    }
}
