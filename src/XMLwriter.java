import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




public class XMLwriter {
	private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder docBuilder;
	private static Document doc;
	private static Element rootElement;
	private static TransformerFactory transformerFactory;
	private static Transformer transformer;
	private static DOMSource source;
	private static StreamResult result;
	
	
	
	protected static void createXML(ArrayList<PackageHit> packs, String name, String output) throws ParserConfigurationException, TransformerException{
		docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		rootElement = doc.createElement("interesting_packages");
		rootElement.setAttribute("type", "OSDEP XML");
		doc.appendChild(rootElement);
		
		for(int i=0; i<packs.size(); i++){
			Element entry = doc.createElement("package");
			entry.setAttribute("id", i+"");
			
			Element nameEntry = doc.createElement("name");
			nameEntry.appendChild(doc.createTextNode(packs.get(i).getName()));
			entry.appendChild(nameEntry);
			
			Element desigEntry = doc.createElement("designation");
			desigEntry.appendChild(doc.createTextNode(packs.get(i).getDesig()));
			entry.appendChild(desigEntry);
			
			
			Element annotEntry = doc.createElement("annotation");
			annotEntry.appendChild(doc.createTextNode(packs.get(i).getAnnot()));
			entry.appendChild(annotEntry);
			
			for(int j = 0; j<packs.get(i).getNumberOfFilepaths(); j++){
				Element pathEntry = doc.createElement("filepath");
				pathEntry.setAttribute("id", j+"");
				pathEntry.appendChild(doc.createTextNode(packs.get(i).getFilepaths().get(j)));
				entry.appendChild(pathEntry);
			}
			
			rootElement.appendChild(entry);
		}
		
		transformerFactory = TransformerFactory.newInstance();
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		source = new DOMSource(doc);
		if(!name.contains(".xml") && !name.contains("\\")){
			result = new StreamResult(new File(output+"\\"+name+".xml"));
			
		}
		else if(name.contains("\\") && !name.contains(".xml")){
			result = new StreamResult(new File(name+".xml"));
		}
		else if(!name.contains("\\") && name.contains(".xml")){
			result = new StreamResult(new File(output+"\\"+name));
		}
		else{
			result = new StreamResult(new File(name));
		}
		
		// Output to console for testing
//		StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
		
		
	}
	
	protected static ArrayList<PackageHit> readXML(String filepath) throws ParserConfigurationException, SAXException, IOException{
		ArrayList<PackageHit> packs = new ArrayList<PackageHit>();
		
		File file = new File(filepath);
		
		docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.parse(file);
		
		doc.getDocumentElement().normalize();
		
		if(doc.getElementsByTagName("interesting_packages") != null && doc.getElementsByTagName("interesting_packages").item(0).hasAttributes() && doc.getElementsByTagName("interesting_packages").item(0).getAttributes().item(0).getNodeValue().equals("OSDEP XML")){
			
			NodeList packList = doc.getElementsByTagName("package");
			
			for(int i=0; i<packList.getLength(); i++){
				Element entry = (Element) packList.item(i);
				PackageHit pack = new PackageHit(entry.getElementsByTagName("name").item(0).getTextContent());
				
				pack.setDesig(entry.getElementsByTagName("designation").item(0).getTextContent());
				
				if(!entry.getElementsByTagName("annotation").item(0).getTextContent().equals(null)){
					pack.setAnnot(entry.getElementsByTagName("annotation").item(0).getTextContent());
					pack.setTextField(entry.getElementsByTagName("annotation").item(0).getTextContent());
				}
				
				NodeList pathList = entry.getElementsByTagName("filepath");
				
				for(int j=0; j<pathList.getLength(); j++){
					Element path = (Element) pathList.item(j);
					pack.addFilepath(path.getTextContent());
				}
				
				packs.add(pack);
				
			}
			return packs;
		}else{
			return null;
		}
		
		
	}
}
