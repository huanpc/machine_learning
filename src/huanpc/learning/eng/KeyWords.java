package huanpc.learning.eng;
/**
 * 
 * @author huanpc
 *	Từ khóa lấy ra từ database
 */
public class KeyWords {
	protected String[] word;
	protected int[] spam_mail;
	protected int[] ham_mail;
	protected int[] spam_frequent;
	protected int[] ham_frequent;
	protected int lenght = 0;

	public KeyWords(int lenght) {
		word = new String[lenght];
		spam_mail = new int[lenght];
		spam_frequent = new int[lenght];
		ham_mail = new int[lenght];
		ham_frequent = new int[lenght];
		this.lenght = lenght;
	}

	/**
	 * Lay ra mot tu trong tap tu khoa
	 * 
	 * @param wordSearch
	 * @return
	 */
	public WordObject searchKeyWord(String wordSearch) {
		for (int i = 0; i < word.length; i++) {
			if (word[i].equals(wordSearch))
				return new WordObject(wordSearch, spam_mail[i], ham_mail[i],
						spam_frequent[i], ham_frequent[i]);
		}
		return null;
	}

	/**
	 * Them mot tu khoa
	 * 
	 * @param index
	 * @param word
	 * @param spam_mail
	 * @param ham_mail
	 * @param spam_frequent
	 * @param ham_frequent
	 */
	public void setData(int index, String word, int spam_mail, int ham_mail,
			int spam_frequent, int ham_frequent) {
		this.word[index] = word;
		this.spam_mail[index] = spam_mail;
		this.ham_mail[index] = ham_mail;
		this.spam_frequent[index] = spam_frequent;
		this.ham_frequent[index] = ham_frequent;
	}

	/**
	 * Lay so phan tu cua mang
	 * 
	 * @return
	 */
	public int getLenght() {
		return this.lenght;
	}

}
