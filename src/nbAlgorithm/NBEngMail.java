package nbAlgorithm;

import huanpc.learning.eng.Constant;
import huanpc.learning.eng.KeyWords;
import huanpc.learning.eng.LearningWord;
import huanpc.learning.eng.WordObject;

import java.io.BufferedReader;

import main.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.detect.language.Main;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
/**
 * Lớp chứa các phương thức cần cho quá trình phân loại mail
 * @author vuonglam, huanpc
 *
 */
public class NBEngMail {
	public static int n = 0;
	public int numOfLearn = 700;
	static Connection connection = null;
	double m = 1;
	static double alpha = 0;
	static double beta = 0;
	static final String table = main.Main.TABLE;
	final String condition = main.Main.condition;
	double sumSpamFre = getSumSpamFre();
	double sumHamFre = getSumHamFre();
	// double numWordHam = getNumWordHam();
	// double numWordSpam = getNumWordSpam();
	protected static KeyWords keyWords = null;

	/**
	 * phân biệt dựa trên số mail xuất hiện từ
	 * 
	 * @param listWord
	 * @return
	 */
	public boolean nbEng1(ArrayList<WordObject> listWord) {
		String word = null;
		double spam = 1;
		double ham = 1;
		double spam_mail, ham_mail;
		WordObject ob;
		double size = listWord.size();
		for (int i = 0; i < listWord.size(); i++) {
			word = listWord.get(i).word;
			ob = keyWords.searchKeyWord(word);
			if (ob != null) {
				spam_mail = ob.spam_mail;
				if (spam_mail == 0) {
					spam_mail = 1;
				}
				ham_mail = ob.ham_mail;
				if (ham_mail == 0) {
					ham_mail = 1;
				}
				spam = (spam * ((spam_mail) / numOfLearn));
				ham = (ham * ((ham_mail) / numOfLearn));
			}
		}
		if (spam > ham) {
			System.out.println("Tan suat spam: " + spam);
			System.out.println("Tan suat ham: " + ham);
			System.out.println("thu la spam");
			return true;
		} else {

			System.out.println("Tan suat spam: " + spam);
			System.out.println("Tan suat ham: " + ham);
			System.out.println("thu khong phai la spam");
			return false;
		}

	}

	/**
	 * Hàm phân loại
	 * 
	 * @param spamDir
	 * @param hamDir
	 */
	public void mailClassify(String spamDir, String hamDir) {
		LearningWord learningWord = new LearningWord();
		String condition = this.condition;
		keyWords = learningWord.getKeyWordsFromDB(table, condition);
		// test spam
		File dir = new File(spamDir);
		File[] subFile = dir.listFiles();
		int size = subFile.length;
		double numSpam = 0;
		for (File f : subFile) {
			if (nbEng2(learningWord.getTokens(
					learningWord.processMail(f.getAbsolutePath()), true)) == true)
				numSpam++;
		}
		double a = numSpam / size;
		// test ham
		dir = new File(hamDir);
		subFile = dir.listFiles();
		double numHam = 0;
		size = subFile.length;
		for (File f : subFile) {
			if (nbEng2(learningWord.getTokens(
					learningWord.processMail(f.getAbsolutePath()), false)) == false)
				numHam++;
		}
		double b = numHam / size;
		System.out.println("\n Spam:" + String.valueOf(a) + " Ham:"
				+ String.valueOf(b) + " Num of unclass: ");
	}

	/**
	 * phân biệt dựa trên số lần xuất hiện của từ trong tập mail
	 * 
	 * @param listWord
	 * @return
	 */
	public boolean nbEng2(ArrayList<WordObject> listWord) {
		String word = null;
		WordObject ob;
		double spam = 1;
		double ham = 1;
		double spamFre = 0.0;
		double hamFre = 0.0;
		for (int i = 0; i < listWord.size(); i++) {
			double pSpam = 0, pHam = 0;
			WordObject wO = listWord.get(i);
			word = wO.word;
			ob = keyWords.searchKeyWord(word);
			if (ob != null) {
				spamFre = ob.spam_frequent;
				hamFre = ob.ham_frequent;
				// if(spamFre==0){
				// pSpam = 1/(double)keyWords.getLenght();
				// // pHam = (hamFre + 1) / (sumHamFre + keyWords.getLenght());
				// pHam = (hamFre) / (sumHamFre);
				// }else if(hamFre==0){
				// // pSpam = (spamFre + 1) / (sumSpamFre +
				// keyWords.getLenght());
				// pSpam = (spamFre) / (sumSpamFre);
				// pHam = 1/(double)keyWords.getLenght();
				// }else{
				// // pSpam = (spamFre + 1) / (sumSpamFre +
				// keyWords.getLenght());
				// // pHam = (hamFre + 1) / (sumHamFre + keyWords.getLenght());
				// pSpam = (spamFre) / (sumSpamFre );
				// pHam = (hamFre) / (sumHamFre );
				// }
				pSpam = (spamFre + 1) / (sumSpamFre + keyWords.getLenght());
				pHam = (hamFre + 1) / (sumHamFre + keyWords.getLenght());
				spam = spam + Math.log(pSpam);
				ham = ham + Math.log(pHam);
			}

		}
		Double s = new Double(spam);
		Double h = new Double(ham);
		System.out.println("xac suat hau nghiem spam: " + s.toString());
		System.out.println("xac suat hau nghiem ham: " + h.toString());
		n++;
		if (s.compareTo(h) > 0) {
			if (n >= 360) {
				alpha += s.doubleValue() - h.doubleValue();
				beta++;
			}
			System.out.println("thu la spam");
			return true;
		} else {
			if (s.compareTo(h) == 0) {

				System.out.println("unclass");
			}
			System.out.println("thu khong phai la spam " + alpha / beta);
			return false;
		}
	}

