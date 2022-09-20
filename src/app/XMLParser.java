package app;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.*;
import java.util.HashMap;

public class XMLParser {
	public static HashMap parseProfile(InputSource xmlInputSource) throws ParserConfigurationException, SAXException, IOException, TransformerException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.parse(xmlInputSource);
		
		HashMap<String, String> profileInfo = new HashMap<String, String>();
		
		NodeList profiles = doc.getElementsByTagName("profile");
		
		for (int i = 0; i < profiles.getLength(); i++) {
			Node profile = profiles.item(i);
			Element profileElement = (Element) profile;
			
			profileInfo.put("name", profileElement.getAttribute("name"));
			profileInfo.put("version", profileElement.getAttribute("version"));
		
			//Solution to remove white space from DOM document found here: https://stackoverflow.com/a/979606/6689085
				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");  
				NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
	
				for (int j = 0; j < emptyTextNodes.getLength(); j++) {
				    Node emptyTextNode = emptyTextNodes.item(j);
				    emptyTextNode.getParentNode().removeChild(emptyTextNode);
				}
			//End stackoverflow solution
				
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			
			transformer.transform(new DOMSource(profile), result);
			
			profileInfo.put("xml", writer.toString());
		}
		
		return profileInfo;
	}
	
	public static HashMap parseProfileFromString(String xmlString) throws ParserConfigurationException, SAXException, IOException, TransformerException, XPathExpressionException {
		return parseProfile(new InputSource(new StringReader(xmlString)));
	}
}
