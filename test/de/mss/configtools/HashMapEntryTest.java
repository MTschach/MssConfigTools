package de.mss.configtools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class HashMapEntryTest {

   @Test
   public void test() {
      final HashMapEntry t = new HashMapEntry();

      assertNull(t.getKey());
      assertNull(t.getValue());
      assertNull(t.getMapEntry());
      assertEquals("", t.toString());
   }


   @Test
   public void test2() {
      final HashMapEntry t = new HashMapEntry("key", "value");

      assertEquals("key", t.getKey());
      assertEquals("value", t.getValue());
      assertNull(t.getMapEntry());
      assertEquals("Key: key, Value: value", t.toString());
   }


   @Test
   public void test3() {
      final HashMapEntry t = new HashMapEntry("key", new HashMap<>());

      assertEquals("key", t.getKey());
      assertNull(t.getValue());
      assertNotNull(t.getMapEntry());
      assertEquals("Key: key, MapEntry: {}", t.toString());
   }


   @Test
   public void test4() {
      final HashMapEntry t = new HashMapEntry();
      t.setKey("key");
      t.setMapEntry(new HashMap<>());
      t.setValue("value");

      assertEquals("key", t.getKey());
      assertEquals("value", t.getValue());
      assertNotNull(t.getMapEntry());
      assertEquals("Key: key, MapEntry: {}, Value: value", t.toString());
   }


   @Test
   public void test5() {
      final HashMapEntry t = new HashMapEntry();
      t.setMapEntry(new HashMap<>());

      assertNull(t.getKey());
      assertNull(t.getValue());
      assertNotNull(t.getMapEntry());
      assertEquals("MapEntry: {}", t.toString());
   }


   @Test
   public void test6() {
      final HashMapEntry t = new HashMapEntry();
      t.setValue("value");

      assertNull(t.getKey());
      assertEquals("value", t.getValue());
      assertNull(t.getMapEntry());
      assertEquals("Value: value", t.toString());
   }
}
