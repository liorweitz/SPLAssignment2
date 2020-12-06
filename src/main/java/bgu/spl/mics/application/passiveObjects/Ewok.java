package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
    private int serialNumber;
    private boolean available;

    //empty constructor for Test.
    public Ewok(){
        serialNumber=0;
        available=true;
    }

    public Ewok(int serialNumber, boolean available){
        this.serialNumber=serialNumber;
        this.available=available;
    }

    /**
     * Acquires an Ewok
     */
    public void acquire() {
        if(available == true)
            available = false;
    }

    /**
     * release an Ewok
     */
    public void release() {
        if(available == false)
            available = true;
    }

    public boolean isAvailable(){
        return this.available;
    }
}