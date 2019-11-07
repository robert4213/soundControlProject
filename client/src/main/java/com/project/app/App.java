package com.project.app;

import static org.eclipse.leshan.LwM2mId.DEVICE;
import static org.eclipse.leshan.LwM2mId.SECURITY;
import static org.eclipse.leshan.LwM2mId.SERVER;
import static org.eclipse.leshan.client.object.Security.noSecBootstap;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.leshan.LwM2m;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.model.StaticModel;
import org.eclipse.leshan.core.request.RegisterRequest;
import org.eclipse.leshan.core.response.RegisterResponse;

import com.project.model.SqlDB;


public class App 
{
    private final static String[] modelPaths = new String[] { "3303.xml" ,"23333.xml"};

    private static final int OBJECT_ID_TEMPERATURE_SENSOR = 3303;
    
    private static String endpoint = "Client1";
    
    private static String uri = "coap://localhost:" + LwM2m.DEFAULT_COAP_PORT;

	private static Tempreture temp = new Tempreture();
    
    public static void main( String[] args )
    {
    	try {
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    		LocalDateTime now = LocalDateTime.now();  
    		String time = dtf.format(now);  
    		
    		SqlDB s = new SqlDB();
     		Connection c = s.getC();
 			Statement stm = c.createStatement();
 			int id;
 			String sql;
 			ResultSet rs;
 			try {
	 			sql = "SELECT ID "
	 					+ "FROM  Register "
	 					+ "WHERE ID = (SELECT MAX(ID) FROM Register);";
	 			rs = stm.executeQuery(sql);
	 			id = rs.getInt("ID")+1;
 			}catch(Exception e) {
 				e.printStackTrace();
 				id = 1;
 			}
 			
    		 
    		LeshanClient client = createAndStart();

			System.out.println("ID: "+id);
			sql = "INSERT INTO Register (Endpoint, Time,ID,Type) "+
			"VALUES('Client1','"+time+"',"+id+",'register');";
//			System.out.println(sql);
			stm.executeUpdate(sql);	
//			System.out.println(rs);
			c.commit();
    	
    	while(true) {
    		System.out.print("Do you want to finish?");
    		//Enter data using BufferReader 
            BufferedReader reader =  
                       new BufferedReader(new InputStreamReader(System.in)); 
             
            // Reading data using readLine 
            String name = reader.readLine();

            if(name.equalsIgnoreCase("y")) {
            	break;
            }
			TimeUnit.SECONDS.sleep(3);
    	}
    	
    	client.destroy(true);
    	
    	now = LocalDateTime.now();  
		time = dtf.format(now);  
    	id++;
    	sql = "INSERT INTO Register (Endpoint, Time,ID,Type) "+
    			"VALUES('Client1','"+time+"',"+id+",'deregister');";
//    			System.out.println(sql);
    			stm.executeUpdate(sql);	
//    			System.out.println(rs);
    			c.commit();
    	
    	
    	
    	sql = "SELECT * FROM Register";
		rs = stm.executeQuery(sql);
		System.out.println("DB result");
		while(rs.next()) {
			System.out.println("ID:" + rs.getInt("ID"));
			System.out.println("Endpoint:" + rs.getString("Endpoint"));
			System.out.println("Type:" + rs.getString("Type"));
			System.out.println("Time:" + rs.getString("Time")+"\n");
		}
		
		c.close();
    	
    	return;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return;
    	}
    }
    
    public static LeshanClient createAndStart() throws InterruptedException {
    	String localAddress = null;
        int localPort = 0;
    	
    	List<ObjectModel> models = ObjectLoader.loadDefault();
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));
        
        ObjectsInitializer initializer = new ObjectsInitializer(new StaticModel(models));

        initializer.setInstancesForObject(SECURITY, noSecBootstap(uri));
        initializer.setClassForObject(SERVER, Server.class);
		initializer.setInstancesForObject(23333, temp);
        initializer.setInstancesForObject(DEVICE, new MyDevice());
        initializer.setInstancesForObject(OBJECT_ID_TEMPERATURE_SENSOR, new RandomTemperatureSensor());
        List<LwM2mObjectEnabler> enablers = initializer.createAll();
        
        NetworkConfig coapConfig;
        File configFile = new File(NetworkConfig.DEFAULT_FILE_NAME);
        if (configFile.isFile()) {
            coapConfig = new NetworkConfig();
            coapConfig.load(configFile);
        } else {
            coapConfig = LeshanClientBuilder.createDefaultNetworkConfig();
            coapConfig.store(configFile);
        }
        
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        builder.setLocalAddress(localAddress, localPort);
        builder.setObjects(enablers);
        builder.setCoapConfig(coapConfig);
        final LeshanClient client = builder.build();
        
        client.start();

        return client;




	}
}
