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
import java.util.Set;

public abstract class ConfigFile {

   protected Map<String, String> configValues    = new HashMap<>();

   protected static final int    MAX_DEPTH       = 20;

   protected String              configSeparator = ".";
   protected String              lineSeparator   = System.getProperty("line.separator");


   public ConfigFile(String filename) {
      try {
         loadConfig(new File(filename));
      }
      catch (IOException e) {
         // TO DO Auto-generated catch block
         e.printStackTrace();
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
      return configSeparator;
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
         return new Double(this.configValues.get(key));
      }
      catch (NumberFormatException nfe) {
         return defaultValue;
      }
   }


   public Float getValue(String key, Float defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      try {
         return new Float(this.configValues.get(key));
      }
      catch (NumberFormatException nfe) {
         return defaultValue;
      }
   }


   public Integer getValue(String key, Integer defaultValue) {
      if (!this.contains(key))
         return defaultValue;

      try {
         return new Integer(this.configValues.get(key));
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


   protected void insertKeyValue(String key, String value) {
      if (!isSet(key))
         return;

      if (configValues.containsKey(key))
         configValues.remove(key);

      configValues.put(key, isSet(value) ? value : "");
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
      configSeparator = c;
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
}
