import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is responsible for all XML reading and writing
 */
public class XMLP {
	List<Element> eList= new ArrayList<Element>();
	DocumentBuilderFactory dbFactory;
	DocumentBuilder docBuilder;
	Document document;
	NodeList myNodeList;
	String path = "./File.xml";
	
/**
 * reads from the XML file to get the number of existing entries then
 * writes a new entry depending on user input
 */
	public void write(String programName, ArrayList<String> options){
		try {
			
			//read the file
			dbFactory = DocumentBuilderFactory.newInstance();
			docBuilder = dbFactory.newDocumentBuilder();
			//ADD VALIDATION FOR IF IT EXISTS
			//CREATE A NEW ONE IF IT DOESN'T, NEEDS
			//THE HEADER AND THE <BASE>
			document = docBuilder.parse(path);
			
			//count the number of pages that already exist
			myNodeList = document.getElementsByTagName("Page");
			int nextID = myNodeList.getLength();
			
			//obtain the base directory, make a new page
			Element baseE = document.getDocumentElement();
			Element newpage = document.createElement("Page");
			newpage.setAttribute("id",""+ nextID);
			
			//add all new options under the new page then append newpage to the base
			eList.add(document.createElement("program"));
			eList.get(0).appendChild(document.createTextNode(programName));
			newpage.appendChild(eList.get(0));
			for(int i = 1; i<=options.size();i++){
					eList.add(document.createElement("option"));
					eList.get(i).appendChild(document.createTextNode(options.get(i-1)));
					eList.get(i).setAttribute("id", ""+i);
					newpage.appendChild(eList.get(i));
			}
			baseE.appendChild(newpage);
			
			//write a new XML file	
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(path));
			transformer.transform(source, result);
			System.out.println("file saved!");
	
		}catch (ParserConfigurationException e) {
			e.printStackTrace();
		}catch (SAXException | IOException e) {
			e.printStackTrace();
		}catch (TransformerException tfe){
			tfe.printStackTrace();
		}
	}// write
	
	/**
	 * Parses the XML document, each <Page></Page> as a Node in NodeList
	 */
	public void read(){
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			docBuilder = dbFactory.newDocumentBuilder();
			document = docBuilder.parse(path);
			myNodeList = document.getElementsByTagName("Page"); //array of all Page elements
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}//read
	
	/**
	 * getters for the number of pages and the myNodeList
	 */
	public int getNumPages(){
		return myNodeList.getLength();
	}
	public NodeList getNodeList(){
		return myNodeList;
	}
}//class