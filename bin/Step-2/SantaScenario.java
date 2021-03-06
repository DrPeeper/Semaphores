import java.util.ArrayList;
import java.util.List;



public class SantaScenario {

        public Santa santa;
        public List<Elf> elves;
        public boolean isDecember;
        
        public static void main(String args[]) {
                SantaScenario scenario = new SantaScenario();

                scenario.isDecember = false;
                // create the participants
                // Santa
                scenario.santa = new Santa(scenario);
                Thread th = new Thread(scenario.santa);
                th.start();
                // The elves: in this case: 10
                scenario.elves = new ArrayList<>();
                for(int i = 0; i != 10; i++) {
                        Elf elf = new Elf(i+1, scenario);
                        scenario.elves.add(elf);
                        th = new Thread(elf);
                        th.start();
                }
                // now, start the passing of time
                for(int day = 1; day < 500; day++) {
		        if (day <= 370) {
                        // wait a day
                                try {
                                        Thread.sleep(100);
                                } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                                // turn on December
                                if (day > (365 - 31)) {
                                        scenario.isDecember = true;
                                }
                                // print out the state:
                                System.out.println("***********  Day " + day + " *************************");
                                scenario.santa.report();
                                for(Elf elf: scenario.elves) {
                                elf.report();
                                }
			}
		        if (day == 370){
				for(int i=0; i != 10; i++) {
					scenario.elves.get(i).terminate();
				}
				scenario.santa.terminate();
			}
                }
        }
}
