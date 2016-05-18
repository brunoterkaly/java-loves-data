// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.CassandraJava;

// Adding the import statement
import com.datastax.driver.core.*;


public class App 
{

    static private Cluster cluster = null;
    static private Session session = null;
	
    public static void main( String[] args )
    {
        // cassandra.yaml
        // rpc_address: 0.0.0.0
        // broadcast_rpc_address: [ put your ip address here ]  [ this is the public ip address ]
        // storage_port: 8000
        // ssl_storage_port: 8001

        
            
        
        try {
            // Connect to Cassandra
            connect("[ put your ip address here ]");
            // Create keyspace and table
            createSchema();
            // Load table with data
            loadData();
            // Query table using "where" clause
            querySchema();
            // cleanup and close
            close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Connect to Cassandra running in Azure
    public static void connect(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n",
        metadata.getClusterName());
        for ( Host host : metadata.getAllHosts() ) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
            host.getDatacenter(), host.getAddress(), host.getRack());
        }
        session = cluster.connect();
    }
    // Create the keyspace and tables
    public static void createSchema() {
        session.execute("CREATE KEYSPACE IF NOT EXISTS simplex WITH replication " +
         "= {'class':'SimpleStrategy', 'replication_factor':3};");
        session.execute(
                "CREATE TABLE IF NOT EXISTS simplex.songs (" +
                "id uuid PRIMARY KEY," +
                "title text," +
                "album text," +
                "artist text," +
                "tags set<text>," +
                "data blob" +
                ");");
        session.execute(
                "CREATE TABLE IF NOT EXISTS simplex.playlists (" +
                "id uuid," +
                "title text," +
                "album text, " +
                "artist text," +
                "song_id uuid," +
                "PRIMARY KEY (id, title, album, artist)" +
                ");");
    }
    // load the data
    public static void loadData() {
    
        session.execute(
                "INSERT INTO simplex.songs (id, title, album, artist, tags) " +
                "VALUES (" +
                "756716f7-2e54-4715-9f00-91dcbea6cf50," +
                "'La Petite Tonkinoise'," +
                "'Bye Bye Blackbird'," +
                "'Joséphine Baker'," +
                "{'jazz', '2013'})" +
                ";");
                
                session.execute(
                "INSERT INTO simplex.playlists (id, song_id, title, album, artist) "
                +
                "VALUES (" +
                "2cc9ccb7-6221-4ccb-8387-f22b6a1b354d," +
                "756716f7-2e54-4715-9f00-91dcbea6cf50," +
                "'La Petite Tonkinoise'," +
                "'Bye Bye Blackbird'," +
                "'Joséphine Baker'" +
                ");");
    }
    // Query the data
    public static void querySchema() {
        ResultSet results = session.execute("SELECT * FROM simplex.playlists " +
                        "WHERE id = 2cc9ccb7-6221-4ccb-8387-f22b6a1b354d;");
        System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "title",
                        "album", "artist",
                        "-------------------------------+-----------------------"
                        + "--------------------"));
        for (Row row : results) {
            System.out.println(String.format("%-30s\t%-20s\t%-20s",
                       row.getString("title"),
                       row.getString("album"), row.getString("artist")));
        }
        System.out.println();
    }
    // Close the connection
    public static void close() {
        cluster.close();
    }    
    
    


        
}












