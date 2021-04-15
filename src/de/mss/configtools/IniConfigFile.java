package de.mss.configtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class IniConfigFile extends ConfigFile {


   public IniConfigFile(Map<String, String> values) {
      super(values);
   }


   public IniConfigFile(String filename) {
      super(filename);
   }


   private void extractKeyValue(String line, String path, boolean append) {
      final int index = line.indexOf('=');

      if (index <= 0) {
         return;
      }

      final String key = line.substring(0, index);
      final String value = line.substring(index + 1);
      if (de.mss.utils.Tools.isSet(path)) {
         insertKeyValue(path + this.configSeparator + key, value, append);
      } else {
         insertKeyValue(key, value, append);
      }
   }


   private String extractPath(String key) {
      if (key.lastIndexOf(this.configSeparator) < 0) {
         return "";
      }

      return key.substring(0, key.lastIndexOf(this.configSeparator));
   }


   private String extractValueKey(String key) {
      if (key.lastIndexOf(this.configSeparator) < 0) {
         return key;
      }

      return key.substring(key.lastIndexOf(this.configSeparator) + 1);
   }


   private String extraxtPath(String line) {
      final String line1 = line.substring(1, line.indexOf(']'));

      return line1.replace("/", this.configSeparator).replace("\\", this.configSeparator);
   }


   @Override
   public void loadConfig(String cfg, boolean append) {
      if (!append) {
         clearConfig();
      }

      try (BufferedReader br = new BufferedReader(new StringReader(cfg))) {
         String line;
         String path = "";

         do {
            line = br.readLine();
            if (line == null) {
               break;
            }

            line = line.trim();
            if (line.startsWith("[")) {
               path = extraxtPath(line);
            } else if (line.indexOf('=') >= 0) {
               extractKeyValue(line, path, append);
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

      final HashMap<String, HashMap<String, String>> values = new HashMap<>();

      for (final Entry<String, ConfigValue> entry : this.configValues.entrySet()) {
         final String path = extractPath(entry.getKey());
         final String valueKey = extractValueKey(entry.getKey());

         if (!values.containsKey(path)) {
            values.put(path, new HashMap<>());
         }
         values.get(path).put(valueKey, entry.getValue().getOrigValue());
      }

      for (final Entry<String, HashMap<String, String>> entry : values.entrySet()) {
         if (isSet(entry.getKey())) {
            sb.append("[");
            sb.append(entry.getKey());
            sb.append("]");
            sb.append(this.lineSeparator);
         }

         sb.append(writeValues(entry.getValue()));
      }


      return sb.toString();
   }


   private String writeValues(HashMap<String, String> values) {
      final StringBuilder sb = new StringBuilder();

      for (final Entry<String, String> entry : values.entrySet()) {
         sb.append(entry.getKey());
         sb.append("=");
         sb.append(entry.getValue());
         sb.append(this.lineSeparator);
      }
      sb.append(this.lineSeparator);

      return sb.toString();
   }


}
