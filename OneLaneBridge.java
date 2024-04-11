public class OneLaneBridge extends Bridge {
    private int bridgeLimit;
    private Object inTrafficTrueDir;
    private Object inTrafficFalseDir;

    /**
     * The constructor for a OneLaneBridge
     * @param bridgeLimit The maximum number of cars allowed on the bridge at a time
     */
    public OneLaneBridge(int bridgeLimit) {
        super();
        this.bridgeLimit = bridgeLimit;
        this.inTrafficTrueDir = new Object();
        this.inTrafficFalseDir = new Object();
        System.out.println(inTrafficTrueDir);
        System.out.println(inTrafficFalseDir);
        
    }
    @Override
    /**
     * Try to let another car onto the bridge
     * @param car This is the car that is arriving on the bridge
     */
    public void arrive(Car car) throws InterruptedException {
        // acquire the monitor for the correct direction:
        Object trafficQueue = car.getDirection() ? this.inTrafficTrueDir : this.inTrafficFalseDir;
        System.out.println("waiting for " + trafficQueue);
        synchronized (trafficQueue) {
            // if the bridge is empty, switch the direction to accomodate the arriving car:
            synchronized (this) {
                if (super.bridge.isEmpty()) {
                    super.direction = car.getDirection();
                }
            }
            // make the car wait in traffic until the car is going in the correct direction and the bridge is under capacity
            while ((car.getDirection() != super.direction) || (super.bridge.size() >= bridgeLimit)) {
                // if the bridge is empty, switch the direction to accomodate the arriving car:
                if (super.bridge.isEmpty()) {
                    super.direction = car.getDirection();
                }
                // make the car wait in the correct traffic queue
                System.out.println("making " + car + " wait in " + trafficQueue);
                trafficQueue.wait();
            }
            car.setEntryTime(super.currentTime);
            // add the car 
            super.bridge.add(car);
            // show the cars currently on the bridge to verify that we are still number the maximum cars on bridge total
            System.out.println(this.toString());
            // increment the time
            super.currentTime++;
        }
    }


    @Override
    /**
     * Let a car on the bridge exit. If the car isn't the next car on the bridge, it will wait until it is.
     * @param car The car exiting the bridge
     */
    public void exit(Car car) throws InterruptedException {
        Object trafficQueue = car.getDirection() ? this.inTrafficTrueDir : this.inTrafficFalseDir;
        synchronized(trafficQueue) {
            // wait until this car is at the front of the line and can exit:
            while (super.bridge.get(0).getID() != car.getID()) {
                trafficQueue.wait();
            }
        }
        synchronized(this) {
            // remove the car that wants to exit
            super.bridge.remove(0);
            System.out.println(this.toString());
            // if the bridge is now empty, reverse the direction and notify all for the new direction
            if (super.bridge.isEmpty()) {
                super.direction = !super.direction; 
            }
            Object waitingQueue = super.direction ? this.inTrafficTrueDir : this.inTrafficFalseDir;
            // notify the next car in the waiting queue:
            synchronized(waitingQueue) {
                System.out.println("notifying next car in " + waitingQueue);
                waitingQueue.notify();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Bridge (dir=%s): %s", super.direction, super.bridge); 
    }

}
