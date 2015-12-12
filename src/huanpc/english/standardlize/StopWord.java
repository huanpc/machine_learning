package huanpc.english.standardlize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class StopWord {
	// Document StopWords: http://www.ranks.nl/stopwords
	private String[] ArrStopWord_one = {"a","about","above","after","again","against","all","am","an","and","any","are","aren't","as","at",
			"be","because","been","before","being","below","between","both","but","by",
			"can't","cannot","could","couldn't",
			"did","didn't","do","does","doesn't","doing","don't","down","during",
			"each",
			"few","for","from","further",
			"had","hadn't","has","hasn't","have","haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","him","himself","his","how","how's",
			"i","i'd","i'll","i'm","i've","if","in","into","is","isn't","it","it's","its","itself",
			"let's",
			"me","more","most","mustn't","my","myself",
			"no","nor","not",
			"of","off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own",
			"same","shan't","she","she'd","she'll","she's","should","shouldn't","so","some","such",
			"than","that","that's","the","their","theirs","them","themselves","then","there","there's","these","they","they'd","they'll","they're","they've","this","those","through","to","too",
			"under","until","up",
			"very",
			"was","wasn't","we","we'd","we'll","we're","we've","were","weren't","what","what's","when","when's","where","where's","which","while","who","who's","whom","why","why's","with","won't","would","wouldn't",
			"you","you'd","you'll","you're","you've","your","yours","yourself","yourselves"};
	private String[] ArrStopWord_two = {
			"a","a's","able","about","above","according","accordingly","across","actually","after","afterwards",
			"again","against","ain't","all","allow","allows","almost","alone","along","already","also","although", 
			"always","am","among","amongst","an","and","another","any","anybody","anyhow","anyone","anything","anyway",
			"anyways","anywhere","apart","appear","appreciate","appropriate","are","aren't","around","as","aside","ask",
			"asking","associated","at","available","away","awfully", 	
			"b","be","became","because","become","becomes","becoming","been","before","beforehand","behind","being",
			"believe","below","beside","besides","best","better","between","beyond","both","brief","but","by", 	
			"c","c'mon","c's","came","can","can't","cannot","cant","cause","causes","certain","certainly","changes",
			"clearly","co","com","come","comes","concerning","consequently","consider","considering","contain",
			"containing","contains","corresponding","could","couldn't","course","currently",
			"d","definitely","described","despite","did","didn't","different","do","does","doesn't","doing",
			"don't","done","down","downwards","during",
			"e","each","edu","eg","eight","either","else","elsewhere","enough","entirely","especially",
			"et","etc","even","ever","every","everybody","everyone","everything","everywhere","ex",
			"exactly","example","except",
			"f","far","few","fifth","first","five","followed","following","follows","for","former","formerly","forth",
			"four" ,"from","further","furthermore", 	
			"g","get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings", 
			"h","had","hadn't","happens","hardly","has","hasn't","have","haven't","having","he","he's",
			"hello","help","hence","her","here","here's","hereafter","hereby","herein","hereupon",
			"hers","herself","hi","him","himself","his","hither","hopefully","how","howbeit","however", 	
			"i","i'd","i'll","i'm","i've","ie","if","ignored","immediate","in","inasmuch","inc","indeed",
			"indicate","indicated","indicates","inner","insofar","instead","into","inward","is","isn't",
			"it","it'd","it'll","it's","its","itself", 	
			"j","just", 	
			"k","keep","keeps","kept","know","known","knows",
			"l","last","lately","later","latter","latterly","least","less","lest","let","let's","like","liked",
			"likely","little","look","looking","looks","ltd",
			"m","mainly","many","may","maybe","me","mean","meanwhile","merely","might",
			"more","moreover","most","mostly","much","must","my","myself",
			"n","name","namely","nd","near","nearly","necessary","need","needs","neither","never","nevertheless","new",
			"next","nine","no","nobody","non","none","noone","nor","normally","not","nothing","novel","now","nowhere",
			"o","obviously","of","off","often","oh","ok","okay","old","on","once","one",
			"ones","only","onto","or","other","others","otherwise","ought","our","ours",
			"ourselves","out","outside","over","overall","own",
			"p","particular","particularly","per","perhaps","placed","please","plus","possible","presumably",
			"probably","provides",
			"q","que","quite","qv",
			"r","rather","rd","re","really","reasonably","regarding","regardless","regards","relatively","respectively",
			"right",
			"s","said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed",
			"seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall",
			"she","should","shouldn't","since","six","so","some","somebody","somehow","someone","something","sometime",
			"sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such",
			"sup","sure",
			"t","t's","take","taken","tell","tends","th","than","thank","thanks","thanx","that","that's","thats","the",
			"their","theirs","them","themselves","then","thence","there","there's","thereafter","thereby","therefore",
			"therein","theres","thereupon","these","they","they'd","they'll","they're","they've","think","third","this",
			"thorough","thoroughly","those","though","three","through","throughout","thru","thus","to","together","too",
			"took","toward","towards","tried","tries","truly","try","trying","twice","two", 	
			"u","un","under","unfortunately","unless","unlikely","until","unto","up","upon","us","use","used","useful","uses","using","usually",
			"v","value","various","very","via","viz","vs",
			"w","want","wants","was","wasn't","way","we","we'd","we'll","we're","we've","welcome","well","went","were",
			"weren't","what","what's","whatever","when","whence","whenever","where","where's","whereafter",
			"whereas","whereby","wherein","whereupon","wherever","whether","which","while","whither","who",
			"who's","whoever","whole","whom","whose","why","will","willing","wish","with","within","without",
			"won't","wonder","would","wouldn't", 	
			"y","yes","yet","you","you'd","you'll","you're","you've","your","yours","yourself","yourselves",
			"z","zero"};
	private HashMap<String ,Integer> map;
	public StopWord(){
		map = new HashMap<String,Integer>();
		createHashMap();
	}
	/**
	 * Tao bang bam cho StopWord
	 * 
	 */
	private void createHashMap(){
		for(int i = 0;i < ArrStopWord_one.length;i++){
			map.put(ArrStopWord_one[i], i);
		}
		for(int i = 0;i < ArrStopWord_two.length;i++){
			map.put(ArrStopWord_two[i], i);
		}
	}
	/**
	 * Ham tra ve mang tu stopwords duoc su dung
	 * @return Tra ve kieu bam bam
	 * 
	 */
//	public HashMap<String, Integer> getStopWords(){
//		return map;
//	}
	/** 
	 *  @param text : Doan van ban can phan tich
	 *  @param isStopWord : Dau ra mong muon cua phuong thuc, True la cac tu stopwords ,False la cac tu khong phai stopwords
	 *  @return tra ve danh sach cac tu
	 * 
	 */
	public ArrayList<String> letterStopWords(String text,boolean isStopWord){
		ArrayList<String> arr = new ArrayList<>();
		text = text.toLowerCase();
		text = text.trim();
		text = text.replaceAll("\b", " ");
		text = text.replaceAll("\t", " ");
		text = text.replaceAll("\n", " ");
		text = text.replaceAll("\f", " ");
		text = text.replaceAll("\r", " ");
		text = text.replaceAll("\\.", " ");
		text = text.replaceAll(",", " ");
		text = text.replaceAll("\\?", " ");
		text = text.replaceAll("!", " ");
		text = text.replaceAll(":", " ");
		text = text.replaceAll(";", " ");
		text = text.replaceAll("\"", " ");
		text = text.replaceAll("\\{", " ");
		text = text.replaceAll("\\}", " ");
		text = text.replaceAll("\\[", " ");
		text = text.replaceAll("\\]", " ");
		text = text.replaceAll("\\(", " ");
		text = text.replaceAll("\\)", " ");
		String [] letter = text.split(" ");
		for(int i=0;i<letter.length;i++){
			if(letter[i].equalsIgnoreCase(" ")||letter[i].equalsIgnoreCase("")){
				continue;
			}
			if(isStopWord){
				if(map.get(letter[i]) == null){
					continue;
				}
			}else{
				if(map.get(letter[i]) != null){
					continue;
				}
			}
			try{
				Integer.parseInt(letter[i]);
				continue;
			}catch (NumberFormatException e){
			}
			arr.add(letter[i]);
		}
		return arr;
	}
//	public static void main(String []args){
//		 StopWord tw = new StopWord();
//		 String text = "The last two options allow you to code some or all of your game using those script languages instead of the native C++; and they work just as you would expect a scripting language to work, meaning the commands written in either Javascript or Lua are actually replaced and interpreted as C++ commands by the compiler.\n"+
//				 		"Now if you look at the files,. created by the basic template you will see a HelloWorldScene class file. That's the reason I didn't want you to call your application HelloWorld, because I didn't want you to have the impression that the file name was based on your project name. It isn't. You will always get a HelloWorldScene file unless you change the template itself.";
//		 ArrayList<String> t = tw.letterStopWords(text, false);
//		 for (int i = 0; i < t.size(); i++) {
//			System.err.println("|"+t.get(i)+"|");
//		}
//	}
}
