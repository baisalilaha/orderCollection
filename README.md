# orderCollection
To make this application up and running we need below components:  
__MongoDB__  
__Tomcat__  
__JDK 1.8__  
__Jmeter(For API's unit test with 200 output)__  
  
Limitations : 	
--------------
All values for Width, Length, Height are in inches<br>  
Zip Code - 5 digits long	<br>
Phone number - 10 digits long(No Special character)<br>		
State - US state Only	<br>

	
To start this application please follow below steps:	<br>

Restore MongoDB database - orders	<br>
			  Collection - Orders	<br>
			  Collection - Product	<br>
			  
`mongorestore --collection Orders --db orders {Your_machines_path_to_OrderCollection_folder}/orders/Orders.bson`		
`mongorestore --collection Product --db orders {Your_machines_path_to_OrderCollection_folder}/orders/Product.bson`		

Run below command :		
`mongo`		
`use orders`		
`db.createUser( { user: "user", pwd: "pass123", roles: [ "readWrite"] } );`		

Below DB configs has been used in this Application		  
			 `db_host =127.0.0.1`
			  `db_port =27017`
			  `db_username =user`
			  `db_password =pass123`

If your DB **user_name/password/host/port** is different, replace them in below file --					
**src/main/resources/config.properties**		
			
Run below Command on command line:		
			
			`cd {Your_machines_path_to_OrderCollection_folder}/OrderCollection`
			`mkdir /var/frengo/`
			`mkdir /var/frengo/OrderCollection`  		
			`cp src/main/resources/config.properties to /var/frengo/OrderCollection/config.properties`  		
		
This is a WEB Application, and it runs on Tomcat server. Next we need to copy the .war file to Tomcat webapps folder:		
			`cp target/collection.war /{tomcat_server_root_directory_path}/webapps/`		
		
Add below line in server.xml file of Tomcat -- 		
			`<!--`		
				`<Valve className="org.apache.catalina.authenticator.SingleSignOn" />`			
			`-->`			
			`<Context docBase="{tomcat_server_root_directory_path}/webapps/collection" path="/" reloadable="true" override="true"/>`			
					
NB :- If you already have any other application running as path "/", please change that as well, since this will create a conflict		
 		
Stop Tomcat Server -(if it was already running)		
			`/{tomcat_server_root_directory_path}/bin/catalina stop`		
		
Start Tomcat server		
			`/{tomcat_server_root_directory_path}/bin/catalina start`		
		
Paste below link on browser :		 
			`http://localhost:8080/products.html#`		
			
and .....   voila !!!		
		
Any log related to this application is written to the catalina.out file.		