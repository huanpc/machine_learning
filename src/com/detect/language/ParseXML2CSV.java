/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detect.language;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
//import org.w3c.dom.Entity;
//import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Telosma
 */
public class ParseXML2CSV {
//    public static void main(String[] args) throws SAXException, IOException {
//        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//        try {
//            DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
//            Document document = (Document) dBuilder.parse("C:\\Users\\Telosma\\Documents\\NetBeansProjects\\JavaApplication10\\src\\com\\detect\\00001-05000.xml");
//            NodeList headwordLists = document.getElementsByTagName("HeadWord");
//            //headwordLists.item(0);
//            File file = new File("C:\\Users\\Telosma\\Documents\\NetBeansProjects\\JavaApplication10\\src\\com\\detect\\00001-05000-out.txt");
//  
//            // if file doesnt exists, then create it
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bwriter = new BufferedWriter(fw);
//            
//            System.out.println("Danh sach tu la:\n");
//            for (int i = 0; i < headwordLists.getLength(); i++){
//                Node n = headwordLists.item(i);
//                if (n.getNodeType() == Node.ELEMENT_NODE){
//                    Element headword = (Element) n;
//                    headword.getTextContent().replace("\n", " ").replaceAll("\\s", " ");
//                    bwriter.write(headword.getTextContent());
//                    bwriter.write("\n");
//                    System.out.println( i+1 + " "+
//                            headword.getTagName()+" = " + headword.getTextContent() + "\n");
//                            
//                }
//                
//            }
//        } catch (ParserConfigurationException ex) {
//            Logger.getLogger(XMLDictionary.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        catch(SAXException e){
//            e.printStackTrace();
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        
//        }

//    }
    public ParseXML2CSV(String inpath, String outpath) {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inpath);
            NodeList wordList = doc.getElementsByTagName("text");
            File file = new File(outpath);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < wordList.getLength(); i++) {
                Node word = wordList.item(i);
                if (word.getNodeType() == Node.ELEMENT_NODE) {
                    word.getTextContent().replace("\n", " ").replaceAll("\\s", " ");
                    bw.write(word.getTextContent());
                    bw.write("\n");
                }
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ParseXML2CSV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
