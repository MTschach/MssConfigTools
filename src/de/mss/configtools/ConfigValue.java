package de.mss.configtools;


public class ConfigValue implements java.io.Serializable {

   private static final long serialVersionUID = 797782640647731916L;
   private String            origValue        = null;
   private String            mergedValue      = null;

   public ConfigValue(String o) {
      setOrigValue(o);
   }


   public ConfigValue(String o, String m) {
      setOrigValue(o);
      setMergedValue(m);
   }


   public String getMergedValue() {
      return this.mergedValue;
   }


   public String getOrigValue() {
      return this.origValue;
   }


   public String getValue() {
      return this.mergedValue != null ? this.mergedValue : this.origValue;
   }


   public void setMergedValue(String m) {
      this.mergedValue = m;
   }


   public void setOrigValue(String o) {
      this.origValue = o;
   }


   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder("Value {" + this.getValue() + "}");
      if (this.origValue != null) {
         sb.append(", OrigValue {" + this.origValue + "}");
      }
      if (this.mergedValue != null) {
         sb.append(", MergedValue {" + this.mergedValue + "}");
      }
      sb.append(" ");
      return sb.toString();
   }

}
