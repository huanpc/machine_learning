package Learning_Eng;

public class Constant {
	public static final String DATASET_FOLDER = "./Code Project/Mail datasets/English";
	public static final String DATA_TEST_DIR = DATASET_FOLDER+"/test";
	public static final String DATA_LEARN_DIR = DATASET_FOLDER+"/learn";
	
	public static final String DATA_TEST_NO_HTML_1[] = {DATA_TEST_DIR+"/test_spam_noHTML_300",
			DATA_TEST_DIR+"/test_ham_noHTML_300"};
	public static final String DATA_LEARN_NO_HTML_1[] = {DATA_LEARN_DIR+"/learn_spam_noHTML_700_",
			DATA_LEARN_DIR+"/learn_ham_noHTML_700_"};
	public static final String WORD_TABLE_1 = "learning_words";
	
	public static final String DATA_TEST_HTML_3[] = {DATA_TEST_DIR+"/testSpam_HTML",
			DATA_TEST_DIR+"/testHam_HTML"};
	public static final String DATA_LEARN_HTML_3[] = {DATA_LEARN_DIR+"/learnSpam_HTML",
			DATA_LEARN_DIR+"/learnHam_HTML"};
	public static final String WORD_TABLE_3 = "learning_words_3";
	

}
