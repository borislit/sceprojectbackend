package sce.finalprojects.sceprojectbackend.factories;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sce.finalprojects.sceprojectbackend.database.DatabaseOperations;
import sce.finalprojects.sceprojectbackend.datatypes.DocDO;
import sce.finalprojects.sceprojectbackend.managers.CacheManager;
import sce.finalprojects.sceprojectbackend.managers.CacheManager.ObjectType;

public class DocFactory extends BaseFactory<DocDO> {

	@Override
	/**
	 * This method will call ONLY in case the DOC didn't exist in the cache
	 * Will generate the DOC from XMLRepresentation
	 */
	protected DocDO handle(String DocArticleId) {
		
		String xmlrep = DatabaseOperations.getXMLRepresentation(DocArticleId);
		Document _doc = this.getDocumentFromXml(xmlrep);
		return new DocDO(DocArticleId,_doc);
	}

	@Override
	protected ObjectType getType() {
		return CacheManager.ObjectType.DOC;
	}
	
	/**
	 * helper to get a DOC initiate with the XML of the article
	 * @param xmlRepresentation
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocumentFromXml(String xmlRepresentation){
		
		// Create a factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// Use document builder factory
		DocumentBuilder builder;
		Document doc;
		try {
			builder = factory.newDocumentBuilder();
			//Parse the document
			Reader reader = new CharArrayReader(xmlRepresentation.toCharArray());
			doc = builder.parse(new InputSource(reader));
			return doc;

		} catch (Exception e) {e.printStackTrace();}
		
		return null;
		
	}

}
