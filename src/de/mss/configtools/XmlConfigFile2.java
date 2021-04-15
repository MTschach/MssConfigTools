package de.mss.configtools;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class XmlConfigFile2 extends ConfigFile {


   private class ConfigObject {

      public String                        value       = null;
      public HashMap<String, ConfigObject> configValue = null;
   }


   public XmlConfigFile2(Map<String, String> values) {
      super(values);
   }


   public XmlConfigFile2(String filename) {
      super(filename);
   }


   private HashMap<String, ConfigObject> addEntry(HashMap<String, ConfigObject> recursiveConfigValues, String key, String value) {
      if (recursiveConfigValues == null) {
         recursiveConfigValues = new HashMap<>();
      }


      if (!key.contains(this.configSeparator)) {
         return addValue(recursiveConfigValues, key, value);
      }

      final String newKey = key.substring(0, key.indexOf(this.configSeparator));
      final String subKey = key.substring(key.indexOf(this.configSeparator) + 1);

      final ConfigObject co = new ConfigObject();
      co.configValue = addEntry(recursiveConfigValues.get(newKey).configValue, subKey, value);
      recursiveConfigValues.put(newKey, co);

      return recursiveConfigValues;
   }


   private HashMap<String, ConfigObject> addValue(HashMap<String, ConfigObject> recursiveConfigValues, String key, String value) {
      if (recursiveConfigValues == null) {
         recursiveConfigValues = new HashMap<>();
      }
      final ConfigObject co = new ConfigObject();
      co.value = value;

      recursiveConfigValues.put(key, co);

      return recursiveConfigValues;
   }


   @Override
   public void loadConfig(String cfg, boolean append) {
      if (!append) {
         clearConfig();
      }

      try {
         final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         final DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
         final Document doc = dbBuilder.parse(new InputSource(new StringReader(cfg)));

         doc.getDocumentElement().normalize();

         readNodeList(doc.getChildNodes(), "");
      }
      catch (final Exception e) {
         e.printStackTrace();
      }
   }


   private void readNode(Node node, String path, int actDepth) {
      switch (node.getNodeType()) {
         case Node.DOCUMENT_NODE:
         case Node.ELEMENT_NODE:
         case Node.ENTITY_REFERENCE_NODE:
            readNodeList(node.getChildNodes(), (path.length() > 0 ? path + this.configSeparator : "") + node.getNodeName(), actDepth + 1);
            break;

         case Node.TEXT_NODE:
            if (node instanceof Text) {
               readTextNode((Text)node, path);
            }
            break;
         default:
            break;
      }
   }


   private void readNodeList(NodeList childNodes, String path) {
      readNodeList(childNodes, path, 0);
   }


   private void readNodeList(NodeList childNodes, String path, int actDepth) {
      if (actDepth >= MAX_DEPTH || childNodes == null) {
         return;
      }

      for (int i = 0; i < childNodes.getLength(); i++ ) {
         readNode(childNodes.item(i), path, actDepth);
      }
   }


   private void readTextNode(Text node, String path) {
      if (isSet(node.getNodeValue())) {
         this.configValues.put(path, new ConfigValue(node.getNodeValue()));
         //         System.out.println(path + "=" + node.getNodeValue());
      }
   }


   @Override
   public String writeConfig() {
      final StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
      sb.append(this.lineSeparator);

      HashMap<String, ConfigObject> recursiveConfigValues = null;

      for (final Entry<String, ConfigValue> entry : this.configValues.entrySet()) {
         recursiveConfigValues = addEntry(recursiveConfigValues, entry.getKey(), entry.getValue().getOrigValue());
      }

      sb.append(writeConfigValues(recursiveConfigValues, ""));

      return sb.toString();
   }


   private String writeConfigValues(HashMap<String, ConfigObject> recursiveConfigValues, String spacer) {
      final StringBuilder sb = new StringBuilder();

      if (recursiveConfigValues == null) {
         return "";
      }

      for (final Entry<String, ConfigObject> entry : recursiveConfigValues.entrySet()) {
         if (entry.getValue().value != null) {
            sb.append(spacer + "<" + entry.getKey() + ">" + entry.getValue().value + "</" + entry.getKey() + ">" + this.lineSeparator);
         } else if (entry.getValue().configValue != null) {
            sb.append(spacer + "<" + entry.getKey() + ">" + this.lineSeparator);
            sb.append(writeConfigValues(entry.getValue().configValue, spacer + "   "));
            sb.append(spacer + "</" + entry.getKey() + ">" + this.lineSeparator);
         }
      }

      return sb.toString();
   }
}
