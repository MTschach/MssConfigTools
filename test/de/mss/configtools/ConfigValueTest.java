package de.mss.configtools;

import org.junit.Test;

import junit.framework.TestCase;

public class ConfigValueTest extends TestCase {

   @Test
   public void test() {
      final ConfigValue t = new ConfigValue("test");

      assertEquals("Value", "test", t.getValue());
      assertEquals("OrigValue", "test", t.getOrigValue());
      assertNull("MergedValue", t.getMergedValue());

      t.setMergedValue("merged");
      assertEquals("Value", "merged", t.getValue());
      assertEquals("OrigValue", "test", t.getOrigValue());
      assertEquals("MergedValue", "merged", t.getMergedValue());
   }


   @Test
   public void test2() {
      final ConfigValue t = new ConfigValue("test", "merged");
      assertEquals("Value", "merged", t.getValue());
      assertEquals("OrigValue", "test", t.getOrigValue());
      assertEquals("MergedValue", "merged", t.getMergedValue());

   }


   @Test
   public void testToString() {
      final ConfigValue t = new ConfigValue("test");
      assertEquals("toString", "Value {test}, OrigValue {test} ", t.toString());
      t.setMergedValue("merged");
      assertEquals("toString", "Value {merged}, OrigValue {test}, MergedValue {merged} ", t.toString());
   }
}
