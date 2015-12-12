package nbAlgorithm;

import huanpc.learning.eng.Constant;
import huanpc.learning.eng.KeyWords;
import huanpc.learning.eng.LearningWord;
import huanpc.learning.eng.WordObject;
import huanpc.learning.eng.XMLparse;

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
 *
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
    static boolean NEAD_REMOVED_HEADER = false;
    static String table = main.Main.TABLE;
    static String condition = "";
    double sumSpamFre = 0;
    double sumHamFre = 0;
    static double pSpamClassify = 0;
    static double pHamClassify = 0;
    
    protected static KeyWords keyWords = null;

    /**
     *
     * @param table
     * @param condition
     * @param flag
     */
    public NBEngMail(String table, String condition, boolean flag) {
        this.table = table;
        this.condition = condition;
        this.NEAD_REMOVED_HEADER = flag;
        sumSpamFre = getSumSpamFre();
        sumHamFre = getSumHamFre();
    }

    /**
     * Hàm phân loại
     *
     * @param spamDir : thư mục spam mail
     * @param hamDir : thư mục ham mail
     */
    public double[] mailClassifyTestv1(String spamDir, String hamDir) {
        LearningWord learningWord = new LearningWord(NEAD_REMOVED_HEADER, table, new String[]{spamDir, hamDir});
        String condition = this.condition;
        keyWords = learningWord.getKeyWordsFromDB(table, condition);
        // test spam
        File dir = new File(spamDir);
        File[] subFile = dir.listFiles();
        int size = subFile.length;
        double numSpam = 0;
        int l = 0;
        for (int j = l; j < size; j++) {
            if (nbEng2(LearningWord.processMail((subFile[j]).getAbsolutePath())) == true) {
                numSpam++;
            }
            System.out.println(numSpam);
        }
        double a = numSpam / (size - l);
        // test ham
        dir = new File(hamDir);
        subFile = dir.listFiles();
        double numHam = 0;
        size = subFile.length;               
        for (int j = l; j < size; j++) {
        	if (nbEng2(LearningWord.processMail((subFile[j]).getAbsolutePath())) == false) {
                numHam++;
            }
            System.out.println(numHam);
        }
        double b = numHam / (size - l);
        System.out.println("\n Spam:" + String.valueOf(a) + " Ham:"
                + String.valueOf(b) + "Num of key words: " + keyWords.getLenght());
        double rs[];
        rs = new double[2];
        rs[0] = a;
        rs[1] = b;
        return rs;

    }
    /**
     * Test voi tap spam
     * @param spamDir
     * @return
     */
    public double mailClassifyTestSpam(String spamDir) {
        LearningWord learningWord = new LearningWord(NEAD_REMOVED_HEADER, table, new String[]{spamDir, ""});
        String condition = this.condition;
        keyWords = learningWord.getKeyWordsFromDB(table, condition);
        int i = 0;
        // test spam
        File dir = new File(spamDir);
        File[] subFile = dir.listFiles();
        int size = subFile.length;
        double numSpam = 0;
        int l = 0;
        for (int j = l; j < size; j++) {
            if (nbEng2(LearningWord.processMail((subFile[j]).getAbsolutePath())) == true) {
                numSpam++;
            }
        }
        double a = numSpam / (size - l);        
        return a;

    }
    /**
     * TEst voi tap ham
     * @param hamDir
     * @return
     */
    public double mailClassifyTestHam(String hamDir) {
        LearningWord learningWord = new LearningWord(NEAD_REMOVED_HEADER, table, new String[]{"", hamDir});
        String condition = this.condition;
        keyWords = learningWord.getKeyWordsFromDB(table, condition);
        int i = 0;        
        int l = 0;
        // test ham
        File dir = new File(hamDir);
        File [] subFile = dir.listFiles();
        double numHam = 0;
        double size = subFile.length;
        for (int j = l; j < size; j++) {
        	if (nbEng2(LearningWord.processMail((subFile[j]).getAbsolutePath())) == false) {
                numHam++;
            }
        }
        double b = numHam / (size - l);
        return b;

    }
    /**
     * Phan loai voi 1 mail
     * @param filePath
     * @return
     */
    public boolean mailClassify(String filePath) {
        LearningWord learningWord = new LearningWord(NEAD_REMOVED_HEADER, table, new String[]{});
        String condition = this.condition;
        keyWords = learningWord.getKeyWordsFromDB(table, condition);        
        File dir = new File(filePath);
        return (nbEng2(LearningWord.processMail((dir.getAbsolutePath()))));
    }
    /**
     * Hàm gọi thuật toán phân loại văn bản theo bayes
     *
     * @param listWord
     * @return
     */
    public boolean nbEng2(ArrayList<String> listWord) {
        WordObject ob;
        double spam = 1;
        double ham = 1;
        double spamFre = 0.0;
        double hamFre = 0.0;
        for (int i = 0; i < listWord.size(); i++) {        	
            double pSpam = 0, pHam = 0;
            String wO = listWord.get(i);
//            System.out.print(wO+" ");
            ob = keyWords.searchKeyWord(wO);
            if (ob != null) {
                spamFre = ob.spam_frequent;
                hamFre = ob.ham_frequent;                
                if (spamFre == 0) {
                	pSpam = 1 / (double)(sumSpamFre); 
                    pHam = (hamFre) / (sumHamFre);
                } else if (hamFre == 0) {
                    pSpam = (spamFre) / (sumSpamFre);
                    pHam = 1 / (double)(sumHamFre);
                } else {
                    pSpam = (spamFre) / (sumSpamFre);
                    pHam = (hamFre) / (sumHamFre);
                }
                spam = spam + Math.log(pSpam);
                ham = ham + Math.log(pHam);
            } 
            else {
                pHam = 1 / (double) (sumHamFre);
                pSpam = 1 / (double)(sumSpamFre);
                spam = spam + Math.log(pSpam);
                ham = ham + Math.log(pHam);
            }
        }
        Double s = new Double(spam);
        Double h = new Double(ham);
        pSpamClassify = s;
        pHamClassify = h;
        System.out.println("xac suat hau nghiem spam: " + s.toString());
        System.out.println("xac suat hau nghiem ham: " + h.toString());
        n++;
        if (s.compareTo(h) > 0) {
            System.out.println("thu la spam");
            return true;
        } else {
        	if (s.compareTo(h) == 0){
        	}
            System.out.println("thu khong phai la spam ");
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
     *
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

}
