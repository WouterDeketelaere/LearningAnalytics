package be.kul.mai.la.domain.analytics.config;

import org.junit.Test;

public class MacroMapTest {
    @Test
    public void activate() throws Exception {
        MacroMap macroMap = new MacroMap();
        macroMap.instrument("1").targets("Doorloop: Studieduur").mapper("1").filtercode("11,12,13").activate();
        System.out.println(macroMap.toPairList());
    }

}