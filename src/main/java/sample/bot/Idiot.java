package sample.bot;

import eu.ace_design.island.bot.IExplorerRaid;


public class Idiot implements IExplorerRaid {

    @Override
    public void initialize(String s) { }

    @Override
    public String takeDecision() { return "{ \"action\": \"stop\" }"; }

    @Override
    public void acknowledgeResults(String s) { }

    @Override
    public String deliverFinalReport() { return "This is the end."; }

}
