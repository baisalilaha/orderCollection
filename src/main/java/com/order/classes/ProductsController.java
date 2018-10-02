package com.order.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import net.minidev.json.JSONObject;

/**
 * Servlet class for product related operations
 * @author baisalilaha
 *
 */

@SuppressWarnings("serial")
@WebServlet(name="Product", urlPatterns={"/product"})
public class ProductsController extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		DbConfig.configLoader();
	}

	//GET method
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try{
			String id = request.getParameter("product_id");

			ProductRepository repo = new ProductRepository();
			List<Map<String, String>> prod = new ArrayList<Map<String, String>>();

			if(id == null){
				prod = repo.getListOfProducts(); //Get all of them 
			}else{
				try{
					Integer.parseInt(id);
					prod = repo.findOneProducts(id); //get only one for details
				}catch(Exception ex){
					throw ex;
				}
			}

			JSONObject msg = new JSONObject();
			for(Map<String, String> p : prod){
				msg.put(p.get("product_id"), p);
			}

			PrintWriter out = response.getWriter();
			response.setStatus(HttpStatus.SC_OK);
			response.setHeader("Content-type", "application/json");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			out.println(msg.toJSONString());
		}catch(Exception ex){
			try {
				throw new Exception("Get call failure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//Form action does not support PUT so adding a "id" with POST to do PUT operation  
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try{
			InputStream is = request.getInputStream();
			System.out.println(request.getMethod());

			String json = "";
			InputStreamReader reader = new InputStreamReader(is);
			char[] buffer = new char[10240];
			StringWriter writer = new StringWriter();

			int bytes_read;
			try {
				while ((bytes_read = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, bytes_read);
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to read the inputstream.  Please try again.");
			}

			json = writer.toString();

			System.out.println(json.toString());
			ProductRepository repo = new ProductRepository();
			boolean success = false;
			Set<String> keys = new HashSet<String>();
			//Validate inputs
			keys.add("product_id");
			keys.add("name");
			keys.add("description");
			keys.add("width");
			keys.add("height");
			keys.add("length");
			keys.add("weight");
			keys.add("value");
			keys.add("currency");
			
			Map<String, String> map = new HashMap<String, String>();
			if(json != null){
				
				//It seems Jquery DELETE call does not work properly .. making it a post call with different params
				if(json.contains("delete")){
					
					String[] inputs = json.split("\\&");
					for(String i : inputs){
						String[] element = i.split("=");
						String key = element[0];
						String value = element[1];
						if(key.equals("id")){
							success = repo.deleteProduct(value);
							break;
						}
					}	
				}else{
			
					String[] inputs = json.split("\\&");
					for(String i : inputs){
						String[] element = i.split("=");
						String key = element[0];
						String value = element[1];
						if(keys.contains(key) && !(value.toString().contains("$"))){
							String decoded = "";
							try{
								if(!key.equals("name") && !key.equals("description") && !key.equals("currency")){
									Float.parseFloat(value.toString());
								}
								if(value.contains(".")){
									value.replaceAll(".", ",");
								}
								decoded = URLDecoder.decode(value, "UTF-8"); //Looks like java decoder cant support ., changing it to ,
							}catch(Exception e){
								e.printStackTrace();
								throw new Exception("Number Format Error");
							}
							map.put(key, decoded.replaceAll(",", "."));
						}
					}
				}
	
				if(map.size() >= 7 ){
					if(map.get("product_id") != null){
						//This is a update call
						String id = map.get("product_id").toString();
						map.remove("product_id");
						success = repo.updateProduct(id, map);
					}else{
						//This is a update call
						success = repo.createProduct(map);
					}
				}
			}	

			if(success){
				response.setStatus(HttpStatus.SC_OK);
			}else{
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			response.setHeader("Content-type", "application/json");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
		}catch(Exception ex){
			try {
				throw new Exception("Post call failure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}