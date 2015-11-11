/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detect.language;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Telosma
 */
public class WordMatch {

    public WordMatch() {
    }

    public int getNumWordMatch(String text, Boolean flag) throws SQLException {    //flag == true : Tiếng Việt; flag == false : Tiếng Anh
        int count = 0;
        Conn conn = new Conn();
        SplitWord sw = new SplitWord();
        String[] words = sw.parseToArray(text);
        String tbname;
        String dbname = "machine_learning";
        if (flag == true) {
            tbname = "vietdic";
        } else {
            tbname = "learning_words";
        }
        for (int i = 0; i < words.length; i++) {
            ResultSet rstmp = conn.getWord(words[i],dbname, tbname);
            rstmp.last();
            int size = rstmp.getRow();
            if (size != 0) {
                count++;
            }
        }
        return count;
    }

    // Trả về true nếu ngôn ngữ đoạn text là tiếng Việt/ False nếu ngôn ngữ của đoạn text là tiếng Anh
    public boolean detectVN(String text) throws SQLException {
        boolean flag;
        if (this.getNumWordMatch(text, true) > this.getNumWordMatch(text, false)) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }
}
