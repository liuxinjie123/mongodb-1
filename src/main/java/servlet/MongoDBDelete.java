import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MongoDBDelete extends HttpServlet 
{
	private MongoDBClient db = new MongoDBClient();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	try
    	{
    		db.deleteAll();
    		request.setAttribute("msg", "Deleted");
    	}
		catch (Exception e) {
			request.setAttribute("msg", e.getMessage());
			e.printStackTrace(System.err);
		}

		response.setContentType("text/html");
        response.setStatus(200);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}