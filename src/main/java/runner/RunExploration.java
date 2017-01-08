package runner;

import fr.unice.polytech.si3.qgl.iadb.Explorer;
import io.ReadTextFile;

import java.io.File;
import static eu.ace_design.island.runner.Runner.*;

public class RunExploration {

    /**
     * This Run is based on the 2016-2017 championship series, week #45.
     * Map:       https://github.com/mosser/QGL-16-17/tree/master/championships/week45
     * Island:    https://github.com/mosser/QGL-16-17/blob/master/arena/src/main/scala/library/Islands.scala (week45)
     * Contracts: https://github.com/mosser/QGL-16-17/blob/master/arena/src/main/scala/championships/Week45.scala
     */
    public static void main(String[] args) throws Exception {

        run(Explorer.class)             // <== Change me!
                .withName("ISLDB :: Nyan")
                .exploring(loadResource(args[0]))
                .withSeed(0xB03CA1A997813D02L)
                .startingAt(1,1,"EAST")
                .backBefore(20000)
                .withCrew(15)
                .collecting(1000, "WOOD"  )
		        .collecting(300, "FISH")
                .collecting(  10, "FLOWER")
                .storingInto("./results/"+args[0]+".d")
                .showReport()
                .fire()
        ;

	    ReadTextFile io = new ReadTextFile("results/"+args[0]+".d/Explorer_ISLDB :: Nyan.json");
	    if (io.getData().anyMatch(s -> s.contains("\"exception\":"))) {
			System.err.println(args[0]+": failed");
	    } else {
	    	System.err.println(args[0]+": succeeded");
	    }

    }


    /**
     * Load a local resource (in the src/main/resources directory) and consider it as a File
     * @return a File instance pointing to the embedded resource
     */
    private static File loadResource(String resourceName) {
        ClassLoader loader = RunExploration.class.getClassLoader();
        return new File(loader.getResource(resourceName).getFile());
    }


}
