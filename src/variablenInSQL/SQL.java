package variablenInSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQL
{
	private final String url = "jdbc:postgresql://localhost:5432/aufgabe3";
	private String user = "read_only";
	private String password = "read_only";
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private Exception e = null;

	public SQL(String query)
	{
		if (GUI.finished)
		{
			user = "read_and_write";
			password = "read_and_write";
		}
		try
		{
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			System.out.println("Connected to the PostgreSQL server successfully.");
		}
		catch (Exception e)
		{
			this.setE(e);
			System.out.println(e.getMessage());
		}
		try
		{
			rs = stmt.executeQuery(query);
			System.out.println("Run query successfully");
		}
		catch (Exception e)
		{
			this.setE(e);
			System.out.println(e.getMessage());
		}
	}

	public ResultSet getResultSet()
	{
		return rs;
	}

	public Exception getE()
	{
		return e;
	}

	public void setE(Exception e)
	{
		this.e = e;
	}
}