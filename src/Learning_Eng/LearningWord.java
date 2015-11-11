package Learning_Eng;

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

import org.jsoup.Jsoup;

import English_Standardlize.Stemmer;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class LearningWord {
	public static String password = "";
	// Can xoa bo header hay k ?
	public static boolean NEED_REMOVE_HEADER = false;	
	static Connection connection = null;
	// so mail trong folder
	static int numOfSpamMail = 700;
	static int numOfHamMail = 700;

	public static void main(String[] args) {
		// hoc tu
		learningEngMail();		
		
	}

	/**
	 * Tien xu ly mail: xoa HTML tag, digit, special character
	 * 
	 * @param filePath
	 *            : duong dan file mail
	 * @return
	 */
	public static String preprocessMail(String filePath) {
		Boolean meetBodyLine = false;
		StringBuilder lines = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			// remove mail header
			while ((line = br.readLine()) != null) {
				if (NEED_REMOVE_HEADER) {
					if (line.equals("")) {
						meetBodyLine = true;
						continue;
					}
					if (meetBodyLine == true) {
						lines.append(" " + line);
					}
				} else {
					lines.append(" " + line);
				}
			}
			br.close();
		} catch (IOException e) {

		}
		// remove HTML tag and special character

		String text = Jsoup.parse(lines.toString()).text();
		text = text.replaceAll("[^A-Za-z ]", "");
		return text;

	}

	/**
	 * Ket noi database
	 * 
	 * @return
	 */
	public static Connection getConnectDatabase() {
		String url = "jdbc:mysql://localhost:3306/machine_learning";
		String username = "root";
		String password = "444455555";
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
	 * @param which : table name
	 * @param word : tu
	 * @param numOfSpam : so lan xuat hien trong spam
	 * @param numOfHam : so lan xuat hien trong ham
	 * @param spamFreq : tan suat xuat hien trong tap spam
	 * @param hamFreq : tan suat xuat hien trong tap ham
	 * @param pSpam : xac suat tinh theo spam
	 * @param pHam : xac suat tinh theo ham
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
	public static void learningEngMail() {
		
		System.out.println("Begin learning mail!");
		Long time = System.currentTimeMillis();
		File spamDir = new File(Constant.DATA_LEARN_HTML_3[0]);
		File[] listSpamFile = spamDir.listFiles();		
		numOfSpamMail = listSpamFile.length;
		File hamDir = new File(Constant.DATA_LEARN_HTML_3[1]);
		File[] listHamFile = hamDir.listFiles();
		numOfHamMail = listHamFile.length;
		System.out.println("Learning spam mail!");
		float index = 0;
		for (File file : listSpamFile) {
			index++;			
			learnSpamMail(getTokens(processMail(file.getAbsolutePath()), true));
			System.out.println("Learning progress .... "+index*100/(numOfHamMail+numOfSpamMail));
		}				
		System.out.println("Learning ham mail!");
		for (File file : listHamFile) {
			index++;
			learnHamMail(getTokens(processMail(file.getAbsolutePath()), false));
			System.out.println("Learning progress .... "+index*100/(numOfHamMail+numOfSpamMail));
		}

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
		for (WordObject word : listWord) {
			WordObject wordTemp = null;
			if ((wordTemp = getWordFromDatabase(Constant.WORD_TABLE_3, word.word)) != null) {
				wordTemp.spam_mail += 1;
				wordTemp.spam_frequent += word.spam_frequent;
				updateDatabase(Constant.WORD_TABLE_3, wordTemp.word, wordTemp.spam_mail,
						wordTemp.ham_mail, wordTemp.spam_frequent,
						wordTemp.ham_frequent, wordTemp.p_spam, wordTemp.p_ham);
			} else {
				insertDatabase(Constant.WORD_TABLE_3, word.word, 1, 0, word.spam_frequent,
						word.ham_frequent, word.p_spam, word.p_ham);
			}
		}
	}

	/**
	 * Lấy ra token cùng với tần suất xuất hiện trong 1 văn bản
	 * 
	 * @param listWord
	 * @param isSpam
	 * @return
	 */
	public static ArrayList<WordObject> getTokens(ArrayList<String> listWord,
			boolean isSpam) {
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
				tokens.add(new WordObject(words.get(i), 1, 0, 0, frequents
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
		for (WordObject word : listWord) {
			WordObject wordTemp = null;
			if ((wordTemp = getWordFromDatabase(Constant.WORD_TABLE_3, word.word)) != null) {
				wordTemp.ham_mail++;
				wordTemp.ham_frequent += word.ham_frequent;
				updateDatabase(Constant.WORD_TABLE_3, wordTemp.word, wordTemp.spam_mail,
						wordTemp.ham_mail, wordTemp.spam_frequent,
						wordTemp.ham_frequent, wordTemp.p_spam, wordTemp.p_ham);
			} else {
				insertDatabase(Constant.WORD_TABLE_3, word.word, 0, 1, word.spam_frequent,
						word.ham_frequent, word.p_spam, word.p_ham);
			}
		}
	}

	/**
	 * Xử lý mail: tách token
	 * 
	 * @param filePath
	 * @return
	 */
	public static ArrayList<String> processMail(String filePath) {
		ArrayList<String> listWord = new ArrayList<String>();
		PTBTokenizer<CoreLabel> ptbt;
		ptbt = new PTBTokenizer<>(new StringReader(preprocessMail(filePath)),
				new CoreLabelTokenFactory(), "");
		while (ptbt.hasNext()) {
			CoreLabel label = ptbt.next();
			String word = label.toString().toLowerCase();
			if (word.startsWith("http"))
				word = "http";			
			if (word.length() > 45)
				continue;
			word = standardlize(word);
			listWord.add(word);
		}
		return listWord;
	}
	/**
	 * Chuan hoa tu
	 * Bo ing, ed, es, s, ational, ment, ...
	 * @param word
	 * @return
	 */
	public static String standardlize(String word){
		String result = "";
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		hash.put(word, 0);
		Stemmer.StemHashTable(hash);
		for(String temp:hash.keySet())
        {
            result = temp;
        }
		return result;
	} 

}
