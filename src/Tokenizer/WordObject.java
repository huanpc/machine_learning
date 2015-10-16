package Tokenizer;

public class WordObject {
	public String word = "";
	public int frequent = 0;
	public float p = 0;

	public WordObject(String mWord, int mFrequent, float mP) {
		this.word = mWord;
		this.frequent = mFrequent;
		this.p = mP;
	}
}