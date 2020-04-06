package de.mss.configtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.mss.utils.Tools;

public abstract class ConfigFile {

   protected Map<String, String> configValues    = new HashMap<>();

   protected static final int    MAX_DEPTH       = 20;

   protected String              configSeparator = ".";
   protected String              lineSeparator   = System.getProperty("line.separator");

   protected Logger              logger          = null;


   public ConfigFile(String filename) {
      this.logger = LogManager.getLogger("config");
      try {
         loadConfig(new File(filename));
      }
      catch (IOException e) {
         this.logger.error(Tools.getId(new Throwable()), e);
      }
   }


   public ConfigFile(String filename, Logger l) {
      this.logger = l;
      try {
         loadConfig(new File(filename));
      }
      catch (IOException e) {
         this.logger.error(Tools.getId(new Throwable()), e);
      }
   }


   public ConfigFile(Map<String, String> values) {
      this.configValues = values;
   }


   public void clearConfig() {
      this.configValues.clear();
   }


   public boolean contains(String key) {
      return this.configValues.containsKey(key);
   }


   public String getConfigSeparator() {
      return this.configSeparator;
   }


   public void removeKey(String key) {
      Set<String> keys = getKeys();

      for (String k : keys) {
         if (k.startsWith(key))
            this.configValues.remove(k);
      }
   }


   public Set<String> getKeys() {
      return this.configValues.keySet();
   }


   public BigDecimal getValue(String key, BigDecimal defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      try {
         return new BigDecimal(this.configValues.get(key));
      }
      catch (NumberFormatException nfe) {
         return defaultValue;
      }
   }


   public BigInteger getValue(String key, BigInteger defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      try {
         return new BigInteger(this.configValues.get(key));
      }
      catch (NumberFormatException nfe) {
         return defaultValue;
      }
   }


   public Boolean getValue(String key, Boolean defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      String value = this.getValue(key, defaultValue.toString());

      if ("J".equalsIgnoreCase(value)
            || "JA".equalsIgnoreCase(value)
            || "TRUE".equals(value)
            || "1".equals(value))
         return Boolean.TRUE;

      return Boolean.FALSE;
   }


   public Double getValue(String key, Double defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      try {
         return Double.parseDouble(this.configValues.get(key));
      }
      catch (NumberFormatException nfe) {
         return defaultValue;
      }
   }


   public Float getValue(String key, Float defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      try {
         return Float.parseFloat(this.configValues.get(key));
      }
      catch (NumberFormatException nfe) {
         return defaultValue;
      }
   }


   public Integer getValue(String key, Integer defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      try {
         return Integer.parseInt(this.configValues.get(key));
      }
      catch (NumberFormatException nfe) {
         return defaultValue;
      }
   }


   public String getValue(String key, String defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      return this.configValues.get(key);
   }


   public void insertKeyValue(String key, String value) {
      if (this.configValues.containsKey(key))
         this.configValues.remove(key);

      this.configValues.put(key, isSet(value) ? value : "");
   }


   protected boolean isSet(String s) {
      return (s != null && s.trim().length() > 0);
   }


   public void loadConfig(File file) throws IOException {
      loadConfig(file, false);
   }


   public void loadConfig(File file, boolean append) throws IOException {
      if (!file.exists()
            || !file.isFile())
         return;
      loadConfig(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath()))), append);
   }


   public void loadConfig(String cfg) {
      loadConfig(cfg, false);
   }


   public abstract void loadConfig(String cfg, boolean append);


   public void setConfigSeparator(String c) {
      this.configSeparator = c;
   }


   public void writeConfig(File file) throws IOException {
      writeConfig(file.getName());
   }


   public void writeConfig(String filename) throws IOException {
      try (FileOutputStream fos = new FileOutputStream(filename)) {
         fos.write(writeConfig().getBytes());
         fos.flush();
      }
   }


   public abstract String writeConfig();
   
   
   protected Map<String, HashMapEntry> toHierarchicalStructure() {
      Map<String,HashMapEntry> mapEntries = new TreeMap<>();

      for (Entry<String, String> entry : configValues.entrySet()) {
         if (!entry.getKey().contains(configSeparator))
            mapEntries.put(entry.getKey(), new HashMapEntry(entry.getKey(), entry.getValue()));
         else {
            String newKey = entry.getKey().substring(0, entry.getKey().indexOf(configSeparator));
            String subKey = entry.getKey().substring(entry.getKey().indexOf(configSeparator) + 1);

            mapEntries.put(newKey, addEntry(mapEntries.get(newKey), newKey, subKey, entry.getValue()));
         }
      }

      return mapEntries;
   }
   
   
   private HashMapEntry addEntry(HashMapEntry hashMapEntry, String mapKey, String key, String value) {
      if (hashMapEntry == null)
         hashMapEntry = new HashMapEntry(mapKey, (String)null);
      
      if (hashMapEntry.getMapEntry() == null)
         hashMapEntry.setMapEntry(new HashMap<>());
      
      if (!key.contains(configSeparator))
         hashMapEntry.getMapEntry().put(key, new HashMapEntry(key, value));
      else {
         String newKey = key.substring(0, key.indexOf(configSeparator));
         String subKey = key.substring(key.indexOf(configSeparator) + 1);
         
         hashMapEntry.getMapEntry().put(newKey, addEntry(hashMapEntry.getMapEntry().get(newKey), newKey, subKey, value));
      }
      return hashMapEntry;
   }
}
