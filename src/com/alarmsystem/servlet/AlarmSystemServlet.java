package com.alarmsystem.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Servlet implementation class AlarmSystemServlet
 */
@WebServlet("/status")
public class AlarmSystemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlarmSystemServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Cluster cluster = Cluster.builder().addContactPoints("192.168.72.101","192.168.72.102").build();
		
		Session session = cluster.connect();
		
		String homeId = request.getParameter("h_id");
		String queryString = "SELECT datetime, event, code_used FROM home_security.activity WHERE home_id = '" + homeId + "'";
		String queryString_hometable = "SELECT contact_name, address, city, state, zip FROM home_security.home WHERE home_id = '" + homeId + "'";
		
		ResultSet result = session.execute(queryString);
		ResultSet result_hometable = session.execute(queryString_hometable);
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html>");
		out.println("<head><title>Alarm System Status</title></head>");
		out.println("<body style=\"font:14px verdana,arial,sans-serif\">");
		out.println("<h1>Alarm System Status</h1>");
		out.println("Enter your home id to see the most recent activity for the system");	
		out.println("<p>&nbsp;</p>");
		out.println("<form id=\"form1\" name=\"form1\" method=\"get\" action=\"\">");
		out.println("Home id: ");
		out.println("<input type=\"text\" name=\"h_id\" id=\"h_id\" />");
		out.println("<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Submit\"/>");
		out.println("</form>");
		out.println("<p>&nbsp;</p>");
	
		if(request.getParameter("h_id") == null)
		{
			// blank
		}
		else if(result.isExhausted())
		{
			out.println("<p>&nbsp;</p>");
			out.println("<b>Sorry</b>, no results for home id " + request.getParameter("h_id"));
		}		
		else
		{
			for (Row row : result_hometable)
			{
			   out.println("<p>");
			   out.println("<b>" + row.getString("contact_name") + "</b>, ");
			   out.println(row.getString("address") + ", " + row.getString("city") + ", " + row.getString("state") + ", " + row.getString("zip")); 
			   out.println("</p>");
			}
			
			out.println("<table style=\"font:14px verdana,arial,sans-serif\" cellpadding=\"4\">");
		
			for (Row row : result)
			{
			   out.println("<tr>");
			   out.println("<td>" + row.getString("event") + "</td>");  
			   out.println("<td>" + row.getDate("datetime") + "</td>");
			   out.println("</tr>");
			}
			out.println("</table>");			
		}

		out.println("</body></html>");
	}

}
