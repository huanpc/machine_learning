
/**
 * 
 */
package abc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 *
 * @author Administrator
 */
public class MachineLearningVietNamese {
    public static boolean NEED_REMOVE_HEADER = false;
    public String inputDir = "inputMail";// tập mail thường
    public String inputDirSpam = "inputMailSpam";// tập mail spam
    public String outputDir = "outputMail";
    public String outputDirSpam = "outputMailSpam";
    public String inputStopWords = "vnstopword.txt";// tập từ dừng
    public File tmpFile = new File("default.txt");
    public static String ClassName = "com.mysql.jdbc.Driver";
    public static String Url = "jdbc:mysql://localhost:3306/machine_learning?characterEncoding=utf8";
    public static String User = "root";
    public static String Pass = "";
    public static String tableName = "learning_vietnamese_words";
    public static Connection conn;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            Class.forName(ClassName);
            conn = DriverManager.getConnection(Url,User, Pass);
            System.out.println("Kết nối thành công!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Không tìm thấy class!");
        } catch (SQLException ex) {
            System.out.println("Lỗi kết nối!");
        }
        MachineLearningVietNamese result = new MachineLearningVietNamese();
        
//        result.makeLearningDataset();

        File f = new File("C:\\Users\\duylx\\Documents\\NetBeansProjects\\MachineLearning\\Mail datasets\\VietNamese\\test_HAM\\2.txt");
        String[] outputString = result.getStringFromFile(f);
        for(int i = 0;i<outputString.length;i++){
            System.out.println(i+" = "+outputString[i]);
        }
    }
	//đầu vào: File() đầu ra: mảng String
    public String[] getStringFromFile(File f) throws FileNotFoundException{
        File fStop = new File(this.inputStopWords);
        VietTokenizer tokenizer = new VietTokenizer();
        tokenizer.tokenize(f.getAbsolutePath(),this.tmpFile.getAbsolutePath());
        String[] listString = new String[1];
            listString[0] = getStringMail(this.tmpFile.getAbsolutePath());
        String[] listWords = getListWords(listString);
        String[] listStopWords = getListStopWords(fStop);
        String[] resultString = getKeyWords(listWords, listString, listStopWords);
        return resultString;
    }
	/**
	* tao bo hoc tieng viet
	**/
    public void makeLearningDataset() throws FileNotFoundException, IOException{
        
        
        VietTokenizer avc = new VietTokenizer();        
        
        avc.tokenizeDirectory(this.inputDirSpam, this.outputDirSpam);
        avc.tokenizeDirectory(this.inputDir, this.outputDir);
        
	this.preprocessMail(this.inputDir);
        this.preprocessMail(this.inputDirSpam);
		
        File f = new File(this.outputDir);
        File fSpam = new File(this.outputDirSpam);
        File fStop = new File(this.inputStopWords);
        String[] listStopWords = getListStopWords(fStop);
        String[] listString = getListString(f);//return list string from listfile
        String[] listStringSpam = getListString(fSpam);//return listString form listSpamFile
        String[] listStringTotal = appendString(listString,listStringSpam);
        String[] listWords = getListWords(listString);//return listwords
        String[] listWordsSpam = getListWords(listStringSpam);
        String[] listWordsTotal = appendString(listWords, listWordsSpam);
        String[] keyWords = getKeyWords(listWordsTotal,listStringTotal,listStopWords);//return list keywords of total mail and spammail
        int[] numberOfMail = getNumberOfMail(listString, keyWords);
        int[] numberOfFrequent = getNumberOfFrequent(listString, keyWords);
        int[] numberOfMailSpam = getNumberOfMail(listStringSpam, keyWords);
        int[] numberOfFrequentSpam = getNumberOfFrequent(listStringSpam, keyWords);
        for(int j = 0; j<keyWords.length;j++){
            System.out.println(keyWords[j]+"||"+numberOfMail[j]+"||"+numberOfFrequent[j]+"||"+numberOfMailSpam[j]+"||"+numberOfFrequentSpam[j]);
        }
        float[] pHam = new float[keyWords.length];
        float[] pSpam = new float[keyWords.length];
//        insertRecord(tableName, keyWords, numberOfMail, numberOfFrequent, numberOfMailSpam, numberOfFrequentSpam, pHam, pSpam);
    }
    public static void insertRecord(String tableName,String[] keyWords, int[] numberOfMail,int[] numberOfFrequent,
                int[] numberOfMailSpam,int[] numberOfFrequentSpam,
                float[] pHam, float[] pSpam){
        for(int i =  0; i<keyWords.length;i++){
            String query = "INSERT INTO "+tableName+" (`word`, `spam_mail`,`ham_mail`, `spam_frequent`,  "
               + "`ham_frequent`, `p_spam`, `p_ham`)"
               + " VALUES (?,?,?,?,?,?,?);";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1,keyWords[i]);
            pst.setInt(2,numberOfMailSpam[i]);
            pst.setInt(3,numberOfMail[i]);
            pst.setInt(4,numberOfFrequentSpam[i]);
            pst.setInt(5,numberOfFrequent[i]);
            pst.setFloat(6,pSpam[i]);
            pst.setFloat(7,pHam[i]);
            if (pst.executeUpdate() > 0) {
                System.out.println("Thêm thành công");
            }
            else {
                System.out.println("Lỗi khi thêm sản phẩm\n");
            }
                } catch (SQLException e) {
                    System.out.println("Lỗi \n" + e.toString());
            }
        }
    }
    public static String[] appendString(String[] str1, String[] str2){
        String[] resultString = new String[str1.length+str2.length];
        int i = 0;
        for(i = 0; i<str1.length;i++){
            resultString[i] = str1[i];
        }
        int j = i;
        for(j = i;j<(str2.length+i);j++){
            resultString[j] = str2[j-i];
        }
        return resultString;
    }
    public static int[] getNumberOfMail(String[] listString, String[] keyWords){
        int[] numberOfMail = new int[keyWords.length];
        int k = 0;
        for(int t = 0; t<keyWords.length;t++){
            for(int j = 0; j< listString.length; j++){
                StringTokenizer st = new StringTokenizer(listString[j]," ");
                k = 0;
                while(st.hasMoreTokens()){
                    String tmp = st.nextToken();
                    if(tmp.compareTo(keyWords[t]) == 0){
                        numberOfMail[t]++;
                        break;
                    }
                }
            }
        }
        return numberOfMail;
    }
    public static int[] getNumberOfFrequent(String[] listString, String[] keyWords){
        int[] numberOfFrequent = new int[keyWords.length];
        int k = 0;
        for(int j = 0; j< listString.length; j++){
            StringTokenizer st = new StringTokenizer(listString[j]," ");
            k = 0;
            while(st.hasMoreTokens()){
                String tmp = st.nextToken();
                for(int t = 0; t<keyWords.length;t++){
                    if(tmp.compareTo(keyWords[t]) == 0){
                        numberOfFrequent[t]++;
                    }
                }
            }
        }
        return numberOfFrequent;
    }
    public static String[] getKeyWords(String[] listWords, String[] listString, String[] listStopWords){
        String[] listKeyWords = new String[listWords.length];
        int keyWordsIndex = 0;
        int k = 0;
        for(int j = 0; j< listString.length; j++){
            StringTokenizer st = new StringTokenizer(listString[j]," ");
            k = 0;
            while(st.hasMoreTokens()){
                String tmp = st.nextToken();
                if((isKeyWords(tmp, listKeyWords) == true) && (isStopWords(tmp, listStopWords) == false) &&(tmp.length() >= 3)){
                    listKeyWords[keyWordsIndex] = tmp;
                    keyWordsIndex++;
                }
            }
        }
        String[] keyWords = new String[keyWordsIndex];
        for(int j = 0; j<listKeyWords.length;j++){
            if(listKeyWords[j] == null){
                break;
            }
            keyWords[j] = listKeyWords[j];
        }
        return keyWords;
    }
    public static String[] getListStopWords(File fStop) throws FileNotFoundException{
        String pre = getStringMail(fStop.getAbsolutePath());
        StringTokenizer st = new StringTokenizer(pre);
        String[] listStopWords = new String[st.countTokens()];
        int i = 0;
        while(st.hasMoreTokens()){
            listStopWords[i] = st.nextToken();
            i++;
        }
        return listStopWords;
    }
    public static String[] getListWords(String[] listString){
        int numberOfWord = 0;
        for(int j = 0; j< listString.length; j++){
            StringTokenizer st = new StringTokenizer(listString[j]," ");
            numberOfWord+=st.countTokens();
        }
        int k = 0;
        int l = 0;
        String[] listWords = new String[numberOfWord];
        for(int j = 0; j< listString.length; j++){
            StringTokenizer st = new StringTokenizer(listString[j]," ");
            k = 0;
            while(st.hasMoreTokens()){
                String tmp = st.nextToken();
                if(tmp.length() >=3 ){
                    listWords[l] = tmp;
                    l++;
                }
            }
        }
        return listWords;
    }
    public static String[] getListString(File f) throws FileNotFoundException{
        File[] subFile = f.listFiles();
        String[] listStrings = new String[f.listFiles().length];
        int i = 0;
        for(File file : subFile){
        	listStrings[i] = getStringMail(file.getAbsolutePath());
            i++;
        }
        return listStrings;
    }
    public static boolean isStopWords(String tmp, String[] listStopWords){
        for(int i = 0; i<listStopWords.length ; i++){
            if(tmp.compareTo(listStopWords[i]) == 0){//có từ giống trong liststopwords
                return true;
            }else{
                continue;
            }
        }
        return false;
    }
    public static boolean isKeyWords(String tmp, String[] listWords){
        for(int i = 0; i<listWords.length ; i++){
            if(listWords[i] == null){
                return true;
            }
            if(tmp.compareTo(listWords[i]) == 0){//đã tồn tại keywords trong mảng
                return false;
            }else{
                continue;
            }
        }
        return true;
    }
    /**
     * Tien xu ly mail dau vao
     * @param filePath
     * @return 
     */
    public void preprocessMail(String filePath) throws IOException {
        File rootFile = new File(filePath);
        File[] subFile = rootFile.listFiles();
        for(File file : subFile){
            Boolean meetBodyLine = false;
            StringBuilder lines = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String line;
                // remove mail header
                // mail header ngan cach voi body boi khoang trang
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
            String text = Jsoup.parse(lines.toString()).text().toLowerCase();
            text = text.replaceAll("[1234567890,.~!@#$%^&*\"\';:?/()+=/\\><_-]", "\n");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(text);
            }
        }
        
    }
    public static String getStringMail(String filePath) {
        Boolean meetBodyLine = false;
        StringBuilder lines = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // remove mail header
            // mail header ngan cach voi body boi khoang trang
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
        String text = Jsoup.parse(lines.toString()).text().toLowerCase();
        text = text.replaceAll("[1234567890,.~!@#$%^&*\"\';:?/()+=/\\><-]", " ");
        return text;
    }
}