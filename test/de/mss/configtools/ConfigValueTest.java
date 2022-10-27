package de.mss.configtools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ConfigValueTest {

   @Test
   public void test() {
      final ConfigValue t = new ConfigValue("test");

      assertEquals("test", t.getValue());
      assertEquals("test", t.getOrigValue());
      assertNull(t.getMergedValue());

      t.setMergedValue("merged");
      assertEquals("merged", t.getValue());
      assertEquals("test", t.getOrigValue());
      assertEquals("merged", t.getMergedValue());
   }


   @Test
   public void test2() {
      final ConfigValue t = new ConfigValue("test", "merged");
      assertEquals("merged", t.getValue());
      assertEquals("test", t.getOrigValue());
      assertEquals("merged", t.getMergedValue());

   }


   @Test
   public void testToString() {
      final ConfigValue t = new ConfigValue("test");
      assertEquals("Value {test}, OrigValue {test} ", t.toString());
      t.setMergedValue("merged");
      assertEquals("Value {merged}, OrigValue {test}, MergedValue {merged} ", t.toString());
   }
}
