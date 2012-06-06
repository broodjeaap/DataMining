package nl.hr.datamining.broodjeaap;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Database {
	private static Mongo m;
	private static DB db;
	static{
		try {
			m = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		db = m.getDB("DataMining");
	}
	
	public static void putUsers(Map<Integer, UserRatings> users){
		Set<Integer> ids = users.keySet();
		DBCollection coll = db.getCollection("Users");
		for(Integer id : ids){
			BasicDBObject user = new BasicDBObject();
			user.put("id", users.get(id).getId());
			BasicDBObject productRatings = new BasicDBObject();
			int[] products = users.get(id).getProductID();
			int[] ratings = users.get(id).getRatings();
			for(int a = 0;a < products.length;++a){
				productRatings.put(String.valueOf(products[a]), ratings[a]);
			}
			user.put("ratings", productRatings);
			BasicDBObject recommendations = new BasicDBObject();
			Map<Double, Integer> allRecommendations = users.get(id).getRecommendations();
			int count = 1;
			for(Double d : allRecommendations.keySet()){
				recommendations.put(String.valueOf(count), allRecommendations.get(d));
				if(count++ >= 20){
					break;
				}
			}
			user.put("recommendations", recommendations);
			coll.insert(user);
		}
	}
	
	public static Map<Integer, UserRatings> getUsers(){
		Map<Integer, UserRatings> users = new HashMap<>();
		DBCollection coll = db.getCollection("Users");
		DBCursor cursor = coll.find();
		return users;
	}
}
