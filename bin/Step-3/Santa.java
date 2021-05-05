//import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;


public class Santa implements Runnable {

        enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER};
        private SantaState state;
        private boolean running;
        private SantaScenario scenario;
        
        public Santa(SantaScenario scenario) {
                this.state = SantaState.SLEEPING;
                this.running = true;
		this.scenario = scenario;
        }

        public void wakeUpByElves() {
	        if (this.state == SantaState.SLEEPING) {
	                this.state = SantaState.WOKEN_UP_BY_ELVES;
	        }
	}

        public void helpElves() {
	        while (Elf.readyForHelp != 3) {
			for(int i = 0; i != 3; i++) {
				int elf = Elf.getSantasDoor(i);
				if (elf != -1 ) {
					 this.scenario.elves.get(elf - 1).help();
					 Elf.readyForHelp += 1;
				}
			}
		}
		Elf.clearSantasDoor();
		Elf.readyForHelp = 0;
		Elf.queueFull = false;
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
			        this.helpElves();
				this.state = SantaState.SLEEPING;
                                break;
                        case WOKEN_UP_BY_REINDEER: 
                                // FIXME: assemble the reindeer to the sleigh then change state to ready 
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
