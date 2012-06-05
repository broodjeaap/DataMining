package nl.hr.datamining.broodjeaap;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Main {
	
	static int count = 0;
	static long totalTime = 0;
	
	public static void main(String[] args){
		Map<Integer, UserRatings> users = new TreeMap<>();
		try{
			FileInputStream fstream = new FileInputStream("u.data");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				String[] values = strLine.split("\t");
				int userID = Integer.parseInt(values[0]);
				UserRatings user = users.get(userID);
				if(user == null){
					user = new UserRatings(userID,Integer.parseInt(values[1]),Integer.parseInt(values[2]));
				} else {
					user.addMovieRating(Integer.parseInt(values[1]),Integer.parseInt(values[2]));
				}
				users.put(user.getId(), user);
			}
			in.close();
		}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
				e.printStackTrace();
		}
		System.out.println(Pearson.distance(users.get(1),users.get(2)));
		users.get(1).setRecommendations(getRecommendations(users.get(300),users));
		Map<Double, Integer> rec = users.get(1).getRecommendations();
		for(Double d : rec.keySet()){
			System.out.println("Film "+rec.get(d)+": "+d);
		}
		long finalStart = System.currentTimeMillis();
		for(int id : users.keySet()){
			UserRatings user = users.get(id);
			//long userStart = System.currentTimeMillis();
			user.setRecommendations(getRecommendations(user, users));
			//System.out.println("Created recommendations for user "+user.getId()+" in "+(System.currentTimeMillis() - userStart)+"ms");
		}
		System.out.println("Created recommendations for all user in "+(System.currentTimeMillis() - finalStart)+"ms");
		System.out.println("Number of distances above 0.9: "+Pearson.higherCount);
		//Database.putUsers(users);
		System.out.println(count+" "+(totalTime / count));
	}
	
	public static Map<Double, Integer> getRecommendations(UserRatings user, Map<Integer, UserRatings> users){
		Map<Integer, Double> ranking = new HashMap<>();
		for(int otherId : users.keySet()){
			UserRatings otherUser = users.get(otherId);
			if(user == otherUser){
				continue;
			}
			count++;
			//userRankings.put(Pearson.distance(user, otherUser), otherUser);
			long start = System.nanoTime();
			double distance = Pearson.distance(user, otherUser);
			totalTime += System.nanoTime() - start;
			int[] products = otherUser.getProductID();
			int[] ratings = otherUser.getRatings();
			for(int i = 0;i < products.length;++i){
				Double rank = ranking.get(products[i]);
				if(rank == null){
					rank = new Double(0);
				}
				rank -= distance * ratings[i];
				ranking.put(i, rank);
			}
		}
		Map<Double, Integer> sortedByRank = new TreeMap<>();
		for(int i : ranking.keySet()){
			sortedByRank.put(ranking.get(i), i);
		}
		return sortedByRank;
	}
	
	
}
