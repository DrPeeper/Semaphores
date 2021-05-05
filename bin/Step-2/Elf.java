import java.util.Random;

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

        public void goToSanta() {
	        if (this.state == ElfState.TROUBLE) {
	                this.state = ElfState.AT_SANTAS_DOOR;
	        }
        }

        public void wakeSanta() {
	        scenario.santa.wakeUpByElves();
        }

        public void help() {
	        if (this.state == ElfState.AT_SANTAS_DOOR) {
	                this.state = ElfState.WORKING;
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
                                // at each day, there is a 1% chance that an elf runs into
                                // trouble.
                                if (rand.nextDouble() < 0.01) {
                                        state = ElfState.TROUBLE;
                                }
                                break;
                        }
                        case TROUBLE:
			    this.goToSanta();
                                break;
                        case AT_SANTAS_DOOR:
			    this.wakeSanta();
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
