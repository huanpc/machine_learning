package Tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

public class Tokenizer {
	public static final String WORD_FREQUENT_TABLE = "word_frequent";
	public static final String WORD_FREQUENT_TABLE_TEMP = "word_frequent_temp";
	// Folder chua mail can xu ly (spam+ not spam)
	static String filePath = "./Code Project/Mail datasets/English/testSpam";
	static Connection connection = null;
	// so mail trong folder
	static int numOfMail = 20;

	public static void main(String[] args) {
		// hoc tu
		learningEng();
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
				if (line.equals("")) {
					meetBodyLine = true;
					continue;
				}
				if (meetBodyLine == true) {
					lines.append(" " + line);
				}
			}
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
				wordObject = new WordObject(word, rs.getInt(3), rs.getFloat(4));
				// System.out.println(rs.getString(2) + " " + rs.getInt(3) + " "
				// + rs.getFloat(4));
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
	 * Them mot tu vao database
	 * 
	 * @param which
	 *            : table name
	 * @param word
	 *            : tu can them
	 * @param frequent
	 *            : tan suat tu
	 * @param p
	 *            : xac suat xuat hien cua tu
	 * @return
	 */
	public static boolean insertDatabase(String which, String word,
			int frequent, float p) {
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
					+ which + " values(?,?,?,?)");
			stmt.setInt(1, ++id);
			stmt.setString(2, word);
			stmt.setInt(3, frequent);
			stmt.setFloat(4, p);
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
	 * Cap nhat database
	 * 
	 * @param table
	 * @param word
	 * @param frequent
	 * @param p
	 * @return
	 */
	public static boolean updateDatabase(String table, String word,
			int frequent, float p) {
		Connection con = getConnectDatabase();
		try {
			PreparedStatement stmt = (PreparedStatement) con
					.prepareStatement("UPDATE " + table
							+ " set frequent=? ,p=? where word=?");
			// stmt.setString(1, table);
			stmt.setInt(1, frequent);
			stmt.setFloat(2, p);
			stmt.setString(3, word);
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
	 * 
	 */
	public static void learningEng() {
		System.out.println("Begin process mail!");
		Long time = System.currentTimeMillis();
		File mailDir = new File(filePath);
		File[] listSpamFile = mailDir.listFiles();
		numOfMail = listSpamFile.length;
		PTBTokenizer<CoreLabel> ptbt;
		ArrayList<String> listWord = new ArrayList<String>();
		for (File file : listSpamFile) {
			ptbt = new PTBTokenizer<>(new StringReader(
					preprocessMail(file.getAbsolutePath())),
					new CoreLabelTokenFactory(), "");
			while (ptbt.hasNext()) {
				CoreLabel label = ptbt.next();
				String word = label.toString().toLowerCase();
				if (word.startsWith("http"))
					word = "http";
				listWord.add(word);
			}
			leanrWord(listWord);
			listWord.clear();
		}
		System.out.println("DONE!!!");
		System.out.println("Number of email: " + numOfMail + " Time: "
				+ (System.currentTimeMillis() - time) / 1000);

	}

	/**
	 * Hoc tu
	 * 
	 * @param listWord
	 *            : danh sach tu can hoc
	 */
	public static void leanrWord(ArrayList<String> listWord) {
		for (String word : listWord) {
			WordObject wordTemp = null;
			if ((wordTemp = getWordFromDatabase(WORD_FREQUENT_TABLE, word)) != null) {
				wordTemp.frequent++;
				updateDatabase(WORD_FREQUENT_TABLE, wordTemp.word,
						wordTemp.frequent, wordTemp.p);
			} else if ((wordTemp = getWordFromDatabase(
					WORD_FREQUENT_TABLE_TEMP, word)) != null) {
				if (++wordTemp.frequent > 0.25 * numOfMail) {
					insertDatabase(WORD_FREQUENT_TABLE, wordTemp.word,
							wordTemp.frequent, wordTemp.p);
				} else {
					updateDatabase(WORD_FREQUENT_TABLE_TEMP, word,
							wordTemp.frequent, wordTemp.p);
				}
			} else {
				insertDatabase(WORD_FREQUENT_TABLE_TEMP, word, 1, 0);
			}
		}
	}
}