	/**
	 * phân biệt dựa trên cả 2 : số mail xuất hiện, số lần xuất hiện
	 * 
	 * @param listWord
	 * @return
	 */
	public boolean nbEng3(ArrayList<WordObject> listWord) {
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
		for (int i = 0; i < listWord.size(); i++) {
			word = listWord.get(i).word;
			ob = LearningWord.getWordFromDatabase("learning_words", word);
			if (ob != null) {
				spamFre = ob.spam_frequent;
				hamFre = ob.ham_frequent;
				spamMail = ob.spam_mail;
				hamMail = ob.ham_mail;
				if (spamFre == 0) {
					pSpam = Math.log10(0.0001);
					pHam = Math.log10(0.9999);
				} else if (hamFre == 0) {
					pHam = Math.log10(0.0001);
					pSpam = Math.log10(0.9999);
				} else {
					pSpam = Math
							.log10(((spamFre * spamMail) / sumSpamMail)
									/ ((spamMail * spamFre) / sumSpamMail + (hamMail * sumHamFre)
											/ sumHamMail));
					pHam = Math
							.log10(((hamFre * hamMail) / sumHamMail)
									/ ((spamMail * spamFre) / sumSpamMail + (hamMail * sumHamFre)
											/ sumHamMail));
				}
			} else {
				pSpam = Math.log10(1);
				pHam = Math.log10(1);
			}
			spam = spam + pSpam;
			ham = ham + pHam;
		}

		System.out.println("xac suat hau nghiem spam" + spam);
		System.out.println("xac suat hau nghiem ham" + ham);
		if (spam > ham) {
			System.out.println("thu la spam");
			return true;
		} else {
			System.out.println("thu khong phai la spam");
			return false;
		}
	}

	/**
	 * tính tổng số lần xuất hiện của tất cả các từ trong tập mail với nhãn spam
	 * 
	 * @return
	 */
	public double getSumSpamFre() {
		double sumSpam = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select sum(spam_frequent) from " + table + " where "
					+ this.condition);
			// stmt.execute("select sum(spam_frequent) from " + table +
			// " where spam_mail>0");
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
	 * 
	 * @return
	 */
	public double getSumHamFre() {
		double sumHam = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select sum(ham_frequent) from " + table + " where "
					+ this.condition);
			// stmt.execute("select sum(ham_frequent) from " + table +
			// " where ham_mail>0");
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
	 * 
	 * @return
	 */
	public static double getNumWordHam() {
		double numWord = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select count(*) from " + table
					+ " where ham_frequent>0");
			rs = stmt.getResultSet();
			while (rs.next()) {
				numWord = rs.getInt(1);
			}
		} catch (SQLException e) {
		}
		return numWord;
	}

	/**
	 * Đang k dùng
	 * @return
	 */
	public static double getNumWordSpam() {
		double numWord = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select count(*) from " + table
					+ " where spam_frequent>0");
			rs = stmt.getResultSet();
			while (rs.next()) {
				numWord = rs.getInt(1);
			}
		} catch (SQLException e) {
		}
		return numWord;
	}
	/**
	 * 
	 * @return
	 */
	public static double getSumSpamMail() {
		double sumSpam = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select sum(spam_mail) from " + table);
			rs = stmt.getResultSet();
			while (rs.next()) {
				sumSpam = rs.getInt(1);
			}
		} catch (SQLException e) {
		}
		return sumSpam;
	}

	public static double getSumHamMail() {
		double sumHam = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select sum(ham_mail) from " + table);
			rs = stmt.getResultSet();
			while (rs.next()) {
				sumHam = rs.getInt(1);
			}
		} catch (SQLException e) {
		}
		return sumHam;
	}

	public static double getAvgHamFrequent() {
		double sumHam = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select avg(ham_frequent) from " + table
					+ " where ham_frequent >0");
			rs = stmt.getResultSet();
			while (rs.next()) {
				sumHam = rs.getDouble(1);
			}
		} catch (SQLException e) {
		}
		return sumHam;
	}

	public static double getAvgSpamFrequent() {
		double sumHam = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = LearningWord.getConnectDatabase();
		try {
			stmt = (Statement) conn.createStatement();
			stmt.execute("select avg(spam_frequent) from " + table
					+ " where spam_frequent >0");
			rs = stmt.getResultSet();
			while (rs.next()) {
				sumHam = rs.getDouble(1);
			}
		} catch (SQLException e) {
		}
		return sumHam;
	}

}
