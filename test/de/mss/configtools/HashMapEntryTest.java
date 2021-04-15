package de.mss.configtools;

import java.util.HashMap;

import org.junit.Test;

import junit.framework.TestCase;

public class HashMapEntryTest extends TestCase {

   @Test
   public void test() {
      final HashMapEntry t = new HashMapEntry();

      assertNull("key", t.getKey());
      assertNull("value", t.getValue());
      assertNull("mapEntry", t.getMapEntry());
      assertEquals("toString", "", t.toString());
   }


   @Test
   public void test2() {
      final HashMapEntry t = new HashMapEntry("key", "value");

      assertEquals("key", "key", t.getKey());
      assertEquals("value", "value", t.getValue());
      assertNull("mapEntry", t.getMapEntry());
      assertEquals("toString", "Key: key, Value: value", t.toString());
   }


   @Test
   public void test3() {
      final HashMapEntry t = new HashMapEntry("key", new HashMap<>());

      assertEquals("key", "key", t.getKey());
      assertNull("value", t.getValue());
      assertNotNull("mapEntry", t.getMapEntry());
      assertEquals("toString", "Key: key, MapEntry: {}", t.toString());
   }


   @Test
   public void test4() {
      final HashMapEntry t = new HashMapEntry();
      t.setKey("key");
      t.setMapEntry(new HashMap<>());
      t.setValue("value");

      assertEquals("key", "key", t.getKey());
      assertEquals("value", "value", t.getValue());
      assertNotNull("mapEntry", t.getMapEntry());
      assertEquals("toString", "Key: key, MapEntry: {}, Value: value", t.toString());
   }


   @Test
   public void test5() {
      final HashMapEntry t = new HashMapEntry();
      t.setMapEntry(new HashMap<>());

      assertNull("key", t.getKey());
      assertNull("value", t.getValue());
      assertNotNull("mapEntry", t.getMapEntry());
      assertEquals("toString", "MapEntry: {}", t.toString());
   }


   @Test
   public void test6() {
      final HashMapEntry t = new HashMapEntry();
      t.setValue("value");

      assertNull("key", t.getKey());
      assertEquals("value", "value", t.getValue());
      assertNull("mapEntry", t.getMapEntry());
      assertEquals("toString", "Value: value", t.toString());
   }
}
