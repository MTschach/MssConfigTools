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

   protected static final int         MAX_DEPTH       = 20;

   protected Map<String, ConfigValue> configValues    = new HashMap<>();

   protected String                   configSeparator = ".";
   protected String                   lineSeparator   = System.getProperty("line.separator");

   protected Logger                   logger          = null;


   public ConfigFile(Map<String, String> values) {
      this.configValues = new HashMap<>();
      for (final Entry<String, String> entry : values.entrySet()) {
         this.configValues.put(entry.getKey(), new ConfigValue(entry.getValue()));
      }
   }


   public ConfigFile(String filename) {
      this.logger = LogManager.getLogger("config");
      try {
         loadConfig(new File(filename));
      }
      catch (final IOException e) {
         this.logger.error(Tools.getId(new Throwable()), e);
      }
   }


   public ConfigFile(String filename, Logger l) {
      this.logger = l;
      try {
         loadConfig(new File(filename));
      }
      catch (final IOException e) {
         this.logger.error(Tools.getId(new Throwable()), e);
      }
   }


   private HashMapEntry addEntry(HashMapEntry hashMapEntry, String mapKey, String key, String value) {
      HashMapEntry ret = hashMapEntry;
      if (ret == null) {
         ret = new HashMapEntry(mapKey, (String)null);
      }

      if (ret.getMapEntry() == null) {
         ret.setMapEntry(new HashMap<>());
      }

      if (!key.contains(this.configSeparator)) {
         ret.getMapEntry().put(key, new HashMapEntry(key, value));
      } else {
         final String newKey = key.substring(0, key.indexOf(this.configSeparator));
         final String subKey = key.substring(key.indexOf(this.configSeparator) + 1);

         ret.getMapEntry().put(newKey, addEntry(ret.getMapEntry().get(newKey), newKey, subKey, value));
      }
      return ret;
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


   public Set<String> getKeys() {
      return this.configValues.keySet();
   }


   public BigDecimal getValue(String key, BigDecimal defaultValue) {
      if (!this.contains(key) || this.configValues.get(key) == null) {
         return defaultValue;
      }

      try {
         return new BigDecimal(this.configValues.get(key).getValue());
      }
      catch (final NumberFormatException nfe) {
         Tools.doNullLog(nfe);
         return defaultValue;
      }
   }


   public BigInteger getValue(String key, BigInteger defaultValue) {
      if (!this.contains(key) || this.configValues.get(key) == null) {
         return defaultValue;
      }

      try {
         return new BigInteger(this.configValues.get(key).getValue());
      }
      catch (final NumberFormatException nfe) {
         Tools.doNullLog(nfe);
         return defaultValue;
      }
   }


   public Boolean getValue(String key, Boolean defaultValue) {
      if (!this.contains(key) || this.configValues.get(key) == null) {
         return defaultValue;
      }

      final String value = this.getValue(key, defaultValue.toString());

      if ("J".equalsIgnoreCase(value)
            || "JA".equalsIgnoreCase(value)
            || "TRUE".equals(value)
            || "1".equals(value)) {
         return Boolean.TRUE;
      }

      return Boolean.FALSE;
   }


   public Double getValue(String key, Double defaultValue) {
      if (!this.contains(key) || this.configValues.get(key) == null) {
         return defaultValue;
      }

      try {
         return Double.parseDouble(this.configValues.get(key).getValue());
      }
      catch (final NumberFormatException nfe) {
         Tools.doNullLog(nfe);
         return defaultValue;
      }
   }


   public Float getValue(String key, Float defaultValue) {
      if (!this.contains(key) || this.configValues.get(key) == null) {
         return defaultValue;
      }

      try {
         return Float.parseFloat(this.configValues.get(key).getValue());
      }
      catch (final NumberFormatException nfe) {
         Tools.doNullLog(nfe);
         return defaultValue;
      }
   }


   public Integer getValue(String key, Integer defaultValue) {
      if (!this.contains(key) || this.configValues.get(key) == null) {
         return defaultValue;
      }

      try {
         return Integer.parseInt(this.configValues.get(key).getValue());
      }
      catch (final NumberFormatException nfe) {
         Tools.doNullLog(nfe);
         return defaultValue;
      }
   }


   public String getValue(String key, String defaultValue) {
      if (!this.contains(key) || this.configValues.get(key) == null) {
         return defaultValue;
      }

      return this.configValues.get(key).getValue();
   }


   protected void insertKeyValue(String key, String value) {
      insertKeyValue(key, value, true);
   }


   protected void insertKeyValue(String key, String value, boolean append) {
      if (!append) {
         this.configValues.put(key, new ConfigValue(isSet(value) ? value : ""));
         return;
      }

      if (!this.configValues.containsKey(key)) {
         this.configValues.put(key, new ConfigValue(null, Tools.isSet(value) ? value : ""));
         return;
      }

      this.configValues.get(key).setMergedValue(value);
   }


   protected boolean isSet(String s) {
      return s != null && s.trim().length() > 0;
   }


   public void loadConfig(File file) throws IOException {
      loadConfig(file, false);
   }


   public void loadConfig(File file, boolean append) throws IOException {
      if (!file.exists()
            || !file.isFile()) {
         return;
      }
      loadConfig(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath()))), append);
   }


   public void loadConfig(String cfg) {
      loadConfig(cfg, false);
   }


   public abstract void loadConfig(String cfg, boolean append);


   public void mergeKeyValue(String key, String value) {
      if (this.configValues.containsKey(key)) {
         this.configValues.get(key).setMergedValue(value);
      } else {
         this.configValues.put(key, new ConfigValue(null, isSet(value) ? value : ""));
      }
   }


   public void removeKey(String key) {
      final Set<String> keys = getKeys();

      for (final String k : keys) {
         if (k.startsWith(key)) {
            this.configValues.remove(k);
         }
      }
   }


   public void setConfigSeparator(String c) {
      this.configSeparator = c;
   }


   protected Map<String, HashMapEntry> toHierarchicalStructure() {
      final Map<String, HashMapEntry> mapEntries = new TreeMap<>();

      for (final Entry<String, ConfigValue> entry : this.configValues.entrySet()) {
         if (!entry.getKey().contains(this.configSeparator)) {
            mapEntries.put(entry.getKey(), new HashMapEntry(entry.getKey(), entry.getValue().getValue()));
         } else {
            final String newKey = entry.getKey().substring(0, entry.getKey().indexOf(this.configSeparator));
            final String subKey = entry.getKey().substring(entry.getKey().indexOf(this.configSeparator) + 1);

            mapEntries.put(newKey, addEntry(mapEntries.get(newKey), newKey, subKey, entry.getValue().getValue()));
         }
      }

      return mapEntries;
   }


   public void unmergeKey(String key) {
      if (this.configValues.containsKey(key)) {
         if (this.configValues.get(key).getOrigValue() == null) {
            this.configValues.remove(key);
         } else {
            this.configValues.get(key).setMergedValue(null);
         }
      }
   }


   public abstract String writeConfig();


   public void writeConfig(File file) throws IOException {
      writeConfig(file.getName());
   }


   public void writeConfig(String filename) throws IOException {
      try (FileOutputStream fos = new FileOutputStream(filename)) {
         fos.write(writeConfig().getBytes());
         fos.flush();
      }
   }
}
