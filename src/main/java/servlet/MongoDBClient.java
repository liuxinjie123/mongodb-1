import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.cloudfoundry.runtime.env.CloudEnvironment;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.util.JSON;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class MongoDBClient
{

    public MongoDBClient()
    {

    }

    public int addEntry(String jsonString) throws Exception, ParseException
    {

        JSONParser parser = new JSONParser();
        int result = 0;

        try
        {

            //get service connection and initialize mongoclient
            String connURL = getServiceURI();
            MongoClient mongo = new MongoClient(new MongoClientURI(connURL));

            //initialize database and collection
            DB db = mongo.getDB("db");
            DBCollection table = db.getCollection("books");

            Object obj = parser.parse(jsonString);

            
            //check if json is an array or not
            if(obj.getClass().getName().matches(".*[JSONArray]")){

                JSONArray objArr = (JSONArray) obj;
                BulkWriteOperation builder = table.initializeOrderedBulkOperation();

                for(int i=0; i < objArr.size();i++)
                {

                    BasicDBObject entry = (BasicDBObject) JSON.parse(objArr.get(i).toString());
                    builder.insert(entry);
                }

                BulkWriteResult wr = builder.execute(); // bulk insert operation

                //Returns true if the write was acknowledged.
                if(wr.isAcknowledged())
                    result = objArr.size();
                
            }
            else
            { 

                BasicDBObject entry = (BasicDBObject) JSON.parse(obj.toString());
                WriteResult wr = table.insert(entry);

                //Returns true if the write was acknowledged.
                if(wr.wasAcknowledged())
                    result = 1; 
            }           
            
        }
        catch(ParseException pe)
        {

            pe.printStackTrace();

        }
        catch(Exception e)
        {

            e.printStackTrace();
        }

        return result;
    }


    public List<String> getAll() throws Exception
    {
        try
        {

            //get service connection and initialize mongoclient
            String connURL = getServiceURI();
            MongoClient mongo = new MongoClient(new MongoClientURI(connURL));

            DB db = mongo.getDB("db");
            DBCollection table = db.getCollection("books");

            //get all entries
            DBCursor cursor = table.find();

            List<String> entries = new ArrayList<String>();

            while (cursor.hasNext()) 
            {
                entries.add(cursor.next().toString());
            }

            return entries;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteAll() throws Exception
    {
         try
         {
            String connURL = getServiceURI();
            MongoClient mongo = new MongoClient(new MongoClientURI(connURL));

            DB db = mongo.getDB("db");
            DBCollection table = db.getCollection("books");

            table.drop();
        }
        
        catch (Exception e) 
        {
            e.printStackTrace();
        }


    }


    protected static String getServiceURI() throws Exception
    {
        CloudEnvironment environment = new CloudEnvironment();
        if ( environment.getServiceDataByLabels("mongodb").size() == 0 ) 
        {
            throw new Exception( "No MongoDB service is bound to this app!!" );
        } 

        Map credential = (Map)((Map)environment.getServiceDataByLabels("mongodb").get(0)).get( "credentials" );
     
        return (String)credential.get( "url" );
      }

}

