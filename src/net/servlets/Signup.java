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

import org.json.JSONException;
import org.json.JSONObject;
import dbms.DBConnector;

@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public Signup() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		
		// Form sign-up
		if(request.getParameterMap().containsKey("form-signup")) {
			if(request.getSession(false) != null) {
				request.getSession().invalidate();
				json.put("action", "logout");
			}
			
			String userName = request.getParameter("uname");
			String passHash = request.getParameter("passhash");
			String email = request.getParameter("email");
			
			// is any of the information in use by another user
			try {
				if(userExists(userName, passHash, email)) {
					json.put("status", "fail");
					json.put("reason", "existing_user");
				} else {
					// Register
					registerUser(userName, passHash, email);
					// Login
					int user_id = LoginServlet.getUserID(userName, passHash);
					request.getSession().setAttribute("uid", user_id);
					// Log
					json.put("status", "success");
				}
			} catch (JSONException | SQLException e) {
				e.printStackTrace();
			}
			response.getWriter().append(json.toString());
		} else if(request.getParameterMap().containsKey("fb-signup")) {
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	// Helper functions
	private boolean userExists(String uname, String passHash, String email) throws SQLException {
		DBConnector conn = DBConnector.getInstance();
		PreparedStatement stmt = conn.createPreparedStatement(
				"select max(if(uname=(?), 1, 0)), max(if(pass_hash=(?), 1, 0)), max(if(email=(?), 1, 0))"
				+ "from politics_app.user_cred"
				);
		stmt.setString(1, uname);	stmt.setString(2, passHash);	stmt.setString(3, email);
		ResultSet rs = conn.execSQL(stmt);
		if(rs.getInt(1)==1 || rs.getInt(2)==1 || rs.getInt(3)==1)
			return true;
		return false;
	}
	
	private void registerUser(String uname, String passHash, String email) throws SQLException {
		DBConnector conn = DBConnector.getInstance();
		PreparedStatement stmt = conn.createPreparedStatement(
				"insert into politics_app.user_cred (uname, pass_hash, email, active) "
				+ "values ( (?), (?), (?), 1 )"
				);
		stmt.setString(1, uname);	stmt.setString(2, passHash);	stmt.setString(3, email);
		ResultSet rs = conn.execSQL(stmt);
	}

}
