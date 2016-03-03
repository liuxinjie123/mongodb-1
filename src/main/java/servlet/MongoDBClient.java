import java.util.*;
import org.cloudfoundry.runtime.env.*;
import java.io.*;
import com.mongodb.*;
import com.mongodb.util.*;

public class MongoDBClient
{

    public MongoDBClient()
    {

    }

    public boolean addEntry(String jsonString) throws Exception
    {
        BasicDBObject entry = (BasicDBObject)JSON.parse(jsonString);

        try{
            String connURL = getServiceURI();

            MongoClient mongo = new MongoClient(new MongoClientURI(connURL));
            DB db = mongo.getDB("db");
            DBCollection table = db.getCollection("books");
            WriteResult wr = table.insert(entry);

            //Returns true if the write was acknowledged.
            return wr.wasAcknowledged();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getEntry() throws Exception
    {
        try{
            String connURL = getServiceURI();

            MongoClient mongo = new MongoClient(new MongoClientURI(connURL));
            DB db = mongo.getDB("db");
            DBCollection table = db.getCollection("books");

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
         try{
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

