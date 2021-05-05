import java.util.Random;
import java.util.concurrent.Semaphore;


public class Reindeer implements Runnable {

        public enum ReindeerState {AT_BEACH, AT_WARMING_SHED, AT_THE_SLEIGH};
        private ReindeerState state;
        private SantaScenario scenario;
        private Random rand = new Random();
        private boolean running;
        public static Semaphore waitingOnSanta = new Semaphore(8, true);

        /**
         * The number associated with the reindeer
         */
        private int number;
        
        public Reindeer(int number, SantaScenario scenario) {
                this.number = number;
                this.scenario = scenario;
                this.state = ReindeerState.AT_BEACH;
                this.running = true;
        }

        public void terminate() {
                this.running = false;
        }

        private void goToShed() {
		if (Reindeer.waitingOnSanta.tryAcquire()) {
		        this.state = ReindeerState.AT_WARMING_SHED;
			try {
				this.scenario.santa.reins.acquire();
			} catch (InterruptedException e) {
			        e.printStackTrace();
			}
	        }
		else {
		        this.scenario.santa.wakeUpByReindeer();
			try {
				this.scenario.santa.reins.acquire();
			} catch (InterruptedException e) {
			        e.printStackTrace();
			}
			this.state = ReindeerState.AT_THE_SLEIGH;
		}
	}

        @Override
        public void run() {
                while(running) {
                        // wait a day
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// see what we need to do:
			switch(state) {
			        case AT_BEACH: { // if it is December, the reindeer might think about returning from the beach
					if (scenario.isDecember) {
						if (rand.nextDouble() < 0.1) {
							this.goToShed();
						}
					}
					break;                  
				}
				case AT_WARMING_SHED: {
					this.state = ReindeerState.AT_THE_SLEIGH;
					break;
				}
				case AT_THE_SLEIGH: {
					// keep pulling
					break;
				}
			}
                }

                System.out.println("Reindeer " + number + " thread terminated");
        }
        
        /**
         * Report about my state
         */
        public void report() {
                System.out.println("Reindeer " + number + " : " + state);
        }
        
}
