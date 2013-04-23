import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Comp_recall {
	private static int total_doc_num = Main.doc_vector.size() - 1;
	private static double prec_mean_1 = 0;
	private static double prec_mean_2 = 0;
	private static double prec_temp = 0;
	
	public void comp_recall(ArrayList<Integer> rel_hash, int vect_num) {
		
		Main.qry_rank_array = new ArrayList<List<Integer>>();
		
		for (int key = 0; key < rel_hash.size(); key++) {
			for (int index = 1; index < total_doc_num; index++) {
				if(key == Main.res_vector.get(index)) {
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(key);
					Main.qry_rank_array.add(index + 1, tempList);
				}
			}
		}
		
		Main.qry_rank_array = new ArrayList<List<Integer>>();
		Collections.sort(Main.qry_rank_array, new Comparator<List<Integer>>() {
			
			public int compare(List<Integer> o1, List<Integer> o2) {
				return o1.get(0).compareTo(o2.get(0));
			}
		});
		
		int total_relevant = rel_hash.size();
		
		for(int key_2 = 0; key_2 < Main.qry_rank_array.size(); key_2++) {
			Main.rec_array.get(key_2).equals((key_2 + 1) / Main.qry_rank_array.size());
			Main.prec_array.get(key_2).equals((key_2 + 1) / Main.qry_rank_array.get(key_2).get(0));
			Main.qry_rank_array.get(key_2).get(2).equals(Main.prec_array.get(key_2));
		}
		
		double prec_i_10 = 0;
		double rec_special = 0;
		double temp = 0;
		
		for(int i = 1; i <= 10; i++) {
			rec_special = i / 10;
			prec_i_10 = linear_interpolation(rec_special);
			temp = temp + prec_i_10;
		}
		
		prec_mean_1 = (linear_interpolation(0.25) + linear_interpolation(0.50) + 
				linear_interpolation(0.75)) / 3;
		
		prec_mean_2 = temp / 10;
		
		Main.result_array.get(0).equals(Main.result_array.get(0) + linear_interpolation(0.25));
		Main.result_array.get(1).equals(Main.result_array.get(1) + linear_interpolation(0.50));
		Main.result_array.get(2).equals(Main.result_array.get(2) + linear_interpolation(0.75));
		Main.result_array.get(3).equals(Main.result_array.get(3) + linear_interpolation(1.00));
		Main.result_array.get(4).equals(Main.result_array.get(4) + prec_mean_1);
		Main.result_array.get(5).equals(Main.result_array.get(5) + prec_mean_2);
		Main.result_array.get(5).equals(Main.result_array.get(6) + prec_norm());
		Main.result_array.get(5).equals(Main.result_array.get(7) + rec_norm());
		
	}

	private static Double rec_norm() {
		int rel = Main.qry_rank_array.size();
		int N = total_doc_num;
		int temp = 0;
		for(int i = 0; i < rel; i++) {
			temp = temp + Main.qry_rank_array.get(i).get(0);
		}
		return (double) (1 - (temp - (1 + rel) * rel / 2) / rel / (N - rel));
	}

	private static Double prec_norm() {
		int rel = Main.qry_rank_array.size();
		int N = total_doc_num;
		double sum_rank = 0;
		double sum_relv = 0;
		for(int i = 0; i < rel; i++) {
			sum_rank = sum_rank + Math.log(Main.qry_rank_array.get(i).get(0));
			sum_relv = sum_relv + Math.log(i + 1);
		}
		return 1-(sum_rank-sum_relv)/(N*Math.log(N)-(N-rel)*Math.log(N-rel)
				-(rel)*Math.log(rel));
	}

	private static double linear_interpolation(double rec_special) {
		int length = Main.rec_array.size();
		for(int array_index = 0; array_index < length; array_index++) {
			if ((Main.rec_array.get(array_index) < rec_special) 
					&& (Main.rec_array.get(array_index + 1) > rec_special)) {
						prec_temp = (Main.prec_array.get(array_index + 1) - Main.prec_array.get(array_index))
								* (rec_special - Main.rec_array.get(array_index)) 
								/ (Main.rec_array.get(array_index + 1) - Main.rec_array.get(array_index)) 
								+ Main.prec_array.get(array_index);
					}
			if (rec_special < Main.rec_array.get(0)) {
				prec_temp = (Main.prec_array.get(1) - Main.prec_array.get(0)) * (rec_special - Main.rec_array.get(0))
						/ (Main.rec_array.get(1) - Main.rec_array.get(0) + Main.prec_array.get(0));
			}
			
			if (rec_special > Main.rec_array.get(length - 1)) {
				prec_temp = (Main.prec_array.get(length - 1) - Main.prec_array.get(length - 2)) 
						* (rec_special - Main.rec_array.get(length - 2)) / (Main.rec_array.get(length - 1) - 
								Main.rec_array.get(length - 2)) + Main.prec_array.get(length - 2);
			}
			
			if (length == 1) {
				prec_temp = Main.prec_array.get(length - 1);
			}
		}
		return prec_temp;
	}
	
}
