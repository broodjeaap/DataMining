package nl.hr.datamining.broodjeaap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Pearson {
	
	static int higherCount = 0;
	
	public static double distance(UserRatings user1, UserRatings user2){
		Arrays.sort(user1.getProductID()); //lazy
		Arrays.sort(user2.getProductID());
		Set<Integer> union = userProductsUnion(user1,user2);
		
		int[] products1 = user1.getProductID();
		int[] ratings1 = user1.getRatings();
		int[] sharedRatings1 = new int[union.size()];
		int count = 0;
		for(int a = 0;a < products1.length;++a){
			if(union.contains(products1[a])){
				sharedRatings1[count++] = ratings1[a];
			}
		}
		int[] products2 = user2.getProductID();
		int[] ratings2 = user2.getRatings();
		int[] sharedRatings2 = new int[union.size()];
		count = 0;
		for(int a = 0;a < products2.length;++a){
			if(union.contains(products2[a])){
				sharedRatings2[count++] = ratings2[a];
			}
		}
		return distance(sharedRatings1,sharedRatings2);
	}
	
	public static Set<Integer> userProductsUnion(UserRatings user1, UserRatings user2){
		Set<Integer> set1 = new HashSet<>();
		Set<Integer> set2 = new HashSet<>();
		int[] user1Ratings = user1.getProductID();
		int[] user2Ratings = user2.getProductID();
		for(int a = 0;a < user1Ratings.length;++a){
			set1.add(user1Ratings[a]);
		}
		for(int a = 0;a < user2Ratings.length;++a){
			set2.add(user2Ratings[a]);
		}
		set1.retainAll(set2);
		return set1;
	}
	
	
	public static double distance(int[] ratings1, int[] ratings2){
		int productSum = 0;
		for(int a = 0;a < ratings1.length;++a){
			productSum += ratings1[a] * ratings2[a];
		}
		int sum1 = 0;
		int sumPow1 = 0;
		for(int a = 0;a < ratings1.length;++a){
			sum1 += ratings1[a];
			sumPow1 += ratings1[a] * ratings1[a];
		}
		int sum2 = 0;
		int sumPow2 = 0;
		for(int a = 0;a < ratings2.length;++a){
			sum2 += ratings2[a];
			sumPow2 += ratings2[a] * ratings2[a];
		}
		/* debug
		System.out.println("X: "+sum1);
		System.out.println("Y: "+sum2);
		System.out.println("XY: "+productSum);
		System.out.println("Pow X: "+sumPow1);
		System.out.println("Pow Y: "+sumPow2);
		System.out.println("Pow X: "+sumPow1);
		*/
		double top = (ratings1.length*productSum-(sum1*sum2));
		double bottom = Math.sqrt((ratings1.length*sumPow1 - sum1*sum1)*(ratings2.length*sumPow2 - sum2*sum2));
		double result = top/bottom;
		if(result > 0.9d){
			higherCount++;
		}
		return result;
	}
}
