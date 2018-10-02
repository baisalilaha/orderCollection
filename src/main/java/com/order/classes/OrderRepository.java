package com.order.classes;

import java.util.*;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class OrderRepository{
	
	public static void main(String[] args){
		OrderRepository or = new OrderRepository();
		ProductRepository p = new ProductRepository();
		DbConfig.configLoader();
		
		List<Map<String, String>> all_prod = p.getListOfProducts();
		String product_id = "0";
		if(all_prod.size() > 1){
			product_id = all_prod.get(0).get("product_id");
		}	
		
		System.out.println(or.getAllOrder(product_id).toString());
		
		//Create a new one
		Map<String, String> o = new HashMap<String, String>();
		o.put("recipientName", "recipientName");
		o.put("streetAddress", "streetAddress");
		o.put("city", "city");
		o.put("state", "state");
		o.put("zipCode", "zipCode");
		o.put("phoneNumber", "phoneNumber");
		o.put("quantity", "quantity");
		or.createOrder(product_id, o);
		
		//Validate
		System.out.println(or.getAllOrder(product_id).toString());
	}
	
	/**
	 * Get all orders related to the product
	 * @param product_id
	 * @return
	 */
	public List<Map<String, String>> getAllOrder(String product_id){
		List<Map<String, String>> orders = new ArrayList<Map<String, String>>();
		System.out.println("starting..... " );
		try{
			MongoDatabase database = DbConfig.getDbInstance().getDatabase("orders");
			MongoCollection<Document> collection = database.getCollection("Orders");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("product_id", product_id);
			
			FindIterable<Document> documents = collection.find(searchQuery);
			for(Document doc: documents){
				Map<String, String> o = new HashMap<String, String>();
				o.put("recipientName", doc.get("recipientName").toString());
				o.put("streetAddress", doc.getString("streetAddress"));
				o.put("city", doc.getString("city"));
				o.put("state", doc.getString("state"));
				o.put("zipCode", doc.getString("zipCode"));
				o.put("phoneNumber", doc.getString("phoneNumber"));
				o.put("quantity", doc.getString("quantity"));
				orders.add(o);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(new Date());
		}
		return orders;
	}
	
	
	
	/**
	 * Enter an new product details in DB
	 * @param id
	 * @param inputs
	 */
	public boolean createOrder(String product_id, Map<String, String> inputs){
		boolean success = true;
		System.out.println("creating a new order");
		try{
			MongoDatabase database = DbConfig.getDbInstance().getDatabase("orders");
			MongoCollection<Document> orders = database.getCollection("Orders");
			MongoCollection<Document> products = database.getCollection("Product");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("product_id", product_id);
			
			FindIterable<Document> product = products.find(searchQuery).limit(1);
			if(product.first() != null){
				Document insertQuery = new Document();
				for(Entry<String, String> e : inputs.entrySet()){
					insertQuery.put(e.getKey(), e.getValue());
				}
				
				insertQuery.put("product_id", product_id);
				
				orders.insertOne(insertQuery);
			}
			System.out.println("Created a new Order");
			
		}catch(Exception ex){
			success = false;
			ex.printStackTrace();
			System.out.println(new Date());
		}
		return success;
	}
	
}