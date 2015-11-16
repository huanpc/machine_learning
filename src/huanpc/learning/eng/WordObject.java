package huanpc.learning.eng;
/*
 * Định nghĩa 1 từ trong tập từ học được
 */
public class WordObject {
	public String word = "";
	public int spam_mail = 0;
	public int ham_mail = 0;
	public int spam_frequent = 0;
	public int ham_frequent = 0;
	public float p_spam = 0;
	public float p_ham = 0;

	public WordObject(String word, int spam_mail, int ham_mail, int spam_frequent, int ham_frequent){
		this.word = word;
		this.spam_mail = spam_mail;
		this.ham_mail = ham_mail;
		this.spam_frequent = spam_frequent;
		this.ham_frequent = ham_frequent;
	}
	public WordObject(String word, int spam_mail, int ham_mail, int spam_frequent, int ham_frequent, float p_spam, float p_ham){
		this.word = word;
		this.spam_mail = spam_mail;
		this.ham_mail = ham_mail;
		this.spam_frequent = spam_frequent;
		this.ham_frequent = ham_frequent;
		this.p_spam = p_spam;
		this.p_ham = p_ham;
	}	
}