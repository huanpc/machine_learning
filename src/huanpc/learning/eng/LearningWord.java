package huanpc.learning.eng;

import huanpc.english.standardlize.Stemmer;
import huanpc.english.standardlize.StopWord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.scene.control.Tab;

import org.jsoup.Jsoup;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import config.Config;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
/**
 * 
 * @author huanpc
 *Lớp chứa các phương thức cần cho quá tình học từ
 */
public class LearningWord {
	public static String password = "";
	// Can xoa bo header hay k ?
	static boolean NEED_REMOVE_HEADER = false;
	static Connection connection = null;
	static String table = "";
	public static String data_learn[]=null;			
//	public static ArrayList<WordObject> listSpamWord = new ArrayList<WordObject>();
//	public static ArrayList<String> listWord = new ArrayList<String>();
//	public static ArrayList<Integer> listSpamFre = new ArrayList<Integer>();
//	public static ArrayList<Integer> listHamFre = new ArrayList<Integer>();
//	static ArrayList<WordObject> listHamWord = new ArrayList<WordObject>();
	/**
	 * 
	 * @param flag
	 * @param table
	 * @param data
	 */
	public LearningWord(boolean flag, String table, String [] data){
		this.NEED_REMOVE_HEADER = flag;		 
		this.table = table;
		this.data_learn=data;
	}
	/**
	 * Tien xu ly mail: xoa HTML tag, digit, special character
	 * 
	 * @param filePath
	 *            : duong dan file mail
	 * @return
	 */
//	public static String preprocessMail(String filePath) {
//		Boolean meetBodyLine = false;
//		StringBuilder lines = new StringBuilder();
//		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//			String line;
//			// remove mail header
//			while ((line = br.readLine()) != null) {
//				if (NEED_REMOVE_HEADER) {
//					if (line.equals("")) {
//						meetBodyLine = true;
//						continue;
//					}
//					if (meetBodyLine == true) {
//						lines.append(" " + line);
//					}
//				} else {
//					lines.append(" " + line);
//				}
//			}
//			br.close();
//		} catch (IOException e) {
//
//		}
//		// remove HTML tag and special character
//
//		String text = Jsoup.parse(lines.toString()).text();
//		text = text.replaceAll("[^A-Za-z ]", "");
//		return text;
//
//	}

