package main;

import java.io.File;

import Learning_Eng.Constant;
import Learning_Eng.LearningWord;
import nbAlgorithm.NBEngMail;

public class Main {
	
	static String data_test[]=Constant.DATA_TEST_NO_HTML_1;
	
	public static void main(String[] args){
		LearningWord learningWord = new LearningWord();
		NBEngMail nbEng = new NBEngMail();
		// test spam		
		File dir = new File(data_test[0]);
		File[] subFile = dir.listFiles();
		int size = subFile.length;
		double numSpam = 0;		
		for(File f : subFile){	
			if(nbEng.nbEng2(learningWord.getTokens(learningWord.
					processMail(f.getAbsolutePath()), true)) == true)
				numSpam++ ;
		}
		double a = numSpam/size;		
		// test ham
		dir = new File(data_test[1]);		
		subFile = dir.listFiles();
		double numHam = 0;		
		size = subFile.length;
		for(File f : subFile){	
			if(nbEng.nbEng2(learningWord.getTokens(learningWord.
					processMail(f.getAbsolutePath()), true)) == false)
				numHam++ ;
		}
		double b = numHam/size;		
		System.out.println("\n Spam:"+String.valueOf(a)+" Ham:"+String.valueOf(b)+" Num of unclass: ");
	}
}

