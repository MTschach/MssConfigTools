package de.mss.configtools;

import org.junit.Test;

import junit.framework.TestCase;

public class IniConfigFileTest extends TestCase {


   private void doTests(IniConfigFile cfg) {
      assertNotNull("ini is not null", cfg);


   }


   @Test
   public void test() {
      final IniConfigFile cfg = new IniConfigFile("test/iniTest1.ini");

      doTests(cfg);
   }

}
