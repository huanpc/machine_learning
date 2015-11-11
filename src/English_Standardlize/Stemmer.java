
package English_Standardlize;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Stemmer {
	private char[] b;
	public static Irregular irr = new Irregular();
	private int i, /* vi tri trong b */
	i_end, /* vi tri ket thuc trong tu */
	j, k;
	private static int INC = 50;

	/* kich thuoc cua mang char stemmer */

	public Stemmer() {
		b = new char[INC];
		i = 0;
		i_end = 0;
	}

	public void resetIndex() {
		b = new char[INC];
		i = 0;
		i_end = 0;
		j = 0;
		k = 0;

	}

	/**
	 * @param char ch
	 * @return void them ky tu vao trong stemmer
	 * 
	 */

	public void add(char ch) {
		if (i == b.length) {
			char[] new_b = new char[i + INC];
			for (int c = 0; c < i; c++)
				new_b[c] = b[c];
			b = new_b;
		}
		b[i++] = ch;
	}

	/**
	 * @param char[] , int
	 * @return void them vao mang chua cac ky tu
	 */

	public void add(char[] w, int wLen) {
		if (i + wLen >= b.length) {
			char[] new_b = new char[i + wLen + INC];
			for (int c = 0; c < i; c++)
				new_b[c] = b[c];
			b = new_b;
		}
		for (int c = 0; c < wLen; c++)
			b[i++] = w[c];
	}

	/**
	 * @return String sau khi mot tu duoc stemmer , co the lay ra bang cach dung
	 *         ham toString
	 */
	public String toString() {
		return new String(b, 0, i_end);
	}

	/**
	 * 
	 * @return int tra ve do dai mang b;
	 */
	public int getResultLength() {
		return i_end;
	}

	/**
	 * 
	 * @return char[] tra ve mang b
	 */
	public char[] getResultBuffer() {
		return b;
	}

	/**
	 * @param int i
	 * @return boolean cons(i)= true khi ma b[i] la mot phu am.
	 */

	private final boolean cons(int i) {
		switch (b[i]) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return false;
		case 'y':
			return (i == 0) ? true : !cons(i - 1);
		default:
			return true;
		}
	}

	/**
	 * @param void
	 * @return int
	 * 
	 *         ham m() tinh so cum phu am nguyen am theo cong thuc <c><v> gives
	 *         0 <c>vc<v> gives 1 <c>vcvc<v> gives 2 <c>vcvcvc<v> gives 3 ....
	 */

	private final int m() {
		int n = 0;
		int i = 0;
		while (true) {
			if (i > j)
				return n;
			if (!cons(i))
				break;
			i++;
		}
		i++;
		while (true) {
			while (true) {
				if (i > j)
					return n;
				if (cons(i))
					break;
				i++;
			}
			i++;
			n++;
			while (true) {
				if (i > j)
					return n;
				if (!cons(i))
					break;
				i++;
			}
			i++;
		}
	}

	/**
	 * @param void
	 * @return boolean ham vowelinstem la ham cho ta biet trong mang ky tu
	 *         stemmer co nguyen am hay khong.
	 */

	private final boolean vowelinstem() {
		int i;
		for (i = 0; i <= j; i++)
			if (!cons(i))
				return true;
		return false;
	}

	/**
	 * @param int
	 * @return boolean ham doublec (j) se tra ve true <=> tu thu j, j-1 la mot
	 *         cum phu am. vd : 'ph'
	 */

	private final boolean doublec(int j) {
		if (j < 1)
			return false;
		if (b[j] != b[j - 1])
			return false;
		return cons(j);
	}

	/**
	 * @param int
	 * @boolean ham cvc(i) tra ve true <=> tu thu i-1,i-1,i trong mảng ký tự b[]
	 *          theo thu tu la phu am- nguyen am - phu am chu y phu am thu 2
	 *          khong the la w,x,y
	 */

	private final boolean cvc(int i) {
		if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2))
			return false;
		{
			int ch = b[i];
			if (ch == 'w' || ch == 'x' || ch == 'y')
				return false;
		}
		return true;
	}

	/**
	 * @param String
	 * @return boolean hàm kiểm tra xem ở trong chuỗi b[] có tận cùng bằng xâu s
	 *         không
	 */
	private final boolean ends(String s) {
		int l = s.length();
		int o = k - l + 1;
		if (o < 0)
			return false;
		for (int i = 0; i < l; i++)
			if (b[o + i] != s.charAt(i))
				return false;
		j = k - l;
		return true;
	}

	/**
	 * @param String
	 * @return void hàm dùng để set lại mảng ký tự b[] .
	 */

	private final void setto(String s) {
		int l = s.length();
		int o = j + 1;
		for (int i = 0; i < l; i++)
			b[o + i] = s.charAt(i);
		k = j + l;
	}

	/**
	 * @param String
	 * @return void dùng để cắt bỏ xâu s ra khỏi mảng ký tự b[]
	 */

	private final void r(String s) {
		if (m() > 0)
			setto(s);
	}

	/*********************************** 6 step for stemming **************************************************/
	/*
	 * step1() cat bo s,es or -ed or -ing. e.g.
	 */

	private final void step1() {
		if (b[k] == 's') {
			if (ends("sses") || ends("oes") || ends("ches"))
				k -= 2;
			else if (ends("ies"))
				setto("i");
			else if (b[k - 1] != 's')
				k--;

		}
		if (ends("eed")) {
			if (m() > 0)
				k--;
		} else if ((ends("ed") || ends("ing")) && vowelinstem()) {
			k = j;
			if (ends("at"))
				setto("ate");
			else if (ends("bl"))
				setto("ble");
			else if (ends("iz"))
				setto("ize");
			else if (doublec(k)) {
				k--;
				{
					int ch = b[k];
					if (ch == 'l' || ch == 's' || ch == 'z')
						k++;
				}
			} else if (m() == 1 && cvc(k))
				setto("e");
		}
	}

	/* step2() bien doi y thanh i when co mot nguyen am khac trong stem */

	private final void step2() {
		if (ends("y") && vowelinstem())
			b[k] = 'i';
	}

	private final void step3() {
		if (k == 0)
			return;
		switch (b[k - 1]) {
		case 'a':
			if (ends("ational")) {
				r("ate");
				break;
			}
			if (ends("tional")) {
				r("tion");
				break;
			}
			break;
		case 'c':
			if (ends("enci")) {
				r("ence");
				break;
			}
			if (ends("anci")) {
				r("ance");
				break;
			}
			break;
		case 'e':
			if (ends("izer")) {
				r("ize");
				break;
			}
			break;
		case 'l':
			if (ends("bli")) {
				r("ble");
				break;
			}
			if (ends("alli")) {
				r("al");
				break;
			}
			if (ends("entli")) {
				r("ent");
				break;
			}
			if (ends("eli")) {
				r("e");
				break;
			}
			if (ends("ousli")) {
				r("ous");
				break;
			}
			break;
		case 'o':
			if (ends("ization")) {
				r("ize");
				break;
			}
			if (ends("ation")) {
				r("ate");
				break;
			}
			if (ends("ator")) {
				r("ate");
				break;
			}
			break;
		case 's':
			if (ends("alism")) {
				r("al");
				break;
			}
			if (ends("iveness")) {
				r("ive");
				break;
			}
			if (ends("fulness")) {
				r("ful");
				break;
			}
			if (ends("ousness")) {
				r("ous");
				break;
			}
			break;
		case 't':
			if (ends("aliti")) {
				r("al");
				break;
			}
			if (ends("iviti")) {
				r("ive");
				break;
			}
			if (ends("biliti")) {
				r("ble");
				break;
			}
			break;
		case 'g':
			if (ends("logi")) {
				r("log");
				break;
			}
		}
	}

	private final void step4() {
		switch (b[k]) {
		case 'e':
			if (ends("icate")) {
				r("ic");
				break;
			}
			if (ends("ative")) {
				r("");
				break;
			}
			if (ends("alize")) {
				r("al");
				break;
			}
			break;
		case 'i':
			if (ends("iciti")) {
				r("ic");
				break;
			}
			break;
		case 'l':
			if (ends("ical")) {
				r("ic");
				break;
			}
			if (ends("ful")) {
				r("");
				break;
			}
			break;
		case 's':
			if (ends("ness")) {
				r("");
				break;
			}
			break;
		}
	}

	private final void step5() {
		if (k == 0) {
			return;
		}
		switch (b[k - 1]) {
		case 'a':
			if (ends("al")) {
				break;
			}
			return;
		case 'c':
			if (ends("ance")) {
				break;
			}
			if (ends("ence")) {
				break;
			}
			return;
		case 'e':
			if (ends("er")) {
				break;
			}
			return;
		case 'i':
			if (ends("ic")) {
				break;
			}
			return;
		case 'l':
			if (ends("able")) {
				break;
			}
			if (ends("ible")) {
				break;
			}
			return;
		case 'n':
			if (ends("ant")) {
				break;
			}
			if (ends("ement")) {
				break;
			}
			if (ends("ment")) {
				break;
			}
			if (ends("ent")) {
				break;
			}
			return;
		case 'o':
			if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) {
				break;
			}
			if (ends("ou")) {
				break;
			}
			return;
		case 's':
			if (ends("ism")) {
				break;
			}
			return;
		case 't':
			if (ends("ate")) {
				break;
			}
			if (ends("iti")) {
				break;
			}
			return;
		case 'u':
			if (ends("ous")) {
				break;
			}
			return;
		case 'v':
			if (ends("ive")) {
				break;
			}
			return;
		case 'z':
			if (ends("ize")) {
				break;
			}
			return;
		default:
			return;
		}
		if (m() > 1) {
			k = j;
		}
	}

	private final void step6() {
		j = k;
		if (b[k] == 'e') {
			int a = m();
			if (a > 1 || a == 1 && !cvc(k - 1))
				k--;
		}
		if (b[k] == 'l' && doublec(k) && m() > 1)
			k--;
	}

	private final void step7() {
		char[] a = new char[k + 1];
		for (int i = 0; i < k + 1; i++) {
			a[i] = (char) b[i];
		}
		String tuDangXet = new String(a);
		int index = Irregular.quaKhu1.indexOf(tuDangXet);
		if (index != -1) {
			tuDangXet = Irregular.nguyenThe.get(index);
		} else {
			index = Irregular.quaKhu2.indexOf(tuDangXet);
			if (index != -1) {
				tuDangXet = Irregular.nguyenThe.get(index);
			}
		}
		k = tuDangXet.length() - 1;
		for (int i = 0; i < tuDangXet.length(); i++) {
			b[i] = tuDangXet.charAt(i);
		}
	}

	/**
	 * @param void
	 * @return void hàm gọi thực thiện các bước của thuật toán, hàm xử lý từng
	 *         từ. từ được biểu diễn bởi một mảng ký tự b[]
	 */
	public void stem() {
		k = i - 1;
		if (k > 1) {
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
			step7();
		}
		i_end = k + 1;
		i = 0;
	}

	/**
	 * 
	 * @param ArrayList
	 *            <String>
	 * 
	 * @return ArrayList <String> giá trị đầu vào là một arraylist chứa các từ
	 *         cần được cắt tỉa, mảng sẽ được thay đổi bằng cách áp dụng các
	 *         bước từ step1()->step6() của thuật toán để cắt bỏ.
	 */
	public static Hashtable<String, Integer>[] StemAllHashTable(
			Hashtable<String, Integer> k[]) {

		try {
			char[] w = new char[50];
			Stemmer s = new Stemmer();
			int a = 0, b = 0;
			String temp;
			for (int i = 0; i < k.length; i++) {
				Set<String> set = k[i].keySet();
				Object array[] = set.toArray();
				for (int j = 0; j < array.length; j++) {
					temp = array[j].toString();
					a = ((Integer) k[i].get(temp)).intValue();

					k[i].remove(temp);
					temp = temp.toLowerCase();
					temp.getChars(0, temp.length(), w, 0);
					s.resetIndex();
					s.add(w, temp.length());
					s.stem();
					temp = s.toString();
					if (k[i].get(temp) == null) {
						k[i].put(temp, a);
					} else {
						b = ((Integer) k[i].get(temp)).intValue();
						k[i].remove(temp);
						k[i].put(temp, a + b);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return k;
	}

	public static ArrayList<String> StemArrayList(ArrayList<String> k) {
		ArrayList<String> result = k;
		try {
			char[] w = new char[50];
			Stemmer s = new Stemmer();
			int len;
			result = new ArrayList<String>();
			for (String temp : k) {
				temp = temp.toLowerCase();
				len = temp.length();
				if (len > w.length) {
					w = new char[len + 1];
					INC = len + 1;
				}
				temp.getChars(0, len, w, 0);
				s.resetIndex();
				s.add(w, len);
				s.stem();
				temp = s.toString();
				result.add(temp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void StemHashTable(Hashtable<String, Integer> k) {
		try {
			char[] w = new char[50];
			Stemmer s = new Stemmer();
			int len;
			int a = 0, b = 0;
			String temp;
			Set<String> set = k.keySet();
			Object array[] = set.toArray();
			for (int j = 0; j < array.length; j++) {
				temp = array[j].toString();
				a = k.get(temp);
				k.remove(temp);
				temp = temp.toLowerCase();
				len = temp.length();
				if (len > w.length) {
					w = new char[len + 1];
					INC = len + 1;
				}
				temp.getChars(0, len, w, 0);
				s.resetIndex();
				s.add(w, len);
				s.stem();
				temp = s.toString();
				if (k.get(temp) == null) {
					k.put(temp, a);
				} else {
					b = k.get(temp);
					k.put(temp, a + b);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String StemmerToken(String a) {
		String temp = a;
		try {
			char[] w = new char[50];
			Stemmer s = new Stemmer();
			int len;

			temp = temp.toLowerCase();
			len = temp.length();
			if (len > w.length) {
				w = new char[len + 1];
				INC = len + 1;
			}
			temp.getChars(0, len, w, 0);
			s.resetIndex();
			s.add(w, len);
			s.stem();
			temp = s.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return temp;
	}
}
