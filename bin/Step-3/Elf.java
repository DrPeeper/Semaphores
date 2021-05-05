import java.util.Random;
import java.util.ArrayList;

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

        // elves queued at santas door
        private static ArrayList<Integer> santasDoor = new ArrayList<Integer>(0);
        public static int readyForHelp = 0;
        public static boolean queueFull = false;

        public static int getSantasDoor(int target) {
	        int elf = -1;
	        try {
	                elf = Elf.santasDoor.get(target);
	        } catch (IndexOutOfBoundsException e) {
	                return -1;
	        }

	        return elf;
        }

        public static void clearSantasDoor() {
	        Elf.santasDoor.clear();
        }

        // door setter
        public void goToSantasDoor() {
	        if (this.getState() == ElfState.TROUBLE && Elf.queueFull == false) {
		        if (this.number == Elf.getSantasDoor(0) || this.number == Elf.getSantasDoor(1)) {
  			         return;
 		        }
		        Elf.santasDoor.add(this.number);

			if (this.number == Elf.getSantasDoor(2)) {
			         Elf.queueFull = true;
			}
	        }
		else if (Elf.queueFull == true) {
		         if (
			          this.number == Elf.getSantasDoor(0) ||
			          this.number == Elf.getSantasDoor(1) ||
			          this.number == Elf.getSantasDoor(2)
			 ) {
			          this.setState(ElfState.AT_SANTAS_DOOR);
		         }
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
	        scenario.santa.wakeUpByElves();
        }

        public void help() {
	        if (this.getState() == ElfState.AT_SANTAS_DOOR) {
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
                                // at each day, there is a 1% chance that an elf runs into
                                // trouble.
                                if (rand.nextDouble() < .01) {
                                        state = ElfState.TROUBLE;
                                }
                                break;
                        }
                        case TROUBLE:
			        this.goToSantasDoor();
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
