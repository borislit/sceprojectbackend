package sce.finalprojects.sceprojectbackend.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.Acell;
import sce.finalprojects.sceprojectbackend.datatypes.XmlElement;
//import commentsTreatment.articleWords;


public class xmlGenerator {

	private int numberOfMerges;
	private Queue<Element> treeQueue;
	private Queue<XmlElement> queue;
	private HashMap<String, Queue<XmlElement>> mergeMap;   //hash map of cluster_id to suns (cluster_id)
	private ArrayList<Acell> a;
	public String xmlHacRepresentation;
	private int[] level; ///counts the number of elements in each level
	
	public xmlGenerator(String articleId, ArrayList<Acell> ma , int numberOfComments) {
			
		level = new int[numberOfComments];
		this.a = ma;
		XmlElement temp;
		
		 try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				
				numberOfMerges = a.size();
				treeQueue = new LinkedList<Element>();
				Element sunOfRoot;

				String root = initMergerMap();
				buildQueue(root);

				//creating the root element
				temp = queue.poll();
				Element rootElement = doc.createElement("Cluster");
				rootElement.setAttribute("id",temp.id);
				rootElement.setAttribute("level", ""+temp.level);
				rootElement.setAttribute("mergeSim", ""+temp.mergeSim);
				doc.appendChild(rootElement);
				level[temp.level]++;
				pushToqueues(temp);
				
				
				while(numberOfMerges >= 0 )
				{
					//adding the sun on level + 1
					temp = queue.poll();
					sunOfRoot = doc.createElement("Cluster");
					sunOfRoot.setAttribute("id",temp.id);
					if(temp.id.equals(rootElement.getAttribute("id")))
						sunOfRoot.setAttribute("mergeSim", "1");
					else
						sunOfRoot.setAttribute("mergeSim", ""+temp.mergeSim);
					
					sunOfRoot.setAttribute("level", ""+temp.level);
					rootElement.appendChild(sunOfRoot); //adding to XML
					treeQueue.add(sunOfRoot);  //adding to levelOrder queue
					level[temp.level]++;
					pushToqueues(temp); //adding the suns of temp

					//check if the node doesn't have more ( so its can only add itself)
					if(queue.peek().id.equalsIgnoreCase(rootElement.getAttribute("id"))
							|| (mergeMap.containsKey(temp.id) && !mergeMap.get(temp.id).isEmpty() ))
					{
						//adding the root on level +1 if there any child's
						temp = queue.poll();
						sunOfRoot = doc.createElement("Cluster");
						sunOfRoot.setAttribute("id",temp.id);
						sunOfRoot.setAttribute("level", ""+temp.level);
						sunOfRoot.setAttribute("mergeSim", ""+temp.mergeSim);
						rootElement.appendChild(sunOfRoot); //adding to XML
						treeQueue.add(sunOfRoot);  //adding to levelOrder queue
						level[temp.level]++;
						pushToqueues(temp); //adding the suns of temp

					}
					
					//TODO: check the last level
					// if the last level already contains all the comments
					if(level[temp.level-1] >= (numberOfComments))
						break;
					rootElement = treeQueue.poll();
				}	
				
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("C:\\file.xml"));
		 
				transformer.transform(source, result);
				
			    DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
			    LSSerializer lsSerializer = domImplementation.createLSSerializer();
				xmlHacRepresentation = lsSerializer.writeToString(doc);
			
				DatabaseOperations.setXmlRepresentation(articleId,xmlHacRepresentation);
		
		 }catch (Exception e)
		 {
			 e.printStackTrace();
		 }
	}
	
	/**
	 * push sun of the node to the queue with their merge level
	 * fill in the simMerge as the next merge sim level
	 * <BR> and push into tree queue the suns of the root
	 * @param node that his sun will added to the queue
	 */
	private void pushToqueues(XmlElement node) {

		if(mergeMap.containsKey(node.id) && !mergeMap.get(node.id).isEmpty())
		{
			XmlElement temp = mergeMap.get(node.id).poll();
			numberOfMerges--;
			node.level++;
			temp.level = node.level;

			//fill in the merge of the inserted elements
			if(mergeMap.containsKey(temp.id) && !mergeMap.get(temp.id).isEmpty())
				temp.mergeSim = mergeMap.get(temp.id).peek().mergeSim;
			else
				temp.mergeSim = 1;
			if(mergeMap.containsKey(node.id) &&!mergeMap.get(node.id).isEmpty())
				node.mergeSim = mergeMap.get(node.id).peek().mergeSim;
			queue.add(temp);
			queue.add(node);
		}
		else {
			node.level++;
			queue.add(node);
		}
	}
	
	
	/**
	 * Initialized the queue of merges to build the XML (only the root is in the queue)
	 * @param root must be the root of the XML (the last merge)
	 * @throws exeption about: <li> root that don't have suns
	 * <li>no root exist
	 */
	private void buildQueue(String root) throws Exception {
		
		queue = new LinkedList<XmlElement>();
		
		if(!mergeMap.containsKey(root))
			throw new Exception("root element didnt exist");
		
		queue.add(new XmlElement(a.get(a.size()-1).a1,0,a.get(a.size()-1).mergeSim));
	}
	
	/**
	 * initiate the merges map with XmlElements from the a array
	 * @return the start (root) cluster
	 */ 
	private String initMergerMap() {
		
	Queue<XmlElement> temp;
	String root = null;
	mergeMap = new HashMap<String, Queue<XmlElement>>();
		
		for (int i = (a.size()-1) ; i >= 0 ; i--) {
			if(i == (a.size()-1))
				root = a.get(i).a1;
			if(!mergeMap.containsKey(a.get(i).a1)) //in case that the cluster is not yet in the map
			{
				temp = new LinkedList<XmlElement>();
				temp.add(new XmlElement(a.get(i).a2, 0, a.get(i).mergeSim));
				mergeMap.put(a.get(i).a1, temp );
			}
			else //case that the cluster is in the map already
			{
				mergeMap.get(a.get(i).a1).add(new XmlElement(a.get(i).a2, 0, a.get(i).mergeSim));
			}
		}
		return root;
	}
}

