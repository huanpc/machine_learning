package huanpc.english.standardlize;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
/**
 * Đọc thư viện các từ bất quy tắc 
 * @author huanpc
 *	
 */
public class Irregular {	
    public static ArrayList<String> nguyenThe = new ArrayList<String>();
    public static ArrayList<String> quaKhu1 = new ArrayList<String>();
    public static ArrayList<String> quaKhu2 = new ArrayList<String>();
    public Irregular(){
       readFile();
    }
    private void readFile(){
        try{
            FileReader fr = new FileReader("./IrregularVerb.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine())!=null){
                String nguyenthe = " ";
                String quakhu1 = " ";
                String quakhu2 = " ";
                String[] x = line.split(" ");
                try{
                nguyenthe = x[0];
                quakhu1 = x[1];
                quakhu2 = x[2];
                }catch(Exception e){
                    //in ra neu dong text nao co loi
                    System.out.println(line+" :::"+ x.length);
                }
                nguyenThe.add(nguyenthe);
                quaKhu1.add(quakhu1);
                quaKhu2.add(quakhu2);
            }            
            br.close();
            fr.close();
        }
        catch(Exception e){
            
        }
    }
}