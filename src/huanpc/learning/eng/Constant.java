package huanpc.learning.eng;
/**
 * 
 * @author huanpc
 * Khai báo các hằng số sử dụng trong chương trình
 */
public class Constant {
	
	public static final String DATASET_FOLDER = "./Code Project/Mail datasets/English";
	public static final String DATA_TEST_DIR = DATASET_FOLDER+"/test";
	public static final String DATA_LEARN_DIR = DATASET_FOLDER+"/learn";
	// Tap du lieu 1 k chua html 
	public static final String DATA_TEST_NO_HTML_1[] = {DATA_TEST_DIR+"/test_spam_noHTML_300",
			DATA_TEST_DIR+"/test_ham_noHTML_300"};
	public static final String DATA_LEARN_NO_HTML_1[] = {DATA_LEARN_DIR+"/learn_spam_noHTML_700_",
			DATA_LEARN_DIR+"/learn_ham_noHTML_700_"};
	public static final String WORD_TABLE_1 = "learning_words";
	// Tap du lieu 2 k chua html
	public static final String DATA_TEST_NO_HTML_2[] = {DATA_TEST_DIR+"/testSpam_noHTML_360",
		DATA_TEST_DIR+"/testHam_noHTML_360"};
	public static final String DATA_LEARN_NO_HTML_2[] = {DATA_LEARN_DIR+"/learnSpam_noHTML_840",
			DATA_LEARN_DIR+"/learnHam_noHTML_840"};
	public static final String WORD_TABLE_2 = "learning_words_2";
	// Tap du lieu 3 co html
	public static final String DATA_TEST_HTML_3[] = {DATA_TEST_DIR+"/testSpam_HTML",
			DATA_TEST_DIR+"/testHam_HTML"};
	public static final String DATA_LEARN_HTML_3[] = {DATA_LEARN_DIR+"/learnSpam_HTML",
			DATA_LEARN_DIR+"/learnHam_HTML"};
	public static final String WORD_TABLE_3 = "learning_words_3";
	// Tap du lieu 4 co html	
	public static final String DATA_TEST_HTML_4[] = {DATA_TEST_DIR+"/testSpam_HTML_2_360",
		DATA_TEST_DIR+"/testHam_HTML_2_360"};
	public static final String DATA_LEARN_HTML_4[] = {DATA_LEARN_DIR+"/learnSpam_HTML_2_840",
			DATA_LEARN_DIR+"/learnHam_HTML_2_840"};
	public static final String WORD_TABLE_4 = "learning_words_4";
	// Tập dữ liệu tiếng việt
	public static final String DATA_TEST_VI[] = {"./Code Project/Mail datasets/VietNamese/outputMailSpam",
		"./Code Project/Mail datasets/VietNamese/outputMail"};
	public static final String DATA_LEARN_VI[] = {"./Code Project/Mail datasets/VietNamese/outputMailSpam",
		"./Code Project/Mail datasets/VietNamese/outputMail"};
	public static final String WORD_TABLE_VI = "learning_words_vi";
	
	public static final String DATA_TEST_HTML_5[] = {DATASET_FOLDER+"/SpamMail/test_SPAM",
		DATASET_FOLDER+"/SpamMail/test_GEN"};
	public static final String DATA_LEARN_HTML_5[] = {DATASET_FOLDER+"/SpamMail/train_SPAM",
		DATASET_FOLDER+"/SpamMail/train_GEN"};
	public static final String WORD_TABLE_5 = "learning_words_5";
	
}
