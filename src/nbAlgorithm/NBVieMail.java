package nbAlgorithm;

import huanpc.learning.eng.KeyWords;
import huanpc.learning.eng.LearningWord;
import huanpc.learning.eng.WordObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import abc.MachineLearningVietNamese;

import com.mysql.jdbc.Statement;

/**
 * 
 * @author huanpc
 *
 */
public class NBVieMail {
	public static int n = 0;
	public int numOfLearn = 700;
	static Connection connection = null;
	double m = 1;
	static double alpha = 0;
	static double beta = 0;
	static boolean NEAD_REMOVED_HEADER = false;
	static String table = main.Main.TABLE;
	static String condition = main.Main.condition;
	double sumSpamFre = getSumSpamFre();
	double sumHamFre = getSumHamFre();
	// double numWordHam = getNumWordHam();
	// double numWordSpam = getNumWordSpam();
	protected static KeyWords keyWords = null;
	/**
	 * 
	 * @param table
	 * @param condition
	 * @param flag
	 */
	public NBVieMail(String table, String condition,boolean flag){
		this.table = table;
		this.condition = condition;
		this.NEAD_REMOVED_HEADER = flag;
	} 	
	/**
	 * Hàm phân loại
	 * 
	 * @param spamDir
	 * @param hamDir
	 */
	public void mailClassify(String spamDir, String hamDir) {
		LearningWord learningWord = new LearningWord(NEAD_REMOVED_HEADER,table,new String []{spamDir,hamDir});
		String condition = this.condition;
		keyWords = learningWord.getKeyWordsFromDB(table, condition);
		// test spam
		File dir = new File(spamDir);
		File[] subFile = dir.listFiles();
		int size = subFile.length;
		double numSpam = 0;
		for (File f : subFile) {
			MachineLearningVietNamese mc = new MachineLearningVietNamese();
			
			try {
				String[] text = mc.getStringFromFile(f);
				if (nbEng2(text) == true)
					numSpam++;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		double a = numSpam / size;
		// test ham
		dir = new File(hamDir);
		subFile = dir.listFiles();
		double numHam = 0;
		size = subFile.length;
		for (File f : subFile) {
			MachineLearningVietNamese mc = new MachineLearningVietNamese(); 
			try {
				if (nbEng2(mc.getStringFromFile(f)) == false)
					numHam++;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		double b = numHam / size;
		System.out.println("\n Spam:" + String.valueOf(a) + " Ham:"
				+ String.valueOf(b)+"Num of key words: "+keyWords.getLenght());
		
	}

	/**
	 * phân biệt dựa trên số lần xuất hiện của từ trong tập mail
	 * 
	 * @param listWord
	 * @return
	 */
	public boolean nbEng2(String [] listWord) {
		String word = null;
		WordObject ob;
		double spam = Math.log((double)62/142);
		double ham = Math.log((double)80/142)	;
		double spamFre = 0.0;
		double hamFre = 0.0;
		for (int i = 0; i < listWord.length; i++) {
			double pSpam = 0, pHam = 0;
			String wO = listWord[i];			
			ob = keyWords.searchKeyWord(wO);
			if (ob != null) {
				spamFre = ob.spam_frequent;
				hamFre = ob.ham_frequent;
				 if(spamFre==0){
				 pSpam = 1/(double)keyWords.getLenght();
				 pHam = (hamFre) / (sumHamFre);
				 }else if(hamFre==0){
				 pSpam = (spamFre) / (sumSpamFre);
				 pHam = 1/(double)keyWords.getLenght();
				 }else{
				 pSpam = (spamFre) / (sumSpamFre );
				 pHam = (hamFre) / (sumHamFre );
				 }
//				pSpam = (spamFre + 1) / (sumSpamFre + keyWords.getLenght());
//				pHam = (hamFre + 1) / (sumHamFre + keyWords.getLenght());
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

//				System.out.println("unclass");
			}
			System.out.println("thu khong phai la spam " + alpha / beta);
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
			rs = stmt.getResultSet();
			while (rs.next()) {
				sumHam = rs.getInt(1);
			}
		} catch (SQLException e) {
		}
		return sumHam;
	}

}
