package nbAlgorithm;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import Learning_Eng.Constant;
import Learning_Eng.LearningWord;
import Learning_Eng.WordObject;;

public class NBEngMail {
	  public static int n = 0;	
	  public int numOfLearn =350;
	  static Connection connection = null;
	  double m = 1;	  
//	  double alpha = Math.pow(10,-100);
	  double alpha = 0;
	  static String table = Constant.WORD_TABLE_1;	
	  /**
	   * phân biệt dựa trên số mail xuất hiện từ
	   * @param listWord
	   * @return
	   */
	  public boolean nbEng1(ArrayList<WordObject> listWord){
		  String word = null;
		  double spam = 0.5;
		  double ham = 0.5;
		  double spam_mail, ham_mail;
		  WordObject ob;
		  double size = listWord.size();
		  for (int i = 0; i < listWord.size(); i++ ){
			  word = listWord.get(i).word;
			  ob = LearningWord.getWordFromDatabase(table, word);
			  if(ob != null){
				  spam_mail = ob.spam_mail;
				  ham_mail = ob.ham_mail;			  
			  }else
			  {
				  spam_mail = 1;
				  ham_mail = 1;
				  continue;
			  }
			  spam = (spam * ((spam_mail) / numOfLearn));
			  ham = (ham * ((ham_mail) / numOfLearn));
		  }
		  if (spam > ham){
			  System.out.println("Tan suat spam: " + spam);
			  System.out.println("Tan suat ham: " + ham);
			  System.out.println("thu la spam");
			  return true;
		  }else{

			  System.out.println("Tan suat spam: " + spam);
			  System.out.println("Tan suat ham: " + ham);
			  System.out.println("thu khong phai la spam");
			  return false;
		  }
		  
	  }
	  
	  /**
	   * phân biệt dựa trên số lần xuất hiện của từ trong tập mail
	   * @param listWord
	   * @return
	   */
	  public boolean nbEng2(ArrayList<WordObject> listWord){
		  String word = null;
		  WordObject ob;		  
		  double spam = Math.log(0.5);
		  double ham = Math.log(0.5);
		  double sumSpamFre = getSumSpamFre();
		  double sumHamFre = getSumHamFre();
		  double numWordHam = getNumWordHam();
		  double numWordSpam = getNumWordSpam();
		  double spamFre = 0.0;
		  double hamFre = 0.0;		  		 
		  for (int i = 0; i < listWord.size(); i++ ){
			  double pSpam = 0, pHam = 0;
			  word = listWord.get(i).word;
			  ob = LearningWord.getWordFromDatabase(table, word);
			  if(ob != null){
				  spamFre = ob.spam_frequent;
				  hamFre = ob.ham_frequent;
				  if(spamFre == 0){
//					  pSpam = (this.m/numWord)/(numOfLearn+m);
					  pSpam = 1/numWordSpam;
					  pHam = (hamFre + 1) / (sumHamFre + numWordHam);
				  }else if(hamFre == 0){
					  pSpam = (spamFre + 1) / (sumSpamFre + numWordSpam);
//					  pHam = (this.m/numWord)/(numOfLearn+m);
					  pHam = 1/numWordHam;
				  }else{
					  pSpam = (spamFre + 1) / (sumSpamFre + numWordSpam);
					  pHam = (hamFre + 1) / (sumHamFre + numWordHam);
				  }
		      }else{	
		    	  pSpam = 1/numWordSpam;
		    	  pHam = 1/numWordHam;
//		    	  pSpam = (this.m/numWord)/(350+m);
//		    	  pHam = (this.m/numWord)/(350+m);
//		    	  continue;
//		    	  spamFre = 0;
//		    	  hamFre = 0;				  
		      }
			  double f = Math.log(pSpam);
			  spam = spam + Math.log(pSpam);
			  ham = ham + Math.log(pHam);
			  
		  }
		  Double s = new Double(spam);
		  Double h = new Double(ham);
		  System.out.println("xac suat hau nghiem spam: " + s.toString());
		  System.out.println("xac suat hau nghiem ham: " + h.toString());
		  
		  if (s.compareTo(h)>0){
			  System.out.println("thu la spam");
			  return true;
		  }else{
			  if (s.equals(h)){
				  n++;
				  System.out.println("unclass");
			  }
			  System.out.println("thu khong phai la spam "+n);
			  return false;
		  }
	  }	  
	  
