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

import dbms.DBConnector;
import model.Constants;

/**
 * 
 * @author omar barakat
 * 
 * Test through:
 * - check if logged in:
 * http://localhost:8080/ServerSide/Login?query-is-logged-in
 * - wrong login creds
 * http://localhost:8080/ServerSide/Login?uname=omar&passhash=1
 * - check if logged in:
 * http://localhost:8080/ServerSide/Login?query-is-logged-in
 * - right login creds
 * http://localhost:8080/ServerSide/Login?uname=omar&passhash=0
 * - check if logged in:
 * http://localhost:8080/ServerSide/Login?query-is-logged-in
 *
 */

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameterMap().containsKey("query-is-logged-in")) {
			if(request.getSession(false) != null)
				response.getWriter().append("logged in");
			else
				response.getWriter().append("not logged in");
			return;
		}
		
		String name = request.getParameter("uname");
        String passHash = request.getParameter("passhash");
        int user_id = Constants.NULL_USER_ID;
        // try to login
		try {
			user_id = getUserID(name, passHash);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        if(user_id == Constants.NULL_USER_ID) {
        	response.getWriter().append("not logged in");
        	return;
        }
        request.getSession().setAttribute("uid", user_id);
        response.getWriter().append("logged in");
	}

	public static int getUserID(String name, String passHash) throws SQLException {
		// get user_id with provided credentials. Nonnegative if valid.
		DBConnector conn = DBConnector.getInstance();
		PreparedStatement stmt = conn.createPreparedStatement(
				"select user_id from politics_app.user_cred where uname=(?) and pass_hash=(?)"
				);
		stmt.setString(1, name);
		stmt.setString(2, passHash);
		ResultSet rs = conn.execSQL(stmt);//.getInt(1);
		if(rs.next() == false)	return Constants.NULL_USER_ID;
		return rs.getInt(1);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
