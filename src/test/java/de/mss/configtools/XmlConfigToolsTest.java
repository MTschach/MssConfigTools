package de.mss.configtools;


public class XmlConfigToolsTest {

   public static void main (String arg[]) {
      XmlConfigFile xmlconfig = new XmlConfigFile("test/xmlTest1.xml");
      
      String buffer= xmlconfig.writeConfig();
      System.out.println(buffer);
   }
}
