package de.mss.configtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapEntry implements java.io.Serializable {
   
   private static final long serialVersionUID = -2355994955435185782L;
   private Map<String, HashMapEntry> mapEntry = null;
   private String value = null;
   private String key = null;
   
   
   public HashMapEntry() {}
   
   public HashMapEntry(String newKey, Map<String, HashMapEntry> newEntry) {
      setKey(newKey);
      setMapEntry(newEntry);
   }
   
   
   public HashMapEntry(String newKey, String newValue) {
      setKey(newKey);
      setValue(newValue);
   }
   
   
   public void setKey(String newKey) {
      this.key = newKey;
   }
   
   
   public void setMapEntry(Map<String, HashMapEntry> newEntry) {
      this.mapEntry = newEntry;
   }
   
   
   public void setValue(String newValue) {
      this.value = newValue;
   }
   
   
   public String getKey() {
      return this.key;
   }
   
   
   public Map<String, HashMapEntry> getMapEntry() {
      return this.mapEntry;
   }
   
   
   public String getValue() {
      return this.value;
   }
   
   
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      if (this.key != null)
         sb.append("Key: " + this.key);
      
      if (this.mapEntry != null)
         sb.append(sb.length() > 0 ? ", MapEntry: " : "MapEntry: " + this.mapEntry.toString());
      
      if (this.value != null)
         sb.append(sb.length() > 0 ? ", Value: " : "Value: " + this.value);
      
      return sb.toString();
   }
}
