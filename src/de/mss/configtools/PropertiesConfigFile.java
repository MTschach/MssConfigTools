package de.mss.configtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class PropertiesConfigFile extends ConfigFile {


   public PropertiesConfigFile(Map<String, String> values) {
      super(values);
   }


   public PropertiesConfigFile(String filename) {
      super(filename);
   }


   @Override
   public void loadConfig(String cfg, boolean append) {
      if (!append) {
         clearConfig();
      }

      try (BufferedReader br = new BufferedReader(new StringReader(cfg))) {
         String line;

         do {
            line = br.readLine();
            if (line == null) {
               break;
            }

            line = line.trim();
            if (line.indexOf('=') >= 0) {
               final int index = line.indexOf('=');
               final String key = line.substring(0, index);
               final String value = line.substring(index + 1);
               insertKeyValue(key, value, append);
            }
         }
         while (true);
      }
      catch (final IOException e) {
         e.printStackTrace();
      }
   }


   @Override
   public String writeConfig() {
      final StringBuilder sb = new StringBuilder();

      for (final String key : getKeys()) {
         sb.append(key + "=" + getValue(key, "") + System.lineSeparator());
      }

      return sb.toString();
   }
}