	/**
	 * Ket noi database
	 * 
	 * @return
	 */
	public static Connection getConnectDatabase() {
		String url = Config.url;
		String username = Config.username;
		String password = Config.password;
		try {
			if (connection == null || connection.isClosed()) {
				connection = (Connection) DriverManager.getConnection(url,
						username, password);
			}
			return connection;
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	/**
	 * Lay mot tu trong database ra
	 * 
	 * @param which
	 *            : table name
	 * @param word
	 *            : tu can lay
	 * @return
	 */
	public static WordObject getWordFromDatabase(String which, String word) {

		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = getConnectDatabase();
		WordObject wordObject = null;
		try {
			stmt = (Statement) conn.createStatement();
			if (stmt.execute("SELECT * FROM " + which + " WHERE word LIKE '%"
					+ word + "%'")) {
				rs = stmt.getResultSet();
			}
			while (rs.next()) {
				wordObject = new WordObject(word, rs.getInt(3), rs.getInt(4),
						rs.getInt(5), rs.getInt(6), rs.getFloat(7),
						rs.getFloat(8));
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
		return wordObject;
	}

	/**
	 * Them mot tu vao csdl
	 * 
	 * @param which
	 *            : table name
	 * @param word
	 *            : tu
	 * @param numOfSpam
	 *            : so lan xuat hien trong spam
	 * @param numOfHam
	 *            : so lan xuat hien trong ham
	 * @param spamFreq
	 *            : tan suat xuat hien trong tap spam
	 * @param hamFreq
	 *            : tan suat xuat hien trong tap ham
	 * @param pSpam
	 *            : xac suat tinh theo spam
	 * @param pHam
	 *            : xac suat tinh theo ham
	 * @return boolean
	 */
	public static boolean insertDatabase(String which, String word,
			int numOfSpam, int numOfHam, int spamFreq, int hamFreq,
			float pSpam, float pHam) {
		Connection con = getConnectDatabase();
		PreparedStatement stmt;
		Statement st;
		int id = 0;
		try {
			st = (Statement) con.createStatement();
			if (st.execute("SELECT * FROM " + which
					+ " ORDER BY id DESC LIMIT 1")) {
				ResultSet rs = st.getResultSet();
				if (rs.next()) {
					id = rs.getInt(1);
				}
			}

			stmt = (PreparedStatement) con.prepareStatement("INSERT INTO "
					+ which + " values(?,?,?,?,?,?,?,?)");
			stmt.setInt(1, ++id);
			stmt.setString(2, word);
			stmt.setInt(3, numOfSpam);
			stmt.setInt(4, numOfHam);
			stmt.setInt(5, spamFreq);
			stmt.setInt(6, hamFreq);
			stmt.setFloat(7, pSpam);
			stmt.setFloat(8, pHam);
			int i = stmt.executeUpdate();
			stmt.close();
			con.close();
			if (i > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Cap nhat mot tu trong database
	 * 
	 * @param table
	 * @param word
	 * @param numOfSpam
	 * @param numOfHam
	 * @param spamFreq
	 * @param hamFreq
	 * @param pSpam
	 * @param pHam
	 * @return
	 */
	public static boolean updateDatabase(String table, String word,
			int numOfSpam, int numOfHam, int spamFreq, int hamFreq,
			float pSpam, float pHam) {
		Connection con = getConnectDatabase();
		try {
			PreparedStatement stmt = (PreparedStatement) con
					.prepareStatement("UPDATE "
							+ table
							+ " set spam_mail=?,ham_mail=?,spam_frequent=?,ham_frequent=?,p_spam=?,p_ham=? where word=?");
			// stmt.setString(1, table);
			stmt.setInt(1, numOfSpam);
			stmt.setInt(2, numOfHam);
			stmt.setInt(3, spamFreq);
			stmt.setInt(4, hamFreq);
			stmt.setFloat(5, pSpam);
			stmt.setFloat(6, pHam);
			stmt.setString(7, word);
			if (stmt.executeUpdate() > 0) {
				stmt.close();
				con.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Học từ
	 */
//	public static void learningEngMail() {
//
//		System.out.println("Begin learning mail!");
//		Long time = System.currentTimeMillis();
//		File spamDir = new File(data_learn[0]);
//		File[] listSpamFile = spamDir.listFiles();
//		int numOfSpamMail = listSpamFile.length;
//		File hamDir = new File(data_learn[1]);
//		File[] listHamFile = hamDir.listFiles();
//		int numOfHamMail = listHamFile.length;
//		System.out.println("Learning spam mail!");
//		float index = 0;
//		for (File file : listSpamFile) {
//			index++;
//			learnSpamMail(getTokens(processMail(file.getAbsolutePath()), true));
//			System.out.println("Learning progress .... " + index * 100
//					/ (numOfHamMail + numOfSpamMail));
//		}
//		System.out.println("Learning ham mail!");
//		for (File file : listHamFile) {
//			index++;
//			learnHamMail(getTokens(processMail(file.getAbsolutePath()), false));
//			System.out.println("Learning progress .... " + index * 100
//					/ (numOfHamMail + numOfSpamMail));
//		}
//
//		System.out.println("DONE!!!");
//		System.out.println("Number of Ham email: " + numOfHamMail
//				+ " Number of Spam email: " + numOfSpamMail);
//		System.out.println(" Time: " + (System.currentTimeMillis() - time)
//				/ 1000);
//
//	}
	public static void learningEngMail() {
		System.out.println("Begin learning mail!");
		Long time = System.currentTimeMillis();
		File spamDir = new File(data_learn[0]);
		File[] listSpamFile = spamDir.listFiles();
		int numOfSpamMail = 3000;
		File hamDir = new File(data_learn[1]);
		File[] listHamFile = hamDir.listFiles();
		int numOfHamMail = 3000;
		System.out.println("Learning spam mail!");
		XMLparse parse = new XMLparse();
		parse.need_stadardlize_xml = false;
		float index = 0;
		for (int i = 0;i<numOfSpamMail;i++) {
			index++;
//			addWordToList(getTokens(processMail(parse.getTextBody(listSpamFile[i])), true), true);
			learnSpamMail(getTokens(processMail(parse.getTextBody(listSpamFile[i])), true));
			System.out.println("Learning progress .... " + index * 100
					/ (numOfHamMail + numOfSpamMail));
		}
//		learnSpamMail(listSpamWord);
		System.out.println("Learning ham mail!");
		for (int i = 0;i<numOfHamMail;i++) {
			index++;
			learnHamMail(getTokens(processMail(parse.getTextBody(listHamFile[i])), false));
//			addWordToList(getTokens(processMail(parse.getTextBody(listHamFile[i])), false), false);
			System.out.println("Learning progress .... " + index * 100
					/ (numOfHamMail + numOfSpamMail));
		}
//		learnHamMail(listHamWord);
		System.out.println("DONE!!!");
		System.out.println("Number of Ham email: " + numOfHamMail
				+ " Number of Spam email: " + numOfSpamMail);
		System.out.println(" Time: " + (System.currentTimeMillis() - time)
				/ 1000);

	}

	/**
	 * Học từ trong tập spam
	 * 
	 * @param listWord
	 *            : danh sach tu can hoc
	 */
	public static void learnSpamMail(ArrayList<WordObject> listWord) {
//		int i = 0;
		int j = listWord.size();
		for (WordObject word : listWord) {			
//			i++;
//			System.out.println((double)i/j);
			WordObject wordTemp = null;
			if ((wordTemp = getWordFromDatabase(table,
					word.word)) != null) {
//				wordTemp.spam_mail += 1;
				wordTemp.spam_mail += word.spam_mail;
				wordTemp.spam_frequent += word.spam_frequent;
				updateDatabase(table, wordTemp.word,
						wordTemp.spam_mail, wordTemp.ham_mail,
						wordTemp.spam_frequent, wordTemp.ham_frequent,
						wordTemp.p_spam, wordTemp.p_ham);
			} else {
				insertDatabase(table, word.word, word.spam_mail, 0,
						word.spam_frequent, 0, word.p_spam,
						word.p_ham);
			}
		}
	}
//	public static void addWordToList(ArrayList<WordObject> list, boolean isSpam){
//		if(isSpam){
//			for (WordObject word : list) {
//				if(listWord.contains(word.word)){
//					int i = listWord.indexOf(word);
////					WordObject temp = listSpamWord.get(i);
////					temp.spam_frequent+=word.spam_frequent;
////					temp.spam_mail+=1;
////					listSpamWord.set(i, temp);
//					listSpamFre.set(i, listSpamFre.get(i).intValue()+word.spam_frequent);
//					listHamFre.set(i, 0);
//				}else{
//					listWord.add(word.word);
//					listSpamFre.add(1);
//					listHamFre.add(0);
//				}
//			}
//		}else{
////			for (WordObject word : listWord) {
////				if(listHamWord.contains(word)){
////					int i = listHamWord.indexOf(word);
////					WordObject temp = listHamWord.get(i);
////					temp.ham_frequent+=word.ham_frequent;
////					temp.ham_mail+=1;
////					listHamWord.set(i, temp);
////				}else{
////					listHamWord.add(word);
////				}
////			}
//			for (WordObject word : list) {
//				if(listWord.contains(word.word)){
//					int i = listWord.indexOf(word);
////					WordObject temp = listSpamWord.get(i);
////					temp.spam_frequent+=word.spam_frequent;
////					temp.spam_mail+=1;
////					listSpamWord.set(i, temp);
////					listSpamFre.set(i, listSpamFre.get(i).intValue()+word.spam_frequent);
//					listHamFre.set(i, listHamFre.get(i).intValue()+word.ham_frequent);
//				}else{
//					listWord.add(word.word);
//					listSpamFre.add(0);
//					listHamFre.add(1);
//				}
//			}
//		}
//		
//	}

	/**
	 * Lấy ra token cùng với tần suất xuất hiện trong 1 văn bản
	 * 
	 * @param listWord
	 * @param isSpam
	 * @return
	 */
	public static ArrayList<WordObject> getTokens(ArrayList<String> listWord,
			boolean isSpam) {
		if(listWord==null)
			return null;
		ArrayList<WordObject> tokens = new ArrayList<WordObject>();
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<Integer> frequents = new ArrayList<Integer>();

		for (String word : listWord) {
			if (!words.contains(word)) {
				words.add(word);
				frequents.add(1);
			} else {
				int index = words.indexOf(word);
				frequents.set(index, frequents.get(index) + 1);

			}
		}
		if (isSpam) {
			for (int i = 0; i < words.size(); i++) {
				tokens.add(new WordObject(words.get(i), 1, 0, frequents.get(i),
						0));
			}
		} else {
			for (int i = 0; i < words.size(); i++) {
				tokens.add(new WordObject(words.get(i), 0, 1, 0, frequents
						.get(i)));
			}
		}

		return tokens;
	}

	/**
	 * Học tập Ham mail
	 * 
	 * @param listWord
	 */
	public static void learnHamMail(ArrayList<WordObject> listWord) {
//		int i = 0;
//		int j = listWord.size();
		for (WordObject word : listWord) {
//			i++;
//			System.out.println((double)i/j);
			WordObject wordTemp = null;
			if ((wordTemp = getWordFromDatabase(table,
					word.word)) != null) {
				wordTemp.ham_mail+=word.ham_mail;
				wordTemp.ham_frequent += word.ham_frequent;
				updateDatabase(table, wordTemp.word,
						wordTemp.spam_mail, wordTemp.ham_mail,
						wordTemp.spam_frequent, wordTemp.ham_frequent,
						wordTemp.p_spam, wordTemp.p_ham);
			} else {
				insertDatabase(table, word.word, 0, word.ham_mail,
						0, word.ham_frequent, word.p_spam,
						word.p_ham);
			}
		}
	}

	/**
	 * Xử lý mail: tách token
	 * 
	 * @param filePath
	 * @return
	 */
//	public static ArrayList<String> processMail(String filePath) {
//		ArrayList<String> listWord = new ArrayList<String>();
//		PTBTokenizer<CoreLabel> ptbt;
//		ptbt = new PTBTokenizer<>(new StringReader(preprocessMail(filePath)),
//				new CoreLabelTokenFactory(), "");
//		while (ptbt.hasNext()) {
//			CoreLabel label = ptbt.next();
//			String word = label.toString().toLowerCase();
//			if (word.startsWith("http"))
//				word = "http";
//			if (word.length() > 45)
//				continue;
//			word = standardlize(word);
//			listWord.add(word);
//		}
//		return listWord;
//	}
	/**
	 * Trả về tập từ đã tách được trong 1 mail
	 * @param textInput
	 * @return
	 */
	public static ArrayList<String> processMail(String textInput) {
		if(textInput == "")
			return null;
		String text = Jsoup.parse(textInput).text();
		text = text.replaceAll("[^A-Za-z ]", "");
		ArrayList<String> listWord = new ArrayList<String>();
//		PTBTokenizer<CoreLabel> ptbt;
//		ptbt = new PTBTokenizer<>(new StringReader(text),
//				new CoreLabelTokenFactory(), "");
//		while (ptbt.hasNext()) {
//			CoreLabel label = ptbt.next();
//			String word = label.toString().toLowerCase();
//			if (word.startsWith("http"))
//				word = "http";
//			if (word.length() > 45)
//				continue;
//			word = standardlize(word);
//			
//		}
		StopWord sw = new StopWord();		
		ArrayList<String> t = sw.letterStopWords(text, false);
		for(String w : t){
			listWord.add(standardlize(w));
		}
		return listWord;
	}
	

	/**
	 * Chuẩn hóa từ, bỏ ing, ed, es, s, ational, ment, ...
	 * 
	 * @param word
	 * @return
	 */
	public static String standardlize(String word) {
		String result = "";
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		hash.put(word, 0);
		Stemmer.StemHashTable(hash);
		for (String temp : hash.keySet()) {
			result = temp;
		}
		return result;
	}	
	/**
	 * Lấy từ khóa từ tập từ học được theo một điều kiện cho trước
	 * 
	 * @param table
	 *            : bang can lay
	 * @param condition
	 *            : dieu kien lay (VD: spam_mail >5% and ham_mail>5%)
	 * @return
	 */
	public KeyWords getKeyWordsFromDB(String table, String condition) {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = getConnectDatabase();
		WordObject wordObject = null;
		KeyWords keyWords = null;
		int i = 0;
		try {
			stmt = (Statement) conn.createStatement();
			if (stmt.execute("SELECT count(*) FROM " + table + " WHERE "
					+ condition)) {
				rs = stmt.getResultSet();
			}
			if (rs.first()) {
				keyWords = new KeyWords(rs.getInt(1));
			} else {
				return null;
			}
			stmt = (Statement) conn.createStatement();
			if (stmt.execute("SELECT * FROM " + table + " WHERE " + condition)) {
				rs = stmt.getResultSet();
			}
			while (rs.next()) {
				keyWords.setData(i, rs.getString(2), rs.getInt(3),
						rs.getInt(4), rs.getInt(5), rs.getInt(6));
				i++;
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
		return keyWords;
	}
}
