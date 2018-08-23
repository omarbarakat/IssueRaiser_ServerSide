package net.servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dbms.DBConnector;
import model.Constants;

@WebServlet("/CreateIssue")
public class CreateIssue extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public CreateIssue() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject output = new JSONObject();
		String text = request.getParameter("text");
		String issueLevel = request.getParameter("level"); 
		int cityId = Integer.parseInt( request.getParameter("city_id") );
		
		if(request.getSession(false) == null) {
			output.put("status", "fail");
			output.put("reason", "not logged in");
		} else if(text.length() > Constants.MAX_TEXT_LEN) {
			output.put("status", "fail");
			output.put("reason", "text too long. max length = "+Integer.toString(Constants.MAX_TEXT_LEN));
		} else if( !Constants.ISSUE_STATUS.contains( issueLevel) ){
			output.put("status", "fail");
			output.put("reason", "invalid issue status");
		} else {
			try {
				registerIssue( text, issueLevel, cityId, (int) request.getSession(false).getAttribute("uid") );
				output.put("status", "success");
			} catch (SQLException e) {
				output.put("status", "fail");
				output.put("reason", "SQL exception");
			}
		}
		response.getWriter().append(output.toString());
	}

	private void registerIssue(String text, String issueLevel, int cityId, int userId) throws SQLException {
		DBConnector conn = DBConnector.getInstance();
		PreparedStatement stmt = conn.createPreparedStatement(
				"insert into politics_app.issue (text, issue_level, city_id, user_id, image_id) "
				+ "values ( (?), (?), (?), (?), "+Integer.toString(Constants.NULL_IMAGE_ID)+" )"
				);
		stmt.setString(1, text);	stmt.setString(2, issueLevel);	stmt.setInt(3, cityId);	stmt.setInt(3, userId);
		conn.execSQL(stmt);
	}
}