	  /**
	   * phân biệt dựa trên cả 2 : số mail xuất hiện, số lần xuất hiện
	   * @param listWord
	   * @return
	   */
	  public boolean nbEng3(ArrayList<WordObject> listWord){
		  String word = null;
		  WordObject ob;
		  double spam = Math.log10(0.5);
		  double ham = Math.log10(0.5);
		  double sumSpamFre = getSumSpamFre();
		  double sumHamFre = getSumHamFre();
		  double sumSpamMail = getSumSpamMail();
		  double sumHamMail = getSumHamMail();
		  double spamMail;
		  double hamMail;
		  double spamFre = 0.0;
		  double hamFre = 0.0;
		  double pSpam, pHam;
		  for (int i = 0; i < listWord.size(); i++ ){
			  word = listWord.get(i).word;
			  ob = LearningWord.getWordFromDatabase("learning_words", word);
			  if(ob != null){
				  spamFre = ob.spam_frequent;
				  hamFre = ob.ham_frequent;
				  spamMail = ob.spam_mail;
				  hamMail = ob.ham_mail;
				  if(spamFre == 0 ){
					  pSpam = Math.log10(0.0001);
					  pHam = Math.log10(0.9999);
				  }
				  else if(hamFre == 0){
					  pHam = Math.log10(0.0001);
					  pSpam = Math.log10(0.9999);
				  }else{
				  pSpam = Math.log10(((spamFre * spamMail) / sumSpamMail) 
						  / ((spamMail * spamFre) / sumSpamMail + (hamMail * sumHamFre) / sumHamMail));
				  pHam = Math.log10(((hamFre * hamMail) / sumHamMail) 
						  / ((spamMail * spamFre) / sumSpamMail + (hamMail * sumHamFre) / sumHamMail));
				  }
			  }else{
					pSpam = Math.log10(1);
					pHam = Math.log10(1);
				  }
				  spam = spam + pSpam;
				  ham = ham + pHam;
			  }
		  		  		  
		  System.out.println("xac suat hau nghiem spam" + spam);
		  System.out.println("xac suat hau nghiem ham" + ham);
		  if (spam > ham){
			  System.out.println("thu la spam");
			  return true;
		  }else{
			  System.out.println("thu khong phai la spam");
			  return false;
		  }	
	  }	  

		/**
		 * tính tổng số lần xuất hiện của tất cả các từ trong tập mail với nhãn spam
		 * @return
		 */
		public static double getSumSpamFre(){
			double sumSpam = 0.0;
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = LearningWord.getConnectDatabase();
			try {
				stmt = (Statement) conn.createStatement();
				stmt.execute("select sum(spam_frequent) from "+table);
				rs = stmt.getResultSet();
				while (rs.next()) {
					sumSpam = rs.getInt(1);
				}
			} catch (SQLException e) {
			}		
			return sumSpam;
		}
		
		/**
		 * tính tổng số lần xuất hiện của tất cả các từ trong tập mail với nhãn ham
		 * @return
		 */
		public static double getSumHamFre(){
			double sumHam = 0.0;
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = LearningWord.getConnectDatabase();
			try {
				stmt = (Statement) conn.createStatement();
				stmt.execute("select sum(ham_frequent) from "+table);
				rs = stmt.getResultSet();
				while (rs.next()) {
					sumHam = rs.getInt(1);
				}
			} catch (SQLException e) {
			}		
			return sumHam;
		}
				
		/**
		 * tính tổng số từ học được trong tất cả các văn bản
		 * @return
		 */
		public static double getNumWordHam(){
			double numWord = 0.0;
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = LearningWord.getConnectDatabase();
			try {
				stmt = (Statement) conn.createStatement();
				stmt.execute("select count(*) from "+table+" where ham_frequent>0");
				rs = stmt.getResultSet();
				while (rs.next()) {
					numWord = rs.getInt(1);
				}				
			} catch (SQLException e) {
			}		
			return numWord;
		}
		public static double getNumWordSpam(){
			double numWord = 0.0;
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = LearningWord.getConnectDatabase();
			try {
				stmt = (Statement) conn.createStatement();
				stmt.execute("select count(*) from "+table+" where spam_frequent>0");
				rs = stmt.getResultSet();
				while (rs.next()) {
					numWord = rs.getInt(1);
				}				
			} catch (SQLException e) {
			}		
			return numWord;
		}
		

		public static double getSumSpamMail(){
			double sumSpam = 0.0;
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = LearningWord.getConnectDatabase();
			try {
				stmt = (Statement) conn.createStatement();
				stmt.execute("select sum(spam_mail) from "+table);
				rs = stmt.getResultSet();
				while (rs.next()) {
					sumSpam = rs.getInt(1);
				}
			} catch (SQLException e) {
			}		
			return sumSpam;
		}
		
		public static double getSumHamMail(){
			double sumHam = 0.0;
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = LearningWord.getConnectDatabase();
			try {
				stmt = (Statement) conn.createStatement();
				stmt.execute("select sum(ham_mail) from "+table);
				rs = stmt.getResultSet();
				while (rs.next()) {
					sumHam = rs.getInt(1);
				}
			} catch (SQLException e) {
			}		
			return sumHam;
		}
		
		public static ArrayList<WordObject> getKeyWordFromDatabase(String condition){
			ArrayList<WordObject> listKeys = new ArrayList<WordObject>();
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = LearningWord.getConnectDatabase();
			WordObject wordObject = null;
			try {
				stmt = (Statement) conn.createStatement();
				if (stmt.execute("SELECT * FROM " + table + " WHERE "+condition)) {
					rs = stmt.getResultSet();
				}
				while (rs.next()) {
					wordObject = new WordObject(rs.getString(2), rs.getInt(3), rs.getInt(4),
							rs.getInt(5), rs.getInt(6), rs.getFloat(7),
							rs.getFloat(8));
					listKeys.add(wordObject);
				}
			} catch (SQLException ex) {
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());

			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException sqlEx) {
					} // ignore

					rs = null;
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException sqlEx) {
					} // ignore

					stmt = null;
				}
			}			
			return listKeys;
		} 
}

