import java.util.concurrent.Semaphore;

public class Santa implements Runnable {

        enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER};
        private SantaState state;
        private boolean running;
        private SantaScenario scenario;
        public Semaphore reins;
        public Semaphore wakeUp;

        public Santa(SantaScenario scenario) {
	        this.wakeUp = new Semaphore(1);
	        this.reins = new Semaphore(9);
		try {
		        this.reins.acquire(9);
		} catch (InterruptedException e) {
		        e.printStackTrace();
		}
                this.state = SantaState.SLEEPING;
                this.running = true;
		this.scenario = scenario;
        }

        public void wakeUpByReindeer() {
	        try {
                        this.wakeUp.acquire();
	        } catch (InterruptedException e) {
		        e.printStackTrace();
	        }
	        this.state = SantaState.WOKEN_UP_BY_REINDEER;
        }

        public void wakeUpByElves() {
	        try {
                        this.wakeUp.acquire();
	        } catch (InterruptedException e) {
		        e.printStackTrace();
	        }
		System.out.println("here");
		if (Elf.atSantasDoor.availablePermits() == 0) {
	                this.state = SantaState.WOKEN_UP_BY_ELVES;
	        }
	}

        public void helpElves() {
               while (Elf.santasDoor.availablePermits() != 3) {
	               for(int i = 0; i != 10; i++) {
	                       this.scenario.elves.get(i).help();
	               }
	       }
	       this.state = SantaState.SLEEPING;
	       this.wakeUp.release();
	       Elf.atSantasDoor.release();
        }

        public void tieReindeer() {
	        this.reins.release(9);
	        this.state = SantaState.READY_FOR_CHRISTMAS;
        }

        public void terminate(){
                this.running = false;
        }
        
        @Override
        public void run() {
                while(running) {
                        // wait a day...
                        try {
                                Thread.sleep(100);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        switch(state) {
                        case SLEEPING: // if sleeping, continue to sleep
                                break;
                        case WOKEN_UP_BY_ELVES:
			        helpElves();
			        break;
                        case WOKEN_UP_BY_REINDEER:
			        tieReindeer();
                                break;
                        case READY_FOR_CHRISTMAS: // nothing more to be done
                                break;
                        }
                }
                System.out.println("Santa thread stopped running");
        }

        
        /**
         * Report about my state
         */
        public void report() {
                System.out.println("Santa : " + state);
        }
        
        
}
