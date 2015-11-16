package main;

import huanpc.learning.eng.Constant;
import huanpc.learning.eng.LearningWord;

import java.io.File;

import nbAlgorithm.NBEngMail;

public class Main {
	// Nếu dùng bộ dữ liệu test mà mail có chứa header thì cần bật cờ này
	public static final boolean NEED_REMOVE_HEADER = true;
	// Chỉ định bộ dữ liệu test cần dùng
	public static String data_test[]=Constant.DATA_TEST_HTML_3;
	// Chỉ định bộ dữ liệu học cần dùng
	public static String data_learn[]=Constant.DATA_LEARN_HTML_3;
	// Chỉ định bảng dữ liệu trong database sẽ dùng
	public static final String TABLE = Constant.WORD_TABLE_3;
//	public static final String f = String.valueOf(0.01*840);
	public static final String f = String.valueOf(0);
	// Điều kiện lọc tập học
	public static final String condition = " spam_mail>"+f+" and ham_mail>"+f+" and spam_mail <> ham_mail";
//	public static final String condition = " spam_frequent>"+NBEngMail.getAvgSpamFrequent()+" or ham_frequent>"+NBEngMail.getAvgHamFrequent();
//	public static final String condition = " 1";
	
	// Chạy chương trình
	public static void main(String[] args){		
		// Chạy quá trình học bằng hàm này
		LearningWord learn = new LearningWord();
		learn.learningEngMail();
		
		// Chạy quá trình test bằng hàm này
//		NBEngMail nb = new NBEngMail();
//		nb.mailClassify(data_test[0], data_test[1]);		
	}
}

