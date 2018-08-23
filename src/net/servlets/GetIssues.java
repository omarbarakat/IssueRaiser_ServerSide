package net.servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbms.Convertor;
import dbms.DBConnector;
import model.Constants;


/**
 * 
 * @author omar barakat
 *
 * example requests:
 * - http://localhost:8080/ServerSide/GetIssues?retrieve-by=created&key=0
 */

@WebServlet("/GetIssues")
public class GetIssues extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetIssues() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			switch(request.getParameter("retrieve-by")) {
				case "created": {
					String key = request.getParameter("key");
					ResultSet issues = getIssuesCreatedBy(key);
					response.getWriter().append(Convertor.convertToJSON(issues).toString());
					break;
				}
				case "followed": {
					String key = (String)((request.getSession(false) != null)? request.getSession().getAttribute("uid"):Constants.NULL_USER_ID);
					ResultSet issues = getIssuesFollowedBy(key);
					response.getWriter().append(Convertor.convertToJSON(issues).toString());
					break;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	// RETRIEBING METHODS
	
	// get issues created by user
	private ResultSet getIssuesCreatedBy(String key) throws SQLException {
		DBConnector conn = DBConnector.getInstance();
		PreparedStatement stmt = conn.createPreparedStatement(
				"select * from politics_app.issue where user_id=(?)"
				);
		stmt.setString(1, key);
		return conn.execSQL(stmt);
	}
	
	// get issues using ids
	private ResultSet getIssues(ResultSet keys) throws SQLException {
		DBConnector conn = DBConnector.getInstance();
		PreparedStatement stmt = conn.createPreparedStatement(
				"select * from politics_app.issue where issue_id in (?)"
				);
		stmt.setArray(1, keys.getArray(1));
		return conn.execSQL(stmt);
	}
	
	// get issues followed by user
	private ResultSet getIssuesFollowedBy(String key) throws SQLException {
		DBConnector conn = DBConnector.getInstance();
		PreparedStatement stmt = conn.createPreparedStatement(
				"select issue_id from politics_app.user_follows where user_id=(?)"
				);
		stmt.setString(1, key);
		ResultSet issueIDs = conn.execSQL(stmt);
		return this.getIssues(issueIDs);
	}
	
	// get recent issues in a city
	private ResultSet getIssuesRecentIn(String city, String from, String to) throws SQLException {
		return null;
	}
	
	// get recent issues globally
	private ResultSet getIssuesHotGlobal() throws SQLException {
		return null;
	}
	
}
