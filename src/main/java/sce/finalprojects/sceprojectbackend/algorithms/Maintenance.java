package sce.finalprojects.sceprojectbackend.algorithms;

import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.Cluster;
import sce.finalprojects.sceprojectbackend.datatypes.Comment;
import sce.finalprojects.sceprojectbackend.datatypes.MapCell;
/**
 * this class is responsible of creating a mapping from XML and for adding a new element to the HAC
 * @author the source
 *
 */
public class Maintenance {
	
	public Maintenance() {}
	
	/**
	 * Convert the HAC XML representation into a mapping array to reduce
	 * the cost of scanning the xml tree 
	 * @param articleId
	 * @throws Exception
	 */
	public void mapXmlHacToClusters(String articleId) throws Exception {
		
		///dummy calls to DB
		String xmlRepresentation = DatabaseOperations.getXMLRepresentation(articleId);
		ArrayList<Comment> arrayOfComments = DatabaseOperations.getAllComentsWithoutHTML(articleId);
		///dummy calls - END
		ArrayList<Cluster> clustersArray = Cluster.makeClustersArray(arrayOfComments);
		
		ArrayList<MapCell> mapping = new ArrayList<MapCell>();
		Element fatherElement;
		NodeList childNodes;
		Element tempChild;
		Cluster childcluster;
		Cluster fatherCluster;
		
		Document doc = getDocumentFromXml(xmlRepresentation); 
		
		//Evaluate XPath against Document itself
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		Element root = doc.getDocumentElement();
		int currentLevel = getMaxLevel(root);
		int lengthOfNodeList;
		
		///with the root level
		while(currentLevel > 0) {
			
			currentLevel--;
			//Retrieve the nodes of the level that before the last level
			NodeList nodesList = (NodeList)xPath.evaluate("//Cluster[@level = "+currentLevel+" ]",
					doc.getDocumentElement(), XPathConstants.NODESET);
			lengthOfNodeList = nodesList.getLength();
			//take every element from the current level and add his children's comments to the mapping
			for(int i = 0 ; i < lengthOfNodeList ; i++) {

				fatherElement = (Element) nodesList.item(i);
				fatherCluster = new Cluster(Cluster.findClusterByIdFromArray(clustersArray, fatherElement.getAttribute("id")));
				if(fatherCluster.cluster_id == null)
					throw new Exception("cluster didnt found");
				///get the elements children
				childNodes = fatherElement.getChildNodes();

				//for each child get the inner comments of its matching cluster and add a new entry in mapping
				for(int j = 0 ; j < childNodes.getLength() ; j++) {

					tempChild = (Element) childNodes.item(j);
					childcluster  = Cluster.findClusterByIdFromArray(clustersArray, tempChild.getAttribute("id"));
					if(childcluster == null)
						throw new Exception("cluster didnt found");
					//add the entry to the map for each comment that belongs to the child cluster

					for (Comment comment : childcluster.innerComments) {
						mapping.add(new MapCell(articleId , comment.comment_id, fatherElement.getAttribute("id")+"_"+currentLevel));
					}

					//merge the children into the father cluster
					fatherCluster.mergeWithCluster(childcluster);
				}	
				
				clustersArray = Cluster.removeUnavailableClustersFromArray(clustersArray);
				clustersArray.add(fatherCluster);
			}	
		}
		DatabaseOperations.setArticleMapping(articleId,mapping);
	}

	/**
	 * helper to get a DOC initiate with the XML of the article
	 * @param xmlRepresentation
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocumentFromXml(String xmlRepresentation)
			throws ParserConfigurationException, SAXException, IOException {
		// Create a factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// Use document builder factory
		DocumentBuilder builder = factory.newDocumentBuilder();
		//Parse the document
		Reader reader = new CharArrayReader(xmlRepresentation.toCharArray());
		Document doc = builder.parse(new InputSource(reader));
		return doc;
	}
	
	
	/**
	 * Traverse the tree until finding the last level
	 * @param root the root of the tree
	 * @return the max level of the tree
	 */
	private int getMaxLevel(Element root) {
		
		Element last = root;
		do {
			 last = (Element) last.getLastChild();
		}
		while(last.getLastChild() != null);
		return Integer.parseInt(last.getAttribute("level"));
	}
	
