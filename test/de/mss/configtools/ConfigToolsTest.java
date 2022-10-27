package de.mss.configtools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ConfigToolsTest {

   private static final String XML_FILE_NAME   = "test/xmlTest1.xml";
   private static final String INI_FILE_NAME   = "test/iniTest1.ini";
   private static final String PROPS_FILE_NAME = "test/propsTest1.props";


   private void doConfigTests(ConfigFile cfg) {
      assertEquals("Michael", cfg.getValue("data.contact.name", ""));
      assertEquals("0123456789", cfg.getValue("data.contact.tel.number", ""));
      assertEquals("private", cfg.getValue("data.contact.tel.type", ""));
      assertEquals("0987654321", cfg.getValue("data.contact.mobile.number", ""));
      assertEquals("business", cfg.getValue("data.contact.mobile.type", ""));
      assertEquals("Michaels address", cfg.getValue("data.address.name", ""));
      assertEquals("JUnit-Weg", cfg.getValue("data.address.street", ""));
      assertEquals("1", cfg.getValue("data.address.number", ""));
      assertEquals("01234", cfg.getValue("data.address.postCode", ""));
      assertEquals("Schmölln", cfg.getValue("data.address.city", ""));

      cfg.insertKeyValue("data.contact.email.type", "private", true);
      cfg.insertKeyValue("data.contact.email.number", "e.mail@gmail.com", true);
      cfg.insertKeyValue("data.contact.tel.number", "2345678901", true);

      assertEquals("Michael", cfg.getValue("data.contact.name", ""));
      assertEquals("2345678901", cfg.getValue("data.contact.tel.number", ""));
      assertEquals("private", cfg.getValue("data.contact.tel.type", ""));
      assertEquals("0987654321", cfg.getValue("data.contact.mobile.number", ""));
      assertEquals("business", cfg.getValue("data.contact.mobile.type", ""));
      assertEquals("e.mail@gmail.com", cfg.getValue("data.contact.email.number", ""));
      assertEquals("private", cfg.getValue("data.contact.email.type", ""));
      assertEquals("Michaels address", cfg.getValue("data.address.name", ""));
      assertEquals("JUnit-Weg", cfg.getValue("data.address.street", ""));
      assertEquals("1", cfg.getValue("data.address.number", ""));
      assertEquals("01234", cfg.getValue("data.address.postCode", ""));
      assertEquals("Schmölln", cfg.getValue("data.address.city", ""));
   }


   @Test
   public void testIniConfig() {
      final IniConfigFile cfg = new IniConfigFile(INI_FILE_NAME);

      doConfigTests(cfg);

      //@formatter:off
      assertEquals("name=JUnit\n\n" +

                                                 "[data.contact.mobile]\n" +
                                                 "number=0987654321\n" +
                                                 "type=business\n\n" +

                                                 "[data.contact]\n" +
                                                 "name=Michael\n\n" +

                                                 "[data.contact.tel]\n" +
                                                 "number=0123456789\n" +
                                                 "type=private\n" +
                                                 "email=\n\n" +

                                                 "[data.contact.email]\n" +
                                                 "number=null\n" +
                                                 "type=null\n\n" +

                                                 "[data.address]\n"+
                                                 "number=1\n" +
                                                 "city=Schmölln\n" +
                                                 "street=JUnit-Weg\n" +
                                                 "name=Michaels address\n" +
                                                 "postCode=01234\n\n" +

                                                 "",
                                                 cfg.writeConfig());
      //@formatter:on
   }


   @Test
   public void testPropertiesConfig() {
      final PropertiesConfigFile cfg = new PropertiesConfigFile(PROPS_FILE_NAME);

      doConfigTests(cfg);

      //@formatter:off
      assertEquals(cfg.writeConfig(), "data.contact.mobile.type=business\n" +
            "data.contact.tel.number=2345678901\n" +
            "data.address.name=Michaels address\n" +
            "data.contact.mobile.number=0987654321\n" +
            "data.address.street=JUnit-Weg\n" +
            "data.address.postCode=01234\n" +
            "data.contact.email.type=private\n" +
            "data.contact.tel.type=private\n" +
            "data.contact.name=Michael\n" +
            "data.contact.email.number=e.mail@gmail.com\n" +
            "data.address.number=1\n" +
            "data.address.city=Schmölln\n" +
            ""
            );
      //@formatter:on
   }


   @Test
   public void testXmlConfig() {
      final XmlConfigFile cfg = new XmlConfigFile(XML_FILE_NAME);

      doConfigTests(cfg);

      //@formatter:off
      assertEquals(cfg.writeConfig(), "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
            "<data>\n" +
            "   <address>\n" +
            "      <number>1</number>\n" +
            "      <city>Schmölln</city>\n" +
            "      <street>JUnit-Weg</street>\n" +
            "      <name>Michaels address</name>\n" +
            "      <postCode>01234</postCode>\n" +
            "   </address>\n" +
            "   <contact>\n" +
            "      <mobile>\n" +
            "         <number>0987654321</number>\n" +
            "         <type>business</type>\n" +
            "      </mobile>\n" +
            "      <name>Michael</name>\n" +
            "      <tel>\n" +
            "         <number>2345678901</number>\n" +
            "         <type>private</type>\n" +
            "      </tel>\n" +
            "      <email>\n" +
            "         <number>e.mail@gmail.com</number>\n" +
            "         <type>private</type>\n" +
            "      </email>\n" +
            "   </contact>\n" +
            "</data>\n");
      //@formatter:on
   }
}
