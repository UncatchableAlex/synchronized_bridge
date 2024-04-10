/**
 * Runs all threads
 */

public class BridgeRunner {

	public static void main(String[] args) {

		// make sure that the user entered the correct number of args
		if (args.length != 2) {
			System.out.println("Usage: java BridgeRunner <bridge limit> <num cars>");
			System.exit(0);
		}
		int bridgeLimit = 0;
		int numCars = 0;
		// make sure that the user entered real numbers
		try {
			bridgeLimit = Integer.parseInt(args[0]);
			numCars = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Usage: javac BridgeRunner <bridge limit> <num cars>");
			System.exit(0);
		}
		// make sure that neither of the user's inputs were negative
		if (bridgeLimit <= 0 || numCars <= 0) {
			System.out.println("Error: bridge limit and/or num cars must be positive.");
		}


		// instantiate the bridge
		Bridge b = new OneLaneBridge(bridgeLimit);
		
		// allocate space for threads
		Thread[] threads = new Thread[numCars];

		// start then join the threads
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Car(i, b));
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				System.out.println("An interrupt occurred");
				System.exit(0);
			}
		}
		System.out.println("All cars have crossed!!");
	}

}