	public void addNewElementsToHAC(ArrayList<Comment> neArray, String articleId, double[] vector) throws Exception {
		
		
		for (Comment ne : neArray) {
			Comment.nomalizeCommentVector(ne);
			addNewElementToHAC(ne, articleId, vector);
		}
	}
	
	
	/**
	 * set the new comment ne into the right place in the HAC
	 * <br> updates the XML representation
	 * <br> update the mapping
	 * @param ne
	 * @param articleId
	 * @param vector
	 * @throws Exception 
	 */
	public void addNewElementToHAC(Comment ne, String articleId, double[] vector ) throws Exception {
		
		//replace this calls with real values
		ArrayList<MapCell> mappingArray = DatabaseOperations.getArticleMapping(articleId);   ///this should be replaced from dummy call to a real call
		String xmlRepresentation = DatabaseOperations.getXMLRepresentation(articleId); 
		ArrayList<Comment> arrayOfComments = DatabaseOperations.getAllComentsWithoutHTML(articleId); ///this should be replaced from dummy call to a real call
		//end
		
		ArrayList<Cluster> clustersArray = Cluster.makeClustersArray(arrayOfComments);
		Cluster newElement = new Cluster(ne);
		Cluster childcluster;
		Cluster fatherCluster;
		
		Document doc = getDocumentFromXml(xmlRepresentation); 
		
		NodeList childNodes;
		NodeList nodesList;
		
		Element fatherElement;
		Element tempChild;
		Element newXMLElement;
		Element root = doc.getDocumentElement();
		
		//Evaluate XPath against Document itself
		XPath xPath = XPathFactory.newInstance().newXPath();
		int lastLevel = getMaxLevel(root);
		int maxlevel = lastLevel;
		double sim = 0;
		double maxSim = 0;
		int maxSimIndex = -1;
		int lengthOfNodeList;
		
		while(lastLevel > 0)
		{
			lastLevel--;
			//if the New element isn't merged with none of the levels until the root, merge it to the root
			if(lastLevel == 0) {
				fatherElement = root;
				//add the NE to the XML concatenate it until the last level
				do{
					newXMLElement = doc.createElement("Cluster");
					newXMLElement.setAttribute("id", newElement.cluster_id);
					newXMLElement.setAttribute("level", ""+ (Integer.parseInt(fatherElement.getAttribute("level"))+1) );
					newXMLElement.setAttribute("mergeSim", ""+1);
					//adding to XML
					fatherElement.appendChild(newXMLElement); 
					//adding to mapping array
					mappingArray.add(new MapCell(articleId, newElement.cluster_id,""+fatherElement.getAttribute("id")+"_"+fatherElement.getAttribute("level") ));
					fatherElement = newXMLElement;

				}
				while(maxlevel > Integer.parseInt(fatherElement.getAttribute("level")));
				break;
			}

			//Retrieve the nodes of the level that before the last level
			nodesList = (NodeList)xPath.evaluate("//Cluster[@level = "+lastLevel+" ]",
					doc.getDocumentElement(), XPathConstants.NODESET);

			lengthOfNodeList = nodesList.getLength();
			double minSim = 2;

			//take every element from the current level and add his children's comments to the mapping
			for(int i = 0 ; i < lengthOfNodeList ; i++) {

				fatherElement = (Element) nodesList.item(i);
				//find the maximum similarity of the level
				if(Double.parseDouble(fatherElement.getAttribute("mergeSim")) < minSim)
					minSim = Double.parseDouble(fatherElement.getAttribute("mergeSim"));
				fatherCluster = new Cluster(Cluster.findClusterByIdFromArray(clustersArray, fatherElement.getAttribute("id")));
				if(fatherCluster.cluster_id == null)
					throw new Exception("cluster didnt found");
				///get the elements children
				childNodes = fatherElement.getChildNodes();

				//for each child get the inner comments of its matching cluster and add a new entry in mapping
				for(int j = 0 ; j < childNodes.getLength() ; j++) {

					tempChild = (Element) childNodes.item(j);
					childcluster  = Cluster.findClusterByIdFromArray(clustersArray, tempChild.getAttribute("id"));
					if(childcluster == null)
						throw new Exception("cluster didnt found");
					//merge the children into the father cluster
					fatherCluster.mergeWithCluster(childcluster);
				}

				clustersArray = Cluster.removeUnavailableClustersFromArray(clustersArray);
				clustersArray.add(fatherCluster);
			}
			sim = 0;
			maxSim = 0;
			maxSimIndex = -1;
			///finding the maximum similarity element 
			for(int i = 0 ; i < lengthOfNodeList ; i++) {
				tempChild = (Element) nodesList.item(i);
				Cluster element = Cluster.findClusterByIdFromArray(clustersArray, tempChild.getAttribute("id"));
				sim = element.GAAC(element,newElement, vector);
				if(sim > maxSim) {
					maxSim = sim;
					maxSimIndex = i;
				}	
			}

			//adding new element as sun of the max sim element
			if((maxSim > minSim) && maxSimIndex != -1) {
				tempChild = (Element) nodesList.item(maxSimIndex);
				//replacing the similarity of the root to be the similarity with the new elements
				tempChild.removeAttribute("mergeSim");
				tempChild.setAttribute("mergeSim", ""+maxSim);
				fatherElement = tempChild;
				//add the NE to the XML concatenate it until the last level
				do{
					newXMLElement = doc.createElement("Cluster");
					newXMLElement.setAttribute("id", newElement.cluster_id);
					newXMLElement.setAttribute("level", ""+ (Integer.parseInt(fatherElement.getAttribute("level"))+1) );
					newXMLElement.setAttribute("mergeSim", ""+1);
					//adding to XML
					fatherElement.appendChild(newXMLElement); 
					//adding to mapping array
					mappingArray.add(new MapCell(articleId, newElement.cluster_id,""+fatherElement.getAttribute("id")+"_"+fatherElement.getAttribute("level") ));
					fatherElement = newXMLElement;
				}
				while(maxlevel > Integer.parseInt(fatherElement.getAttribute("level"))); 
				lastLevel = 0;
			}
		}
			
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("C:\\file.xml"));
		transformer.transform(source, result);
		DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
		LSSerializer lsSerializer = domImplementation.createLSSerializer();
		
		///writing to DB
		DatabaseOperations.setXmlRepresentation(articleId,lsSerializer.writeToString(doc));
		DatabaseOperations.setArticleMapping(articleId,mappingArray);
		//TODO: Check why if i'm adding a new element the father of the element after running the algorithm is getting wrong merge sim number
	}

	
}
