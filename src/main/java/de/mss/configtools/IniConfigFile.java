package de.mss.configtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class IniConfigFile extends ConfigFile {
   
   
   public IniConfigFile(String filename) {
      super(filename);
   }
   
   
   public IniConfigFile(Map<String,String> values) {
      super(values);
   }
   
   
   @Override
   public void loadConfig (String cfg, boolean append) {
      if (!append)
         clearConfig();
      
      try (BufferedReader br = new BufferedReader(new StringReader(cfg))) {
         String line;
         String path = "";
         
         do {
            line = br.readLine();
            if (line == null)
               break;
            
            line = line.trim();
            if (line.startsWith("[")) {
               path = extraxtPath(line);
            }
            else if (line.indexOf('=') >= 0) {
               extractKeyValue(line, path);
            }
         } while (true);
      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }


   private void extractKeyValue(String line, String path) {
      if (!isSet(line))
         return;
      
      int index = line.indexOf('=');
      
      if (index <= 0)
         return;
      
      String key = line.substring(0, index);
      String value = line.substring(index+1);
      
      insertKeyValue(key, value);
   }


   private String extraxtPath(String line) {
      line = line.substring(1, line.indexOf(']'));
      
      return line.replaceAll("/", configSeparator).replaceAll("\\", configSeparator);
   }
   
   
   @Override
   public String writeConfig() {
      StringBuilder sb = new StringBuilder();
      
      HashMap<String, HashMap<String, String>>values = new HashMap<>();
      
      for (Entry<String,String> entry : configValues.entrySet()) {
         String path = extractPath(entry.getKey());
         String valueKey = extractValueKey(entry.getKey());
         
         if (!values.containsKey(path)) {
            values.put(path, new HashMap<>());
         }
         values.get(path).put(valueKey, entry.getValue());
      }
      
      for (Entry<String,HashMap<String,String>> entry : values.entrySet()) {
         if (isSet(entry.getKey())) {
            sb.append("[");
            sb.append(entry.getKey());
            sb.append("]");
            sb.append(lineSeparator);
         }
         
         sb.append(writeValues(entry.getValue()));
      }
      
      
      return sb.toString();
   }


   private String writeValues(HashMap<String, String> values) {
      StringBuilder sb = new StringBuilder();
      
      for (Entry<String,String> entry : values.entrySet()) {
         sb.append(entry.getKey());
         sb.append("=");
         sb.append(entry.getValue());
         sb.append(lineSeparator);
      }
      sb.append(lineSeparator);
      
      return sb.toString();
   }


   private String extractValueKey(String key) {
      if (key.lastIndexOf(configSeparator) < 0)
         return key;
      
      return key.substring(key.lastIndexOf(configSeparator)+1);
   }


   private String extractPath(String key) {
      if (key.lastIndexOf(configSeparator) < 0)
         return "";
      
      return key.substring(0, key.lastIndexOf(configSeparator));
   }


}
