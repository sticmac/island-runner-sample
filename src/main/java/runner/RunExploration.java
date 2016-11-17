package runner;

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

        run(sample.bot.Idiot.class)             // <== Change me!
                .withName("My_Team_Name")
                .exploring(loadResource("map.json"))
                .withSeed(0xB03CA1A997813D02L)
                .startingAt(1,1,"EAST")
                .backBefore(20000)
                .withCrew(15)
                .collecting(1000, "WOOD"  )
                .collecting( 300, "QUARTZ")
                .collecting(  10, "FLOWER")
                .storingInto("./results")
                .fire()
        ;
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
