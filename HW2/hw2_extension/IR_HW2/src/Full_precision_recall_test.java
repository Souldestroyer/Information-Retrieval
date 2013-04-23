import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Full_precision_recall_test {
	
	
	public void full_precision_recall_test() {
		System.out.println("");
		System.out.println("");
		
		List<String> heading = new ArrayList<String>();
		heading.add("Permutation Name");
		heading.add("Prec_0.25");
		heading.add("Prec_0.50");
		heading.add("Prec_0.75");
		heading.add("Prec_1.00");
		heading.add("Pr_mean_1");
		heading.add("Pr_mean_2");
		heading.add("Prec_norm");
		heading.add("Rec_norm");
		
		List<String> choice = new ArrayList<String>();
		choice.add("Default");
		choice.add("Raw TF weighting");
		choice.add("Boolean weight");
		choice.add("Dice similarity");
		choice.add("Unstemmed Token");
		choice.add("Include token");
		choice.add("Equal weight");
		choice.add("Relative weight");
		
		for(int i = 0; i < heading.size(); i++) {
			System.out.println("%8s" + "|" + i);
		}
		
		System.out.println("");
		
		for(int index = 1; index <= 35; index++) {
//			String temp = "";
			GetAndShowRetrievedSet getAndShowRetrievedSet = new GetAndShowRetrievedSet();
			Comp_recall cr = new Comp_recall();
//			if(index < 10) {
//				temp = "0" + index;			
//			}
//			else {
//				temp = Integer.toString(index);
//				
//			}
			getAndShowRetrievedSet.get_retrieved_set(Main.qry_vector.get(index));
			cr.comp_recall(Main.relevance_hash.get(index), index);
		}
		
		Main.result_array.get(0).equals(Main.result_array.get(0) / 33);
		Main.result_array.get(1).equals(Main.result_array.get(1) / 33);
		Main.result_array.get(2).equals(Main.result_array.get(2) / 33);
		Main.result_array.get(3).equals(Main.result_array.get(3) / 33);
		Main.result_array.get(4).equals(Main.result_array.get(4) / 33);
		Main.result_array.get(5).equals(Main.result_array.get(5) / 33);
		Main.result_array.get(6).equals(Main.result_array.get(6) / 33);
		Main.result_array.get(7).equals(Main.result_array.get(7) / 33);
		
		System.out.println("%8s" + "|");
		
		if (Main.permutation_type == "1") { System.out.println(choice.get(0)+"         "); }
		if (Main.permutation_type == "2") { System.out.println(choice.get(1)); }
		if (Main.permutation_type == "3") { System.out.println(choice.get(2)+"  "); }
		if (Main.permutation_type == "4") { System.out.println(choice.get(3)+" "); }
		if (Main.permutation_type == "5") { System.out.println(choice.get(4)+" "); }
		if (Main.permutation_type == "6") { System.out.println(choice.get(5)+"   "); }
		if (Main.permutation_type == "7") { System.out.println(choice.get(6)+"    "); }
		if (Main.permutation_type == "8") { System.out.println(choice.get(7)+" "); }
		
		for (int ind = 0; ind < Main.result_array.size(); ind++) {
			System.out.println("%8s" + "| ");
			DecimalFormat df = new DecimalFormat("#.0000000");
			System.out.println(df.format(Main.result_array.get(ind)) + "%4s");
		}
		
	}
	
}
