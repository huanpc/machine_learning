package Learning_Eng;

public class WordObject {
	public String word = "";
	public int spam_mail = 0;
	public int ham_mail = 0;
	public int spam_frequent = 0;
	public int ham_frequent = 0;
	public float p_spam = 0;
	public float p_ham = 0;
//	public int num_of_mail = 0;
//	public int num_of_all = 0;
//	public float p = 0;
	
	
//	public WordObject(String mWord, int mNumOfMail, float mP,int mNumOfAll) {
//		this.word = mWord;
//		this.num_of_all = mNumOfAll;
//		this.num_of_mail = mNumOfMail;
//		this.p = mP;
//	}
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