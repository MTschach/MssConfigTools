package de.mss.configtools;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class IniConfigFileTest {


   private void doTests(IniConfigFile cfg) {
      assertNotNull(cfg);
   }


   @Test
   public void test() {
      final IniConfigFile cfg = new IniConfigFile("test/iniTest1.ini");

      doTests(cfg);
   }

}
