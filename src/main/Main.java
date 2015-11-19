package main;

import huanpc.learning.eng.Constant;
import huanpc.learning.eng.LearningWord;

import java.io.File;

import nbAlgorithm.NBEngMail;
import nbAlgorithm.NBVieMail;

public class Main {
	// Nếu dùng bộ dữ liệu test mà mail có chứa header thì cần bật cờ này
	public static  boolean NEED_REMOVE_HEADER = false;
	// Chỉ định bộ dữ liệu test cần dùng
	public static String data_test[]=Constant.DATA_TEST_HTML_5;
	// Chỉ định bộ dữ liệu học cần dùng
	public static String data_learn[]=Constant.DATA_LEARN_HTML_3;
	// Chỉ định bảng dữ liệu trong database sẽ dùng
	public static String TABLE = Constant.WORD_TABLE_5;
	public static  String f = String.valueOf(0);
	// Điều kiện lọc tập học
//	public static final String condition = " spam_mail>"+f+" and ham_mail>"+f+" and spam_mail <> ham_mail";
//	public static final String condition = " (ham_frequent > 6 and spam_frequent < 6 ) or (ham_frequent <6 and spam_frequent>6)";
	public static String condition = " spam_frequent>"+f+" or ham_frequent>"+f+" and spam_frequent <> ham_frequent";
	
	// Chạy chương trình
	public static void main(String[] args){		
//		 Chạy quá trình học bằng hàm này
//		LearningWord learn = new LearningWord(false,Constant.WORD_TABLE_5,Constant.DATA_LEARN_HTML_5);
//		learn.learningEngMail();
		
		// Chạy quá trình test bằng hàm này
//		System.out.println("Bat dau test");
		NBEngMail nb = new NBEngMail(TABLE,"1",false);
		nb.mailClassify(data_test[0], data_test[1]);	
		
//		int j = 0;
//		int maxIndex1 [] ={15,6,15};
//		int maxIndex2 [] ={5,8,8}; 
//		int maxIndex3 [] ={10,17,6}; 
//		String test [][] = {Constant.DATA_TEST_NO_HTML_1,Constant.DATA_TEST_NO_HTML_2,Constant.DATA_TEST_HTML_3};
//		boolean flag [] = {false,false,true};
//		String table [] = {Constant.WORD_TABLE_1,Constant.WORD_TABLE_2,Constant.WORD_TABLE_3};
//		for(int i=0; i<3;i++){
//			f = String.valueOf(maxIndex1[i]);			
////			condition = " spam_frequent>"+f+" or ham_frequent>"+f+" and spam_frequent <> ham_frequent";
////			condition = " (ham_frequent > "+f+" and spam_frequent < "+f+" ) or (ham_frequent <"+f+" and spam_frequent>"+f+")";
//			condition = "1";
////			condition = " (ham_mail > "+f+" and spam_mail < "+f+" ) or (ham_mail <"+f+" and spam_mail>"+f+")";
////			condition = " spam_mail>"+f+" and ham_mail>"+f+" and spam_mail <> ham_mail";
//			System.out.println("Tap :"+i);
//			NEED_REMOVE_HEADER = flag[i];
//			TABLE = table[i];
//			NBEngMail nb = new NBEngMail(table[i],condition,flag[i]);
//			nb.mailClassify(test[i][0], test[i][1]);			
//		}		
//		System.out.println("Done!");
		
//		NBVieMail nb = new NBVieMail(Constant.WORD_TABLE_VI, " spam_frequent>"+1+" or ham_frequent>"+1, false);
//		nb.mailClassify(Constant.DATA_TEST_VI[0], Constant.DATA_TEST_VI[1]);
	}
}

