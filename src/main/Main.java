package main;

import huanpc.learning.eng.Constant;
import huanpc.learning.eng.LearningWord;

import java.io.File;

import nbAlgorithm.NBEngMail;
import nbAlgorithm.NBVietMail;

public class Main {
	// Nếu dùng bộ dữ liệu test mà mail có chứa header thì cần bật cờ này
	public static  boolean NEED_REMOVE_HEADER = false;
	// Chỉ định bộ dữ liệu test cần dùng
	public static String data_test_eng[]=Constant.DATA_TEST_NO_HTML_2;
	public static String data_test_vie[]=Constant.DATA_TEST_VI;
	// Chỉ định bộ dữ liệu học cần dùng
	public static String data_learn[]=Constant.DATA_LEARN_NO_HTML_2;
	// Chỉ định bảng dữ liệu trong database sẽ dùng
	public static String TABLE = Constant.WORD_TABLE_2;
	// Điều kiện lọc tập học
	//Tieng Anh
	public static String condition_eng = " spam_frequent>"+3+" or ham_frequent>"+0;
	//Tieng Viet
	public static String condition_vi = " spam_frequent>"+2+" or ham_frequent>"+0;
	// Chạy chương trình
	public static void main(String[] args){
		
//		 Chạy quá trình học bằng hàm này
//		LearningWord learn = new LearningWord(false,Constant.WORD_TABLE_5,Constant.DATA_LEARN_HTML_5);
//		learn.learningEngMail();
		
		//Chay qua trinh test Mail Eng bang ham nay
		NBEngMail nbE = new NBEngMail(TABLE, condition_eng, NEED_REMOVE_HEADER);
		nbE.mailClassifyTestv1(data_test_eng[0], data_test_eng[1]);
//		nbE.mailClassify(data_test[1]+"/0855.2000-03-30.kaminski.ham.txt");

		//Chay qua trinh test Mail Vie bang ham nay
//		NBVietMail nbV = new NBVietMail(TABLE, condition_vi, NEED_REMOVE_HEADER);
//		nbV.mailClassifyTestv1(data_test_vie[0], data_test_vie[1]);
//		nbV.mailClassify(data_test[1]+"/1.txt");
	}
}

