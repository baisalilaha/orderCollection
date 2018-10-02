package com.order.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * Servlet class for product related operations
 * @author baisalilaha
 *
 */

@SuppressWarnings("serial")
@WebServlet(name="Order", urlPatterns={"/order"})
public class OrderController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try{
			String id = request.getParameter("product_id");

			OrderRepository repo = new OrderRepository();
			List<Map<String, String>> orders = new ArrayList<Map<String, String>>();

			if(id != null){
				try{
					Integer.parseInt(id);
					orders = repo.getAllOrder(id); //Get all of them
				}catch(Exception ex){
					throw ex;
				}
			}
			System.out.print(JSONArray.toJSONString(orders));
			
			PrintWriter out = response.getWriter();
			response.setStatus(HttpStatus.SC_OK);
			response.setHeader("Content-type", "application/json");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			out.println(JSONArray.toJSONString(orders));
		}catch(Exception ex){
			try {
				throw new Exception("Get call failure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//TODO need to add validation for other two put and post

	//Form action does not support PUT so adding a "id" with POST to do PUT operation  
	@SuppressWarnings({ "unchecked", "static-access" })
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
			OrderRepository repo = new OrderRepository();
			boolean success = false;
			Map<String, String> map = new HashMap<String, String>();
			if(json != null){
				Set<String> keys = new HashSet<String>();
				//Validate inputs
				keys.add("recipientName");
				keys.add("streetAddress");
				keys.add("city");
				keys.add("state");
				keys.add("zipCode");
				keys.add("phoneNumber");
				keys.add("quantity");
				keys.add("id");

				String[] inputs = json.split("\\&");
				for(String i : inputs){
					String[] element = i.split("=");
					String key = element[0];
					String value = element[1];
					if(keys.contains(key) && !(value.toString().contains("$"))){
						
						map.put(key, URLDecoder.decode(value.toString(), "UTF-8"));
					}
				}
				if((map.get("id") != null) && (map.get("phoneNumber").length() == 10) && (map.get("zipCode").length() == 5) && (map.get("state").length() == 2)){
					//Validate inputs
					try{
						Long.parseLong(map.get("id"));
						Long.parseLong(map.get("quantity"));
						Long.parseLong(map.get("zipCode"));
						Long.parseLong(map.get("phoneNumber"));
						//This is a insert call
						String id = map.get("id");
						map.remove("id");
						success = repo.createOrder(id, map);
					}catch(Exception ex){
						throw new Exception("Number Format Error");
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