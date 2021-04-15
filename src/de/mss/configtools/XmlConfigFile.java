package de.mss.configtools;

import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class XmlConfigFile extends ConfigFile {


   public XmlConfigFile(Map<String, String> values) {
      super(values);
   }


   public XmlConfigFile(String filename) {
      super(filename);
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
      }
   }


   @Override
   public String writeConfig() {
      final StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
      sb.append(this.lineSeparator);

      final Map<String, HashMapEntry> mapEntries = toHierarchicalStructure();

      sb.append(writeConfigValues(mapEntries, ""));

      return sb.toString();
   }


   private String writeConfigValues(Map<String, HashMapEntry> recursiveConfigValues, String spacer) {
      final StringBuilder sb = new StringBuilder();

      if (recursiveConfigValues == null) {
         return "";
      }

      for (final Entry<String, HashMapEntry> entry : recursiveConfigValues.entrySet()) {
         if (entry.getValue().getValue() != null) {
            sb.append(spacer + "<" + entry.getKey() + ">" + entry.getValue().getValue() + "</" + entry.getKey() + ">" + this.lineSeparator);
         } else if (entry.getValue().getMapEntry() != null) {
            sb.append(spacer + "<" + entry.getKey() + ">" + this.lineSeparator);
            sb.append(writeConfigValues(entry.getValue().getMapEntry(), spacer + "   "));
            sb.append(spacer + "</" + entry.getKey() + ">" + this.lineSeparator);
         }
      }

      return sb.toString();
   }
}
