import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Init {
	
	public String DIR = "/Users/Yao/Desktop/hw2";
	public String HOME = ".";
	
	public String token_docs = DIR + "/cacm";
	public String corps_freq = DIR + "/cacm";
	public String stoplist   = DIR + "/common_words";
	public String titles     = DIR + "/titles.short";	
	public String token_qrys = DIR + "/query";
	public String query_freq = DIR + "/query";
	public String query_relv = DIR + "/query.rels";
	
	
	public String token_intr = HOME + "/interactive";
	public String inter_freq = HOME + "/interactive";
	
	private int QUERY_BASE_WEIGHT = 2;
	private int QUERY_AUTH_WEIGHT = 2;
	
	public void init_files(String stem) {
		
		if (stem.equalsIgnoreCase("stemmed")) {
			token_docs += ".stemmed";
			corps_freq += ".stemmed.hist";
			stoplist += ".stemmed";
			token_qrys += ".stemmed";
			query_freq += ".stemmed.hist";
			token_intr += ".stemmed";
			inter_freq += ".stemmed.hist";
		}
		else {
			token_docs += ".tokenized";
			corps_freq += ".tokenized.hist";
			token_qrys += ".tokenized";
			query_freq += ".tokenized.hist";
			token_intr += ".tokenized";
			inter_freq += ".tokenized.hist";
		}
	}
	
	public int init_qry_vectors() {
		
		File token_qrys_fh = new File(token_qrys);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(token_qrys_fh));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String word = "";
		int qry_num = 0;
		int tweight = 0;
		
		// Push one empty value.
		Main.qry_vector.add(new HashMap<String, Double>());
		
		try {
			while((word = br.readLine()) != null) {
				if(word.equals("")) {
					continue;
				}
				
				if (word.equalsIgnoreCase(". I 0")) {
					break;
				}
				
				if (word.length() > 1 && word.substring(0, 2).equalsIgnoreCase(".I")) {
					Main.qry_vector.add(new HashMap<String, Double>());
					qry_num++;
					continue;
				}
				
				if(word.equalsIgnoreCase(".W")) {
					tweight = this.QUERY_BASE_WEIGHT;
					continue;
				}
				
				if(word.equalsIgnoreCase(".A")) {
					tweight = this.QUERY_AUTH_WEIGHT;
					continue;
				}
				
				if(((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') || 
				(word.charAt(0) >= 'A' && word.charAt(0) <= 'Z')) && (!Main.stoplist_hash.containsKey(word))) {
					if(Main.docs_freq_hash.containsKey(word)) {
						if (Main.qry_vector.get(qry_num).containsKey(word)) {
							double temp = Main.qry_vector.get(qry_num).get(word);
							Main.qry_vector.get(qry_num).put(word, temp + tweight);
						}
//						else {
//							Main.qry_vector.get(qry_num).put(word, (double) tweight);
//						}
					}
					else {
						System.out.println("ERROR: Document frequency of zero: " + word);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return qry_num;
		
	}
	
	public int init_doc_vectors() {
		
		int TITLE_BASE_WEIGHT = 4;
		int KEYWD_BASE_WEIGHT = 3;
		int ABSTR_BASE_WEIGHT = 1;
		int AUTHR_BASE_WEIGHT = 4;
		
		File token_qrys_fh = new File(token_qrys);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(token_qrys_fh));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String word = "";
		int doc_num = 0;
		int tweight = 0;
		
		Main.doc_vector = new ArrayList<HashMap<String,Double>>();
		try {
			while((word = br.readLine()) != null) {
				if(word.equals(" ")) {
					continue;
				}
				if(word.equalsIgnoreCase(".I 0")) {
					break;
				}
				if(word.length() > 1 && word.substring(0, 2).equalsIgnoreCase(".I")) {
					Main.doc_vector.add(new HashMap<String, Double>());
					doc_num++;
					continue;
				}
				if(word.equalsIgnoreCase(".T")) {
					tweight = TITLE_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".K")) {
					tweight = KEYWD_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".W")) {
					tweight = ABSTR_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".A")) {
					tweight = AUTHR_BASE_WEIGHT;
					continue;
				}
				
				if(((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') 
						|| (word.charAt(0) >= 'A' && word.charAt(0) <= 'Z')) 
						&& (!Main.stoplist_hash.containsKey(word))) {
					if(Main.docs_freq_hash.containsValue(word)) {
						if(Main.doc_vector.get(doc_num).containsKey(word)) {
							double temp = Main.doc_vector.get(doc_num).get(word);
							Main.doc_vector.get(doc_num).put(word, temp + tweight);
						}
						else {
							Main.doc_vector.get(doc_num).put(word, (double) tweight);
						}
					}
					else {
						System.out.println("ERROR: Document frequency of zero: " + word);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return doc_num;
	}
	
	public int init_Raw_TF_qry_vectors() {
		File token_qrys_fh = new File(token_qrys);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(token_qrys_fh));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String word = "";
		int qry_num = 0;
		int tweight = 0;
		
		// Push one empty value.
		Main.qry_vector.add(new HashMap<String, Double>());
		
		try {
			while((word = br.readLine()) != null) {
				if(word.equals("")) {
					continue;
				}
				
				if (word.equalsIgnoreCase(". I 0")) {
					break;
				}
				
				if (word.length() > 1 && word.substring(0, 2).equalsIgnoreCase(".I")) {
					Main.qry_vector.add(new HashMap<String, Double>());
					qry_num++;
					continue;
				}
				
				if(word.equalsIgnoreCase(".W")) {
					tweight = this.QUERY_BASE_WEIGHT;
					continue;
				}
				
				if(word.equalsIgnoreCase(".A")) {
					tweight = this.QUERY_AUTH_WEIGHT;
					continue;
				}
				
				if(((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') || 
				(word.charAt(0) >= 'A' && word.charAt(0) <= 'Z')) && (!Main.stoplist_hash.containsKey(word))) {
					if(Main.docs_freq_hash.containsKey(word)) {
						if (Main.qry_vector.get(qry_num).containsKey(word)) {
							double temp = Main.qry_vector.get(qry_num).get(word);
							Main.qry_vector.get(qry_num).put(word, temp + tweight);
						}
//						else {
//							Main.qry_vector.get(qry_num).put(word, (double) tweight);
//						}
					}
					else {
						System.out.println("ERROR: Document frequency of zero: " + word);
					}
				}
				
//				for (int i = 0; i < Main.qry_vector.size(); i++) {
//					
//				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return qry_num;
	}
	
	public void init_corp_freq() {
		File corps_freq_fh = new File(corps_freq);
		File query_freq_fh = new File(query_freq);
		File stoplist_fh = new File(stoplist);
		File titles_fh = new File(titles);
		File query_relv_fh = new File(query_relv);
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(corps_freq_fh));
			System.out.println(corps_freq_fh);
			String line = " ";
			
			try {
				while((line = br.readLine()) != null) {
					String[] str = line.split(" ");
					for(int i = 0; i < str.length; i++) {
						str[i] = str[i].replace(" ", "");
					}
					int doc_freq = Integer.parseInt(str[0]);
					int cor_freq = Integer.parseInt(str[1]);
					if(str.length > 2) {
						String term = str[2];
						Main.docs_freq_hash.put(term, doc_freq);
						Main.corp_freq_hash.put(term, cor_freq);
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			br = new BufferedReader(new FileReader(query_freq_fh));
			String line = " ";
			
			try {
				while((line = br.readLine()) != null) {
					String[] str = line.split(" ");
					for (int i = 0; i < str.length; i++) {
						str[i] = str[i].replace("", "");
					}
					
					int doc_freq = Integer.parseInt(str[0]);
					int cor_freq = Integer.parseInt(str[1]);
					if(str.length > 2) {
						String term = str[2];
						if(Main.docs_freq_hash.containsKey(term)) {
							int temp = Main.docs_freq_hash.get(term);
							Main.docs_freq_hash.put(term, temp + doc_freq);
						}
						else {
							Main.docs_freq_hash.put(term, doc_freq);
						}
						if(Main.corp_freq_hash.containsKey(term)) {
							int temp = Main.corp_freq_hash.get(term);
							Main.corp_freq_hash.put(term, temp + cor_freq);
						}
						else {
							Main.corp_freq_hash.put(term, cor_freq);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try{
			br = new BufferedReader(new FileReader(stoplist_fh));
			String line = "";
			while((line = br.readLine()) != null) {
				Main.stoplist_hash.put(line, 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			br = new BufferedReader(new FileReader(titles_fh));
			String line = "";
			Main.titles_vector.add("");
			try {
				while((line = br.readLine()) != null) {
					Main.titles_vector.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			br = new BufferedReader(new FileReader(query_relv_fh));
			String line = "";
			Main.titles_vector.add("");
			int index = 0;
			try {
				while((line = br.readLine()) != null) {
					List<String> list = new ArrayList<String>();
					String[] str = line.split("");
					for(int i = 0; i < str.length; i++) {
						str[i] = str[i].replace("", "");
						if(str[i].equals("")) {
							continue;
						}
						else {
							list.add(str[i]);
						}
					}
					int qry_num = Integer.parseInt(list.get(0));
					int rel_doc = Integer.parseInt(list.get(1));
					if(index != qry_num) {
						index++;
						List<Integer> rels = new ArrayList<Integer>();
						Main.relevance_hash.put(qry_num, (ArrayList<Integer>) rels);
					}
					Main.relevance_hash.get(qry_num).add(rel_doc);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int init_1114_doc_vectors() {
		
		int TITLE_BASE_WEIGHT = 1;
		int KEYWD_BASE_WEIGHT = 1;
		int ABSTR_BASE_WEIGHT = 4;
		int AUTHR_BASE_WEIGHT = 1;
		
		File token_qrys_fh = new File(token_qrys);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(token_qrys_fh));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String word = "";
		int doc_num = 0;
		int tweight = 0;
		
		Main.doc_vector = new ArrayList<HashMap<String,Double>>();
		try {
			while((word = br.readLine()) != null) {
				if(word.equals(" ")) {
					continue;
				}
				if(word.equalsIgnoreCase(".I 0")) {
					break;
				}
				if(word.length() > 1 && word.substring(0, 2).equalsIgnoreCase(".I")) {
					Main.doc_vector.add(new HashMap<String, Double>());
					doc_num++;
					continue;
				}
				if(word.equalsIgnoreCase(".T")) {
					tweight = TITLE_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".K")) {
					tweight = KEYWD_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".W")) {
					tweight = ABSTR_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".A")) {
					tweight = AUTHR_BASE_WEIGHT;
					continue;
				}
				
				if(((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') 
						|| (word.charAt(0) >= 'A' && word.charAt(0) <= 'Z')) 
						&& (!Main.stoplist_hash.containsKey(word))) {
					if(Main.docs_freq_hash.containsValue(word)) {
						if(Main.doc_vector.get(doc_num).containsKey(word)) {
							double temp = Main.doc_vector.get(doc_num).get(word);
							Main.doc_vector.get(doc_num).put(word, temp + tweight);
						}
						else {
							Main.doc_vector.get(doc_num).put(word, (double) tweight);
						}
					}
					else {
						System.out.println("ERROR: Document frequency of zero: " + word);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return doc_num;
	}

	public int init_equal_doc_vectors() {
		
		int TITLE_BASE_WEIGHT = 1;
		int KEYWD_BASE_WEIGHT = 1;
		int ABSTR_BASE_WEIGHT = 1;
		int AUTHR_BASE_WEIGHT = 1;
		
		File token_qrys_fh = new File(token_qrys);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(token_qrys_fh));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String word = "";
		int doc_num = 0;
		int tweight = 0;
		
		Main.doc_vector = new ArrayList<HashMap<String,Double>>();
		try {
			while((word = br.readLine()) != null) {
				if(word.equals(" ")) {
					continue;
				}
				if(word.equalsIgnoreCase(".I 0")) {
					break;
				}
				if(word.length() > 1 && word.substring(0, 2).equalsIgnoreCase(".I")) {
					Main.doc_vector.add(new HashMap<String, Double>());
					doc_num++;
					continue;
				}
				if(word.equalsIgnoreCase(".T")) {
					tweight = TITLE_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".K")) {
					tweight = KEYWD_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".W")) {
					tweight = ABSTR_BASE_WEIGHT;
					continue;
				}
				if(word.equalsIgnoreCase(".A")) {
					tweight = AUTHR_BASE_WEIGHT;
					continue;
				}
				
				if(((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') 
						|| (word.charAt(0) >= 'A' && word.charAt(0) <= 'Z')) 
						&& (!Main.stoplist_hash.containsKey(word))) {
					if(Main.docs_freq_hash.containsValue(word)) {
						if(Main.doc_vector.get(doc_num).containsKey(word)) {
							double temp = Main.doc_vector.get(doc_num).get(word);
							Main.doc_vector.get(doc_num).put(word, temp + tweight);
						}
						else {
							Main.doc_vector.get(doc_num).put(word, (double) tweight);
						}
					}
					else {
						System.out.println("ERROR: Document frequency of zero: " + word);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return doc_num;
	}
}
