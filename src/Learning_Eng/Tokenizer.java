//package Learning_Eng;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.StringReader;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.jsoup.Jsoup;
//
//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;
//import com.mysql.jdbc.Statement;
//
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.ling.HasWord;
//import edu.stanford.nlp.process.CoreLabelTokenFactory;
//import edu.stanford.nlp.process.DocumentPreprocessor;
//import edu.stanford.nlp.process.PTBTokenizer;
//import edu.stanford.nlp.process.TokenizerFactory;
//
//public class Tokenizer {
//	public static boolean NEED_REMOVE_HEADER = false;
//	//public static final String WORD_FREQUENT_TABLE = "word_frequent";
//	public static final String WORD_SPAM_TABLE = "word_spam";
//	public static final String WORD_SPAM_TABLE_TEMP = "word_spam_temp";
//	public static final String WORD_HAM_TABLE = "word_ham";
//	public static final String WORD_HAM_TABLE_TEMP = "word_ham_temp";
//	//public static final String WORD_FREQUENT_TABLE_TEMP = "word_frequent_temp";
//	// Folder chua mail can xu ly (spam)
//	static String fileSpamPath = "./Code Project/Mail datasets/English/eron/enron1/spam";
//	static String fileHamPath = "./Code Project/Mail datasets/English/eron/enron1/ham";
//	static Connection connection = null;
//	// so mail trong folder
//	static int numOfSpamMail = 20;
//	static int numOfHamMail = 20;
//
//	public static void main(String[] args) {
//		// hoc tu
//		learningEng();
//	}
//
//	/**
//	 * Tien xu ly mail: xoa HTML tag, digit, special character
//	 * 
//	 * @param filePath
//	 *            : duong dan file mail
//	 * @return
//	 */
//	public static String preprocessMail(String filePath) {
//		Boolean meetBodyLine = false;
//		StringBuilder lines = new StringBuilder();
//		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//			String line;
//			// remove mail header
//			while ((line = br.readLine()) != null) {
//				if(NEED_REMOVE_HEADER){
//					if (line.equals("")) {
//						meetBodyLine = true;
//						continue;
//					}
//					if (meetBodyLine == true) {
//						lines.append(" " + line);
//					}
//				}else{
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
//
//	/**
//	 * Ket noi database
//	 * 
//	 * @return
//	 */
//	public static Connection getConnectDatabase() {
//		String url = "jdbc:mysql://localhost:3306/machine_learning";
//		String username = "root";
//		String password = "444455555";
//		try {
//			if (connection == null || connection.isClosed()) {
//				connection = (Connection) DriverManager.getConnection(url,
//						username, password);
//			}
//			return connection;
//		} catch (SQLException e) {
//			throw new IllegalStateException("Cannot connect the database!", e);
//		}
//	}
//
//	/**
//	 * Lay mot tu trong database ra
//	 * 
//	 * @param which
//	 *            : table name
//	 * @param word
//	 *            : tu can lay
//	 * @return
//	 */
//	public static WordObject getWordFromDatabase(String which, String word) {
//
//		Statement stmt = null;
//		ResultSet rs = null;
//		Connection conn = getConnectDatabase();
//		WordObject wordObject = null;
//		try {
//			stmt = (Statement) conn.createStatement();
//			if (stmt.execute("SELECT * FROM " + which + " WHERE word LIKE '%"
//					+ word + "%'")) {
//				rs = stmt.getResultSet();
//			}
//			while (rs.next()) {
//				wordObject = new WordObject(word, rs.getInt(3), rs.getFloat(4),rs.getInt(5));				
//			}
//		} catch (SQLException ex) {
//			// handle any errors
//			System.out.println("SQLException: " + ex.getMessage());
//			System.out.println("SQLState: " + ex.getSQLState());
//			System.out.println("VendorError: " + ex.getErrorCode());
//
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException sqlEx) {
//				} // ignore
//
//				rs = null;
//			}
//
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException sqlEx) {
//				} // ignore
//
//				stmt = null;
//			}
//		}
//		return wordObject;
//	}
//
//	/**
//	 * Them mot tu vao database
//	 * 
//	 * @param which
//	 *            : table name
//	 * @param word
//	 *            : tu can them
//	 * @param frequent
//	 *            : tan suat tu
//	 * @param p
//	 *            : xac suat xuat hien cua tu
//	 * @return
//	 */
//	public static boolean insertDatabase(String which, String word,
//			int numOfMail, float p,int numOfAll) {
//		Connection con = getConnectDatabase();
//		PreparedStatement stmt;
//		Statement st;
//		int id = 0;
//		try {
//			st = (Statement) con.createStatement();
//			if (st.execute("SELECT * FROM " + which
//					+ " ORDER BY id DESC LIMIT 1")) {
//				ResultSet rs = st.getResultSet();
//				if (rs.next()) {
//					id = rs.getInt(1);
//				}
//			}
//
//			stmt = (PreparedStatement) con.prepareStatement("INSERT INTO "
//					+ which + " values(?,?,?,?,?)");
//			stmt.setInt(1, ++id);
//			stmt.setString(2, word);
//			stmt.setInt(3, numOfMail);
//			stmt.setFloat(4, p);
//			stmt.setInt(5, numOfAll);
//			int i = stmt.executeUpdate();
//			stmt.close();
//			con.close();
//			if (i > 0)
//				return true;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return false;
//	}
//
//	/**
//	 * Cap nhat database
//	 * 
//	 * @param table
//	 * @param word
//	 * @param frequent
//	 * @param p
//	 * @return
//	 */
//	public static boolean updateDatabase(String table, String word,
//			int numOfMail, float p,int numOfAll) {
//		Connection con = getConnectDatabase();
//		try {
//			PreparedStatement stmt = (PreparedStatement) con
//					.prepareStatement("UPDATE " + table
//							+ " set num_of_mail=?,p=?,num_of_all=? where word=?");
//			// stmt.setString(1, table);
//			stmt.setInt(1, numOfMail);
//			stmt.setFloat(2, p);
//			stmt.setInt(3, numOfAll);
//			stmt.setString(4, word);
//			if (stmt.executeUpdate() > 0) {
//				stmt.close();
//				con.close();
//				return true;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	/**
//	 * 
//	 */
//	public static void learningEng() {
//		System.out.println("Begin learning mail!");
//		Long time = System.currentTimeMillis();	
//		File spamDir = new File(fileSpamPath);
//		File[] listSpamFile = spamDir.listFiles();
//		//numOfSpamMail = listSpamFile.length;
//		int index = 0;			
//		System.out.println("Learning spam mail!");
//		for (File file : listSpamFile) {
//			index++;
//			if(index>numOfSpamMail) break;
//			learnSpamWord(getKeyWord(processMail(file.getAbsolutePath())));			
//		}
//		//
//		File hamDir = new File(fileHamPath);
//		File[] listHamFile = hamDir.listFiles();
//		//numOfHamMail = listHamFile.length;				
//		index = 0;
//		System.out.println("Learning ham mail!");
//		for (File file : listHamFile) {
//			index++;
//			if(index>numOfHamMail) break;
//			learnHamWord(getKeyWord(processMail(file.getAbsolutePath())));
//		}
//				
//		System.out.println("DONE!!!");
//		System.out.println("Number of Ham email: " + numOfHamMail +" Number of Spam email: "+numOfSpamMail);
//		System.out.println(" Time: "
//				+ (System.currentTimeMillis() - time) / 1000);
//
//	}
//
//	/**
//	 * Hoc tu trong tap spam
//	 * 
//	 * @param listWord
//	 *            : danh sach tu can hoc
//	 */
//	public static void learnSpamWord(ArrayList<WordObject> listWord) {		
//		for (WordObject word : listWord) {
//			WordObject wordTemp = null;
//			if ((wordTemp = getWordFromDatabase(WORD_SPAM_TABLE, word.word)) != null) {
//				wordTemp.num_of_mail++;
//				wordTemp.num_of_all+=word.num_of_all;
//				updateDatabase(WORD_SPAM_TABLE, wordTemp.word,
//						wordTemp.num_of_mail, wordTemp.p,wordTemp.num_of_all);
//			} else if ((wordTemp = getWordFromDatabase(
//					WORD_SPAM_TABLE_TEMP, word.word)) != null) {
//				if (++wordTemp.num_of_mail > 0.25 * numOfSpamMail) {
//					insertDatabase(WORD_SPAM_TABLE, wordTemp.word,
//							wordTemp.num_of_mail, wordTemp.p,wordTemp.num_of_all+word.num_of_all);
//				} else {
//					updateDatabase(WORD_SPAM_TABLE_TEMP, wordTemp.word,
//							wordTemp.num_of_mail, wordTemp.p,wordTemp.num_of_all+word.num_of_all);
//				}
//			} else {
//				insertDatabase(WORD_SPAM_TABLE_TEMP, word.word, 1, 0,word.num_of_all);
//			}
//		}
//	}
//	public static ArrayList<WordObject> getKeyWord(ArrayList<String> listWord){		
//		ArrayList<WordObject> keyWords = new ArrayList<WordObject>();
//		ArrayList<String> words = new ArrayList<String>();
//		ArrayList<Integer> frequents = new ArrayList<Integer>();
//		
//		for (String word : listWord) {
//			if(!words.contains(word))
//			{
//				words.add(word);
//				frequents.add(1);
//			}	
//			else
//			{
//				int index = words.indexOf(word);
//				frequents.set(index, frequents.get(index)+1);
//				
//			}
//		}	
//		for(int i = 0;i<words.size();i++){
//			keyWords.add(new WordObject(words.get(i), 1, 1, frequents.get(i)));
//		}
//		return keyWords;		
//	}	
//	public static void learnHamWord(ArrayList<WordObject> listWord) {
//		for (WordObject word : listWord) {
//			WordObject wordTemp = null;
//			if ((wordTemp = getWordFromDatabase(WORD_HAM_TABLE, word.word)) != null) {
//				wordTemp.num_of_mail++;
//				wordTemp.num_of_all+=word.num_of_all;
//				updateDatabase(WORD_HAM_TABLE, wordTemp.word,
//						wordTemp.num_of_mail, wordTemp.p,wordTemp.num_of_all);
//			} else if ((wordTemp = getWordFromDatabase(
//					WORD_HAM_TABLE_TEMP, word.word)) != null) {
//				if (++wordTemp.num_of_mail > 0.25 * numOfSpamMail) {
//					insertDatabase(WORD_HAM_TABLE, wordTemp.word,
//							wordTemp.num_of_mail, wordTemp.p,wordTemp.num_of_all+word.num_of_all);
//				} else {
//					updateDatabase(WORD_HAM_TABLE_TEMP, wordTemp.word,
//							wordTemp.num_of_mail, wordTemp.p,wordTemp.num_of_all+word.num_of_all);
//				}
//			} else {
//				insertDatabase(WORD_HAM_TABLE_TEMP, word.word, 1, 0,word.num_of_all);
//			}
//		}
//	}
//	public static ArrayList<String>processMail(String filePath){
//		ArrayList<String> listWord = new ArrayList<String>();
//		PTBTokenizer<CoreLabel> ptbt;
//		ptbt = new PTBTokenizer<>(new StringReader(
//				preprocessMail(filePath)),
//				new CoreLabelTokenFactory(), "");
//		while (ptbt.hasNext()) {
//			CoreLabel label = ptbt.next();
//			String word = label.toString().toLowerCase();
//			if (word.startsWith("http"))
//				word = "http";
//			if(word.length()>45)
//				continue;
//			listWord.add(word);
//		}
//		return listWord;
//	}
//}
