import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Similarity {
	
	public static double cosine_sim_a(HashMap<String, Double> vec1, HashMap<String, Double> vec2) {
		double num = 0;
		double sum_sql1 = 0;
		double sum_sql2 = 0;
		
		if(vec1.size() > vec2.size()) {
			HashMap<String, Double> tmp = new HashMap<String, Double>();
			tmp = vec1;
			vec1 = vec2;
			vec2 = tmp;
		}
		
		Iterator iterator = vec1.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String word = entry.getKey().toString();
			double weight = vec1.get(word);
			
			if(vec2.containsKey(word)) {
				num += weight * vec2.get(word);
			}
			else {
				num += weight * 0;
			}
			sum_sql1 += vec1.get(word) * vec1.get(word);
		}
		
		iterator = vec2.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String word = entry.getKey().toString();
			double weight = vec2.get(word);
			sum_sql2 += vec2.get(word) * vec2.get(word);		
		}
		
		return (num / Math.sqrt(sum_sql1 / sum_sql2));
	}
	
	public static double dice_sim_a(HashMap<String, Double> vec1, HashMap<String, Double> vec2) {
		double num = 0;
		double sum_sql1 = 0;
		double sum_sql2 = 0;
		
		if(vec1.size() > vec2.size()) {
			HashMap<String, Double> tmp = new HashMap<String, Double>();
			tmp = vec1;
			vec1 = vec2;
			vec2 = tmp;
		}
		
		Iterator iterator = vec1.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String word = entry.getKey().toString();
			double weight = vec1.get(word);
			
			if(vec2.containsKey(word)) {
				num += weight * vec2.get(word);
			}
			else {
				num += weight * 0;
			}
			sum_sql1 += vec1.get(word) * vec1.get(word);
		}
		
		iterator = vec2.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String word = entry.getKey().toString();
			double weight = vec2.get(word);
			sum_sql2 += vec2.get(word) * vec2.get(word);		
		}
		
		return (2 * num / (sum_sql1 + sum_sql2));
	}
}
