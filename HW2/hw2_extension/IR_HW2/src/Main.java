import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {
	
	public static HashMap<String, Integer> docs_freq_hash = new HashMap<String, Integer>();
	public static HashMap<String, Integer> corp_freq_hash = new HashMap<String, Integer>(); 
	public static HashMap<String, Integer> stoplist_hash = new HashMap<String, Integer>();
	public static HashMap<Integer, ArrayList<Integer>> relevance_hash = new HashMap<Integer, ArrayList<Integer>>();
	public static List<String> titles_vector = new ArrayList<String>();
	public static List<HashMap<String, Double>> qry_vector = new ArrayList<HashMap<String,Double>>();
	public static List<HashMap<String, Double>> doc_vector = new ArrayList<HashMap<String,Double>>();
	public static HashMap<Integer, Double> doc_simula = new HashMap<Integer,Double>();
	public static HashMap<Integer, Integer> res_vector = new HashMap<Integer,Integer>();
	public static List<Double> rec_array = new ArrayList<Double>();
	public static List<Double> prec_array = new ArrayList<Double>();
	public static List<List<Integer>> qry_rank_array = new ArrayList<List<Integer>>();
	public static List<Double> result_array = new ArrayList<Double>();
	public static String permutation_type = "0";
	public static boolean dice = false;
			
	
	public static void main(String args[]) {
		
		Init init = new Init();
		init.init_files("unstemmed");
		//init.init_corp_freq();
		
		String option = "0";
		String option2 = "0";
		
		int total_docs;
		int total_qrys;
				
		doc_vector = new ArrayList<HashMap<String,Double>>();
		qry_vector = new ArrayList<HashMap<String,Double>>();
		docs_freq_hash = new HashMap<String, Integer>();
		corp_freq_hash = new HashMap<String, Integer>();
		stoplist_hash = new HashMap<String, Integer>();
		titles_vector = new ArrayList<String>();
		relevance_hash = new HashMap<Integer, ArrayList<Integer>>();
		doc_simula = new HashMap<Integer, Double>();
		res_vector = new HashMap<Integer, Integer>();
		
		System.out.println("============================================================");
		System.out.println("==     Welcome to the 600.466 Vector-based IR Engine");
		System.out.println("==  ");
		System.out.println("    == Total Documents: $total_docs");
		System.out.println("== Total Queries:   $total_qrys  ");
		System.out.println("============================================================");
		
		System.out.println( "INITIALIZING VECTORS ... \n" );
		
		System.out.println("OPTIONS:");
		System.out.println("  1 = Find documents most similar to a given query or document:");
		System.out.println("  2 = Compute precision/recall for the full query set:");
		System.out.println("  3 = Compute cosine similarity between two queries/documents:");
		System.out.println("  4 = Quit:");
		System.out.println("============================================================");
		
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		try {
			option2 = br1.readLine();
			if (option2.equals("1")) {
				GetAndShowRetrievedSet getAndShowRetrievedSet = new GetAndShowRetrievedSet();
				getAndShowRetrievedSet.get_and_show_retrieved_set();
			}
			else if(option2.equals("2")) {
		
				System.out.println( "PERMUTATION OPTIONS:" );
				System.out.println( "  1 = Default" );
				System.out.println( "  2 = Raw TF weighting" );
				System.out.println( "  3 = Boolean weighting" );
				System.out.println( "  4 = Dice similarity" );
				System.out.println( "  5 = Use raw, unstemmed tokens (all converted to lower case)" );
				System.out.println( "  6 = Include all tokens, including punctuation" );
				System.out.println( "  7 = Weight titles, keywords, author list and abstract words equally" );
				System.out.println( "  8 = Use relative weighhts of titles=1x, keywords=1x, author list=1x,abstract=4x" );
				System.out.println( "============================================================" );
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try {
					option = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(option.equals("2")) {
					dice = false;
					permutation_type = "2";
					total_docs = init.init_doc_vectors();
					total_qrys = init.init_Raw_TF_qry_vectors();
					init.init_corp_freq();
					init.init_files("stemmed");
				}
				
				if(option.equals("3")) {
					dice = false;
					permutation_type = "3";
					total_docs = init.init_doc_vectors();
					total_qrys = init.init_qry_vectors();
					init.init_corp_freq();
					init.init_files("stemmed");
				}
				
				if(option.equals("4")) {
					dice = true;
					permutation_type = "4";
					total_docs = init.init_doc_vectors();
					total_qrys = init.init_qry_vectors();
					init.init_corp_freq();
				}
				
				if(option.equals("5")) {
					dice = false;
					permutation_type = "5";
					total_docs = init.init_doc_vectors();
					total_qrys = init.init_qry_vectors();
					init.init_files("unstemmed");
				}
				
				if(option.equals("6")) {
					dice = false;
					permutation_type = "6";
					total_docs = init.init_doc_vectors();
					total_qrys = init.init_qry_vectors();
					init.init_corp_freq();
					init.init_files("stemmed");
				}
				
				if(option.equals("7")) {
					dice = false;
					permutation_type = "7";
					total_docs = init.init_equal_doc_vectors();
					total_qrys = init.init_qry_vectors();
					init.init_corp_freq();
					init.init_files("stemmed");
				}
				
				if(option.equals("8")) {
					dice = false;
					permutation_type = "8";
					total_docs = init.init_1114_doc_vectors();
					total_qrys = init.init_qry_vectors();
					init.init_corp_freq();
					init.init_files("stemmed");
				}
				
				if(option.equals("1")) {
					dice = true;
					permutation_type = "1";
					total_docs = init.init_doc_vectors();
					total_qrys = init.init_qry_vectors();
					init.init_corp_freq();
					init.init_files("stemmed");
				}
			}
			Full_precision_recall_test f = new Full_precision_recall_test();
			f.full_precision_recall_test();

			if (option2.equals("3")) {

			}
			if (option2.equals("4")) {
				//break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		

	}
}
