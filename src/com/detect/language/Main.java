/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detect.language;

import huanpc.learning.eng.Constant;
import huanpc.learning.eng.LearningWord;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.swing.text.html.HTML;

import jdk.nashorn.internal.codegen.CompilerConstants;

import org.xml.sax.SAXException;

/**
 *
 * @author Telosma
 */
public class Main {

    public static void main(String[] args) throws SQLException {
       ParseXML2CSV a = new ParseXML2CSV("./1", "./1_out.txt");
//        String textvn = "Tham dự phiên họp có Ủy viên Bộ Chính trị - Trưởng Ban Tuyên giáo Trung ương Đinh Thế Huynh,, Phó Thủ tướng Chính phủ Vũ Đức Đam, lãnh đạo một số bộ, ngành Trung ương và các chuyên gia trong lĩnh vực giáo dục, phát triển nguồn nhân lực.\n hình triển khai và kết quả đạt được trong việc thực hiện Nghị quyết số ";
//
//        String textel = "Militants linked to Islamic State have claimed they brought the plane down.\n" ;
//        
//        File dir = new File(Constant.DATA_LEARN_NO_HTML_1[1]);
//		File[] subFile = dir.listFiles();
//		int size = subFile.length;
//		double numSpam = 0;
//		double i = 0;
//		for (File f : subFile) {
//			WordMatch wm = new WordMatch();
//			String te = LearningWord.preprocessMail(f.getAbsolutePath());
//            if (wm.detectVN(te)) {
////                System.out.println("Ngôn ngữ Tiếng Việt");
//            }
//            else {
////                System.out.println("Ngôn ngữ tiếng Anh");
//            	i++;
//            }			
//		}
//		double a = i / size;
//		System.out.println(String.valueOf(a));
////"The Metrojet Airbus 321, bound for St Petersburg, crashed in Egypt's Sinai desert just 23 minutes after take-off from Sharm el-Sheikh on Saturday.\n" +
////"Most of those on board the plane were Russian.";
//////        Conn getconn = new Conn();
////        Statement stm = getconn.getStatement("vietdic");
////        //String qr = "select distinct * from vietdic limit 100";
////
////        //ResultSet rslt = stm.executeQuery(qr);
////            
////        ResultSet rslt;
////        SplitWord li = new SplitWord();
////        String[] exText = li.parseToArray(text);
////        int j = 0;
////        
////        
////           for (int i = 0; i < exText.length; i++) {
////               String qr = "select distinct * from vietdic where word=\"";
////               qr+= exText[i];
////               qr+="\"";
////               System.out.println(qr);
////               rslt = stm.executeQuery(qr);
////               System.out.println(exText[i]);
////               rslt.last();
////               int size = rslt.getRow();
////               System.out.println(size);
////                if (size!=0) {
////                    
////                    j++;
////                  ;
////                }
////            
////
////        }
////        
//
////        for (int i = 0; i < exText.length; i++) {
////            System.out.println(i);
////           Resulset tmp = rslt;
////            while (rslt.next()) {
////                String a = rslt.getString("word");
////                String b = exText[i];
////                // System.out.println(a + "\n");
////                if (a.equalsIgnoreCase(b)) {
////                    j++;
////                    
////                }
////            }
////        }
//        //     System.out.println(exText[3].length());
//        /*   KIỂM TRA KẾt quá*/
////            System.out.println("Từ điển : \n");
////         while (rslt.next()) {
////        System.out.println(rslt.getString("word")+ " \n");
////         }
////        System.out.println("Tập dữ liệu : \n");
////        for (int i = 0; i < exText.length; i++) {
////            System.out.println(exText[i]);
////        }
////
////        System.out.println("Số từ đúng là :" + j);
////
////        WordMatch wm = new WordMatch();
////       System.out.println(wm.getNumWordMatch(textvn, true));
////        System.out.println(wm.getNumWordMatch(textel, true));
//////        
////        
//        
////        Conn connMachine = new Conn();
////        Statement stm_MachineLearning = connMachine.getStatement("machine_learning", "eldic");
////        ResultSet rspop = connMachine.getResultSet("machine_learning", "learning_words","word");
////       
////                while (rspop.next()){
////                        
////                    String query = "insert into eldic"
////                        +" values(\""
////                        +rspop.getString("word")
////                        + "\")";
////                    stm_MachineLearning.executeQuery(query);
////               
// //               }
//        // Kiểm tra ngôn ngữ của dữ liệu đầu vào
//                        WordMatch wm = new WordMatch();
//                        if (wm.detectVN(textvn)) {
//                            System.out.println("Ngôn ngữ Tiếng Việt");
//                        }
//                        else {
//                            System.out.println("Ngôn ngữ tiếng Anh");
//                        }

    }
}
