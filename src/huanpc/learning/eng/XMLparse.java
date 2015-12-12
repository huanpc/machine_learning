package huanpc.learning.eng;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

public class XMLparse {
	public static boolean need_stadardlize_xml = true;
	public static void main(String[] args) {
	    try {

		File file = new File("./Code Project/Mail datasets/English/SpamMail/test_SPAM/email_5");

		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
	                             .newDocumentBuilder();
		
		Document doc = dBuilder.parse(preprocessXml(file));
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		if (doc.hasChildNodes()) {
			NodeList nList  = doc.getElementsByTagName("message_body");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);															
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
//					Element e = (Element)eElement.getElementsByTagName("text_normal");					
					System.out.println(eElement.getElementsByTagName("text_normal").item(0).getTextContent());
				}
			}
		}

	    } catch (Exception e) {
		System.out.println(e.getMessage());
	    }

	  }
	public String getTextBody(File file){
		String text = "";
		try {

//			File file = new File(filePath);

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
		                             .newDocumentBuilder();
			Document doc = null;
			if(need_stadardlize_xml){
				doc = dBuilder.parse(preprocessXml(file));	
			}
			else{
				doc = dBuilder.parse(file);
			}
			doc.getDocumentElement().normalize();
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			if (doc.hasChildNodes()) {
				NodeList nList  = doc.getElementsByTagName("message_body");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);															
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
//						Element e = (Element)eElement.getElementsByTagName("text_normal");					
//						System.out.println(eElement.getElementsByTagName("text_normal").item(0).getTextContent());
						text = eElement.getElementsByTagName("text_normal").item(0).getTextContent();
					}
				}
			}

		    } catch (Exception e) {
//			System.out.println(e.getMessage());
		    }
		return text;
	}
	public static File preprocessXml(File file){
		String filePath = file.getAbsolutePath();
		File tempFile = new File(filePath+"_");				
//		StringBuilder lines = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
			String line;
			while ((line = br.readLine()) != null) {				
//					lines.append("\n" + line.toLowerCase());
				String tm = line.toLowerCase(); 
//				if(tm.indexOf("&")!=-1){
//					if(tm.charAt((tm.indexOf("&")+1))==32){
//						
//					}
//				}
				tm = tm.replaceAll("&[A-Za-z]", " ");
				tm = tm.replaceAll("!", " ");
				tm = tm.replaceAll("^", " ");
				bw.append(tm);
			}
			br.close();
			bw.close();
		} catch (IOException e) {

		}
		// remove HTML tag and special character
		if(file.delete()){
			tempFile.renameTo(new File(filePath));
		}
		return new File(filePath);
	}

}
