package de.mss.configtools;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class XmlConfigFile2 extends ConfigFile {
   
   
   public XmlConfigFile2(String filename) {
      super(filename);
   }
   
   
   public XmlConfigFile2(Map<String,String> values) {
      super(values);
   }
   
   
   @Override
   public void loadConfig (String cfg, boolean append) {
      if (!append)
         clearConfig();
      
      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
         Document doc = dbBuilder.parse(new InputSource(new StringReader(cfg)));
         
         doc.getDocumentElement().normalize();
         
         readNodeList (doc.getChildNodes(), "");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   
   private void readNodeList(NodeList childNodes, String path) {
      readNodeList(childNodes, path, 0);
   }


   private void readNodeList(NodeList childNodes, String path, int actDepth) {
      if (actDepth >= MAX_DEPTH || childNodes == null)
         return;
      
      for (int i=0; i<childNodes.getLength(); i++)
         readNode(childNodes.item(i), path, actDepth);
   }
   
   
   private void readNode(Node node, String path, int actDepth) {
      switch (node.getNodeType()) {
         case Node.DOCUMENT_NODE:
         case Node.ELEMENT_NODE:
         case Node.ENTITY_REFERENCE_NODE:
            readNodeList(node.getChildNodes(), (path.length() > 0 ? path + configSeparator : "") + node.getNodeName(), (actDepth+1));
            break;
            
         case Node.TEXT_NODE:
            if (node instanceof Text)
               readTextNode((Text)node, path);
            break;
      }
   }


   private void readTextNode(Text node, String path) {
      if (isSet(node.getNodeValue())) {
         configValues.put(path, node.getNodeValue());
         System.out.println(path + "=" + node.getNodeValue());
      }
   }


   @Override
   public String writeConfig() {
      StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
      sb.append(lineSeparator);
      
      HashMap<String,ConfigObject> recursiveConfigValues = null;
      
      for (Entry<String,String> entry : configValues.entrySet()) {
         recursiveConfigValues = addEntry(recursiveConfigValues, entry.getKey(), entry.getValue());
      }
      
      sb.append(writeConfigValues(recursiveConfigValues, ""));
      
      return sb.toString();
   }


   @SuppressWarnings("unchecked")
   private String writeConfigValues(HashMap<String, ConfigObject> recursiveConfigValues, String spacer) {
      StringBuilder sb = new StringBuilder();
      
      if (recursiveConfigValues == null)
         return "";
      
      for (Entry<String,ConfigObject> entry : recursiveConfigValues.entrySet()) {
         if (entry.getValue().value != null) 
            sb.append(spacer + "<" + entry.getKey() + ">" + entry.getValue().value + "</" + entry.getKey() + ">" + lineSeparator);
         else if (entry.getValue().configValues != null) {
            sb.append(spacer + "<" + entry.getKey() + ">" + lineSeparator);
            sb.append(writeConfigValues((HashMap<String,ConfigObject>)entry.getValue().configValues, spacer + "   "));
            sb.append(spacer + "</" + entry.getKey() + ">" + lineSeparator);
         }
      }
      
      return sb.toString();
   }


   private HashMap<String, ConfigObject> addEntry(HashMap<String, ConfigObject> recursiveConfigValues, String key, String value) {
      if (recursiveConfigValues == null)
         recursiveConfigValues = new HashMap<>();
      
      
      if (!key.contains(configSeparator))
         return addValue(recursiveConfigValues, key, value);
      else {
         String newKey = key.substring(0, key.indexOf(configSeparator));
         String subKey = key.substring(key.indexOf(configSeparator)+1);
         
         ConfigObject co = new ConfigObject();
         co .configValues = addEntry((HashMap<String,ConfigObject>)recursiveConfigValues.get(newKey).configValues, subKey, value);
         recursiveConfigValues.put(newKey, co);
      }
      
      return recursiveConfigValues;
   }


   private HashMap<String, ConfigObject> addValue(HashMap<String, ConfigObject> recursiveConfigValues, String key, String value) {
      if (recursiveConfigValues == null)
         recursiveConfigValues = new HashMap<>();
      ConfigObject co = new ConfigObject();
      co.value = value;
      
      recursiveConfigValues.put(key, co);
      
      return recursiveConfigValues;
   }
   
   
   private class ConfigObject {
      public String value                                   = null;
      public HashMap<String, ConfigObject> configValues     = null;
   }
}
