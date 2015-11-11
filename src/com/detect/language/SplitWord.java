/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detect.language;

import com.sun.org.apache.xalan.internal.lib.ExsltStrings;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.sound.midi.Soundbank;
import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author Telosma
 */
public class SplitWord {
//    String text = "Tao la Tung tao bị đao tao thích ăn dao";
//   // String[] a = new String[]{};
//    String[] a = text.split(" ");
//    System.out.print(a);
//    public static String text = "Do đó, nợ công tăng rất nhanh, bình quân 5 năm khoảng 20%/năm, từ khoảng 1,3 triệu tỷ đồng năm 2011 lên dự kiến 2,7 triệu tỷ đồng năm 2015.\n" +
//"\n" +
//"Trong khi đó, từ năm 2013 trở lại đây, Việt Nam không cân đối được đủ nguồn để trả lãi nợ gốc các khoản vay của Chính phủ đến hạn phải trả và phải vay nợ mới để trả nợ cũ, giá trị năm sau cao hơn năm trước. Nếu năm 2013, lần đầu tiên phải vay để đảo nợ với mức 40.000 tỷ đồng thì năm 2014 là 77.000 tỷ đồng, năm 2015 khoảng 125.000 tỷ đồng.\n" +
//"\n" +
//"“Khó có thể nói mọi việc đều suôn sẻ khi nợ đến hạn chúng ta không trả được mà phải cơ cấu lại nợ theo hướng kéo dài thời gian”, ông Văn nhận xét.";
    public String[] parseToArray(String myText){
       // myText.replaceAll(","," ");
        StringTokenizer st = new StringTokenizer( myText.replace("/", " ").replace(".", " ").replaceAll(","," ")," ");
        String[] resultString = new String[st.countTokens()];
        int i = 0;
        while(st.hasMoreTokens()&&i<=25){
            resultString[i] = st.nextToken();
            i++;
        }
//        for(int j = 0; j<st.countTokens();j++){
//            System.out.println(j+" = "+resultString[j]);
//        }
        return resultString;
    }
    public void display(String text){
        String[] resultArray =  parseToArray(text);
        System.out.println(Arrays.toString(resultArray));
        
    }
//    public static void main(String[] args){
//        LanguegeIdentify l = new LanguegeIdentify();
//        String[] result =  l.parseToArray(text);
//        System.out.println(Arrays.toString(result));
//    }
//    
    
    
    
    
}
