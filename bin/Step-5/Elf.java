import java.util.Random;
import java.util.concurrent.Semaphore;

public class Elf implements Runnable {

        enum ElfState {
                WORKING, TROUBLE, AT_SANTAS_DOOR
        };

        private ElfState state;
        private boolean running;
        /**
         * The number associated with the Elf
         */
        private int number;
        private Random rand = new Random();
        private SantaScenario scenario;

        // elves at santas door
        public static Semaphore santasDoor = new Semaphore(3, true);
        public static Semaphore atSantasDoor = new Semaphore(1);
        // queued for santas door
        private boolean queued = false;

        // door setter
        public void goToSantasDoor() {
	    if (Elf.atSantasDoor.availablePermits() != 0 && this.queued == false && this.getState() == ElfState.TROUBLE && Elf.santasDoor.tryAcquire()) {
	        this.queued = true;
	    }
	    else if (this.queued == true && Elf.santasDoor.availablePermits() == 0 && Elf.atSantasDoor.tryAcquire()){
		this.wakeSanta();
	    }
        }

        public Elf(int number, SantaScenario scenario) {
                this.number = number;
                this.scenario = scenario;
                this.state = ElfState.WORKING;
                this.running = true;
        }


        public ElfState getState() {
                return state;
        }

        /**
         * Santa might call this function to fix the trouble
         * @param state
         */
        public void setState(ElfState state) {
                this.state = state;
        }

        public void wakeSanta() {
	         boolean firstElf = true;
	         for (int i = 0; i != 10; i++) {
		         if (this.scenario.elves.get(i).queued) {
			         this.scenario.elves.get(i).setState(ElfState.AT_SANTAS_DOOR);
				 if (firstElf == true) {
				         this.scenario.santa.wakeUpByElves();
					 firstElf = false;
				 }
			 }
	         }
        }

        public void help() {
	        if (this.getState() == ElfState.AT_SANTAS_DOOR) {
		        Elf.santasDoor.release();
			this.queued = false;
		        this.setState(ElfState.WORKING);
	        }
	}

        public void terminate() {
                this.running = false;
        }

        @Override
        public void run() {
                while (running) {
      // wait a day
                try {
                        Thread.sleep(100);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                        switch (state) {
                        case WORKING: {
                                // at each day, there is a 1% chance that an elf runs into trouble
                                if (rand.nextDouble() < 0.01) {
                                        state = ElfState.TROUBLE;
                                }
                                break;
                        }
                        case TROUBLE:
			        this.goToSantasDoor();
                                break;
                        case AT_SANTAS_DOOR:
			        // wait for help because the first elf is waking santa up
			        break;
                        }
                }

                System.out.println("Elf " + number + " thread terminated");
        }

        /**
         * Report about my state
         */
        public void report() {
                System.out.println("Elf " + number + " : " + state);
        }

}
