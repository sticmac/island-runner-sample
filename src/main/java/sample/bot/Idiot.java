package sample.bot;

import eu.ace_design.island.bot.IExplorerRaid;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Idiot implements IExplorerRaid {

    private static final Logger logger = LogManager.getLogger(Idiot.class);

    @Override
    public void initialize(String s) {
        logger.info("Initializing the explorer");
    }

    @Override
    public String takeDecision() {
        logger.info("Taking a decision");
        String decision = "{ \"action\": \"stop\" }";
        logger.trace(decision);
        return decision;
    }

    @Override
    public void acknowledgeResults(String s) {
        logger.info("Acknowledging the results of my latest decision");

    }

    @Override
    public String deliverFinalReport() {
        logger.info("Delivering the final report");
        return "This is the end.";
    }

}
