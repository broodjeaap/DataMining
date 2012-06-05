package nl.hr.datamining.broodjeaap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserRatings {
	private int id;
	private int[] productID = new int[10];
	private int productIDIndex = -1;
	private int[] ratings = new int[10];
	private int ratingsIndex = -1;
	private Map<Double, Integer> recommendations;
	
	
	public UserRatings(int id,int productID,int rating){
		this.id = id;
		this.addId(productID);
		this.addRating(rating);
	}
	
	public boolean addId(int i){
		if(productIDIndex+1 >= productID.length){
			int[] tmp = new int[productID.length/3+productID.length];
			for(int a = 0;a < productID.length;++a){
				tmp[a] = productID[a];
			}
			productID = tmp;
		}
		productID[++productIDIndex] = i;
		return true;
	}
	
	public boolean addRating(int i){
		if(ratingsIndex+1 >= ratings.length){
			 int[] tmp = new int[ratings.length/3+ratings.length];
			 for(int a = 0;a < ratings.length;++a){
				 tmp[a] = ratings[a];
			 }
			 ratings = tmp;
		}
		ratings[++ratingsIndex] = i;
		return true;
	}
	
	public boolean addMovieRating(int id,int rating){
		return (addId(id) && addRating(rating));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getRatingCount(){
		return ratingsIndex+1;
	}
	
	
	public int[] getProductID() {
		return Arrays.copyOfRange(productID, 0, productIDIndex);
	}

	public void setProductID(int[] productID) {
		this.productID = productID;
	}

	public int[] getRatings() {
		return Arrays.copyOfRange(ratings, 0, ratingsIndex);
	}

	public void setRatings(int[] ratings) {
		this.ratings = ratings;
	}

	public Map<Double, Integer> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(Map<Double, Integer> recommendations) {
		this.recommendations = recommendations;
	}

	public String toString(){
		int sum = 0;
		for(Integer i : ratings){
			sum += i;
		}
		return id+", "+ratings.length+" ratings, average: "+((double)sum / ratings.length);
	}
}
