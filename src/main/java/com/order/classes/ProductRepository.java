package com.order.classes;

/**
 * A class for all DB operation
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.*;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ProductRepository{
	public static void main(String[] args){
		ProductRepository or = new ProductRepository();
		DbConfig.configLoader();
		
		//Validate all get query
		System.out.println(or.getListOfProducts().toString());
		
		String id = Long.toString(index + 1);
		
		//Create validation
		Map<String, String> p = new HashMap<String, String>();
		p.put("product_id", id);
		p.put("name", "Dryer");
		p.put("description", "Description");
		p.put("width", "10");
		p.put("weight", "12");
		p.put("height", "11");
		p.put("length","11");
		p.put("value", "50");
		or.createProduct(p);
		
		//Validate creation
		System.out.println(or.findOneProducts(id).toString());

		//UPdate Query Validation
		Map<String, String> q = new HashMap<String, String>();
		q.put("name", "Dryer123");
		q.put("description", "Description");
		q.put("width", "10");
		q.put("weight", "12");
		q.put("height", "11");
		q.put("length","11");
		q.put("value", "50");
		or.updateProduct(id, q);
		
		//Validate
		System.out.println(or.findOneProducts(id).toString());
		
		//Delete it
		or.deleteProduct(id);
		
		//Validate
		System.out.println(or.findOneProducts(id).toString());
	}
	
	//index
	private static long index = 0;
	
	/**
	 * 
	 * @return List of products
	 */
	public List<Map<String, String>> getListOfProducts(){
		List<Map<String, String>> products = new ArrayList<Map<String, String>>();
		System.out.println("started .........");
		try{
			MongoDatabase database = DbConfig.getDbInstance().getDatabase("orders");
			MongoCollection<Document> collection = database.getCollection("Product");

			FindIterable<Document> documents = collection.find();
			for(Document doc: documents){
				Map<String, String> p = new HashMap<String, String>();
				String id = doc.getString("product_id");
				if(Long.parseLong(id) > index){
					index = Integer.parseInt(id);
				}
				p.put("product_id", id);
				p.put("name", doc.get("name").toString());
				p.put("description", doc.getString("description"));
				products.add(p);
			}
			System.out.println("Got all elements");
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(new Date());
		}
		return products;
	}
	
	/**
	 * Get a product matching id
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> findOneProducts(String id){
		List<Map<String, String>> products = new ArrayList<Map<String, String>>();
		System.out.println("retrieving details for product_id " + id );
		try{
			MongoDatabase database = DbConfig.getDbInstance().getDatabase("orders");
			MongoCollection<Document> collection = database.getCollection("Product");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("product_id", id);

			FindIterable<Document> documents = collection.find(searchQuery);
			for(Document doc: documents){
				Map<String, String> p = new HashMap<String, String>();
				p.put("product_id", doc.getString("product_id"));
				p.put("name", doc.get("name").toString());
				p.put("description", doc.getString("description"));
				p.put("width", doc.getString("width"));
				p.put("weight", doc.getString("weight"));
				p.put("height", doc.getString("height"));
				p.put("length",doc.getString("length"));
				p.put("value", doc.getString("value"));
				p.put("currency", doc.getString("currency"));
				products.add(p);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(new Date());
		}
		return products;
	}
	
	/**
	 * Enter an new product details in DB
	 * @param id
	 * @param inputs
	 */
	public boolean createProduct(Map<String, String> inputs){
		boolean success = true;
		try{
			MongoDatabase database = DbConfig.getDbInstance().getDatabase("orders");
			MongoCollection<Document> collection = database.getCollection("Product");

			
			Document insertQuery = new Document();
			for(Entry<String, String> e : inputs.entrySet()){
				insertQuery.put(e.getKey(), e.getValue());
			}
			long id = index + 1;
			insertQuery.put("product_id", Long.toString(id));
			
			collection.insertOne(insertQuery);
			System.out.println("Inserted and new product " + id);
			
		}catch(Exception ex){
			success = false;
			ex.printStackTrace();
			System.out.println(new Date());
		}
		return success;
	}
	
	/**
	 * Update an existing product
	 * @param id
	 * @param inputs
	 */
	public boolean updateProduct(String id, Map<String, String> inputs){
		boolean success = true;
		System.out.println("Updating for product id " + id);
		try{
			MongoDatabase database = DbConfig.getDbInstance().getDatabase("orders");
			MongoCollection<Document> collection = database.getCollection("Product");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("product_id", id);
			
			BasicDBObject updateQuery = new BasicDBObject();
			for(Entry<String, String> e : inputs.entrySet()){
				updateQuery.put(e.getKey(), e.getValue());
			}
			Bson update = new Document("$set", updateQuery);
			collection.findOneAndUpdate(searchQuery, update);
			
		}catch(Exception ex){
			success = false;
			ex.printStackTrace();
			System.out.println(new Date());
		}
		return success;
	}
	
	/**
	 * Delete a product matching id
	 * @param id
	 */
	public boolean deleteProduct(String id){
		boolean success = true;
		System.out.println("Deleting");
		try{
			MongoDatabase database = DbConfig.getDbInstance().getDatabase("orders");
			MongoCollection<Document> collection = database.getCollection("Product");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("product_id", id);

			collection.deleteOne(searchQuery);
			System.out.println("deleted product " + id);
			
		}catch(Exception ex){
			success = false;
			ex.printStackTrace();
			System.out.println(new Date());
		}
		return success;
	}
}
