import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GetAndShowRetrievedSet {
	
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
	
	public void get_and_show_retrieved_set() {
		
		String comp_type = "";
		int vect_num = 1;
		int max_show = 20;
		
		System.out.println("Find documents similar to: ");
		System.out.println(" (1) a query from 'query.raw'");
		System.out.println("(2) an interactive query");
		System.out.println("(3) another document");
		System.out.println("Choice: ");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		try {
			comp_type = br.readLine();
			if(!comp_type.equals("2")) {
				System.out.println("Target Document/Query number: ");
				str = br.readLine();
				if(str.charAt(0) < '1' || str.charAt(0) > '9') {
					vect_num = 1;
				}
				else {
					vect_num = Integer.parseInt(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Show how many matching documents (20): ");
		try {
			str = br.readLine();
			if(str.equals("")) {
				max_show = 20;
			}
			else {
				max_show = Integer.parseInt(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(comp_type.equals("3")) {
			System.out.println("Document to Document comparison\n");
			get_retrieved_set(Main.doc_vector.get(vect_num));
			shw_retrieved_set(max_show, vect_num, Main.doc_vector.get(vect_num), "Document");
		}
		else if(comp_type.equals("2")) {
			
		}
		else {
			System.out.println("Query to Document comparison\n");
			//HashMap<String, Double> int_vector = set_interact_vec();			
			get_retrieved_set(Main.qry_vector.get(vect_num));
			shw_retrieved_set(max_show, vect_num, Main.qry_vector.get(vect_num), "Query");
			
			Comp_recall cr = new Comp_recall();
			cr.comp_recall(Main.relevance_hash.get(vect_num), vect_num);
//			Show_relvnt sr = new Show_relvnt();
//			sr.show_relvnt(Main.relevance_hash.get(vect_num), vect_num, Main.qry_vector.get(vect_num));
		}
		
	}
	
	private HashMap<String, Double> set_interact_vec() {
		
		int QUERY_BASE_WEIGHT = 2;
		int QUERY_AUTH_WEIGHT = 2;
		
		File token_qrys_fh = new File(token_qrys);
		
		 HashMap<String, Double> int_vector = new HashMap<String,Double>();
			
		int tweight = 0;
		int qry_num = 0;
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(token_qrys_fh));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String word = "";
		
		try {
			while((word = br.readLine()) != null) {
				if(word.equals("")) {
					continue;
				}
				
				if(word.equalsIgnoreCase(".W")) {
					tweight = QUERY_BASE_WEIGHT;
					continue;
				}
				
				if(word.equalsIgnoreCase(".A")) {
					tweight = QUERY_AUTH_WEIGHT;
					continue;
				}
				
				if(((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') || 
						(word.charAt(0) >= 'A' && word.charAt(0) <= 'Z')) && (!Main.stoplist_hash.containsKey(word))) {
							if(Main.docs_freq_hash.containsKey(word)) {
								if (Main.qry_vector.get(qry_num).containsKey(word)) {
									int_vector.get(word).equals(int_vector.get(word) + tweight);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return int_vector;
	}

	public void get_retrieved_set(HashMap<String, Double> qry_vector) {
		
		int tot_number = Main.doc_vector.size() - 1;
		int index = 0;
		
		Main.doc_simula = new HashMap<Integer, Double>();
		Main.res_vector = new HashMap<Integer, Integer>();
		
		Main.doc_simula.put(0, 0.0);
		for(index = 1; index <= tot_number; index++) {
			if (Main.dice) {
				Main.doc_simula.put(index, Similarity.dice_sim_a(qry_vector, Main.doc_vector.get(index)));
			}
			else {
				Main.doc_simula.put(index, Similarity.cosine_sim_a(qry_vector, Main.doc_vector.get(index)));
			}	
		}
		
		List<Map.Entry<Integer, Double>> sortList = 
				new ArrayList<Map.Entry<Integer, Double>>(Main.doc_simula.entrySet());
		Collections.sort(sortList, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> val1, Map.Entry<Integer, Double> val2) {
				return (val1.getValue()).compareTo(val2.getValue())*(-1);
			}
		});
		
		Main.res_vector.put(0, 0);
		for(int rank = 1; rank < sortList.size(); rank++) {
			Main.res_vector.put(rank, sortList.get(rank - 1).getKey());
		}
	}
	
	private void shw_retrieved_set(int max_show, int qry_num,
			HashMap<String, Double> qry_vect, String comparison) {
		System.out.println("************************************************************");
		System.out.println("Documents Most Similar To $comparison number $qry_num");
		System.out.println("************************************************************");
		System.out.println("Similarity   Doc#  Author      Title");
		System.out.println("==========   ==== ========     =============================");
		
		for(int i = 1; i < max_show + 1; i++) {
			int ind = Main.res_vector.get(i);
			
			if(comparison.equalsIgnoreCase("Query") && Main.relevance_hash.get(qry_num).contains(ind)) {
				System.out.println("*");
			}
			else {
				System.out.println(" ");
			}
			
			double similarity = Main.doc_simula.get(ind);
			String title;
			if(Main.titles_vector.get(ind).length() > 48) {
				title = Main.titles_vector.get(ind).substring(0, 47);
			}
			else{
				title = Main.titles_vector.get(ind);
			}
			
			System.out.println("  " + similarity + "  " + title);
		}
		
		System.out.println("");
		System.out.println("Show the terms that overlap between the query and ");
		System.out.println("retrieved docs (y/n): ");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String show_term;
		try {
			show_term = br.readLine();
			if(show_term.equalsIgnoreCase("y")) {
				for(int i = 1; i < max_show + 1; i++) {
					int ind = Main.res_vector.get(i);
					show_overlap(qry_vect, Main.doc_vector.get(ind), qry_num, ind);
					if(i % 5 == 4) {
						System.out.println("");
						System.out.println("Continue (y/n)? ");
						
						if(show_term.equalsIgnoreCase("n")) {
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private void show_overlap(HashMap<String, Double> qry_vect,
			HashMap<String, Double> doc_vect, int qry_num, int doc_num) {
		
		System.out.println("============================================================");
		System.out.printf("%-15s  %8d   %8d\t%s\n", "Vector Overlap", qry_num, doc_num, "Docfreq");
		System.out.println("============================================================");
		
		Iterator iterator = qry_vect.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String term = entry.getKey().toString();
			double weight = qry_vect.get(term);
			if(doc_vect.containsKey(term)) {
				System.out.println("%-15s  %8d   %8d\t%d\n");
				System.out.println(doc_vect.get(term));
				System.out.println(weight);
				System.out.println(Main.docs_freq_hash.get(term));
			}
			
		}
		
	}
}
