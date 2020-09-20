import java.sql.*;
import java.util.Scanner;


public class PlayerProgram {

	public Scanner input;

	PlayerProgram() 
	{
		input = new Scanner(System.in);
	}

	private String user_Login(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("Login ---------------------------------------------");
		while(true) 
		{
			String id,password,retry,login_sql;
			System.out.print("ID : ");
			id = input.nextLine();
			id = "'"+ id +"'";
			System.out.print("Password : ");
			password = input.nextLine();
			password = "'" + password +"'";
			
			login_sql = "SELECT USER_ID FROM PLAYER_USER WHERE User_id =";
			login_sql = login_sql +id+ " AND User_password =" +password;		
		
			ResultSet rs = stmt.executeQuery(login_sql);
			if(rs.next())//when success in login
			{
				System.out.println("Success! ");
				rs.close();
				System.out.println("");
				return id;
			}
			else // check retry
			{
				System.out.println("Wrong Password or ID... retry again? Y/N");
				retry = input.nextLine();
				if(!retry.toUpperCase().equals("Y")) // when not retry
				{
					rs.close();
					break;
				}
			}
		}
		System.out.println("");
		return null; 
	}
	private int manager_Login(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("Login ---------------------------------------------");
		while(true)
		{
			String id,password,retry,login_sql;
			int mng_number;
			System.out.print("ID : ");
			id = input.nextLine();
			id = "'"+ id +"'";
			System.out.print("Password : ");
			password = input.nextLine();
			password = "'" + password +"'";
			
			login_sql = "SELECT Manager_number FROM MANAGER WHERE Manager_id =";
			login_sql = login_sql + id+ " AND Manager_password =" +password;
			ResultSet rs = stmt.executeQuery(login_sql);
			if(rs.next())//when success in login
			{
				System.out.println("Success! ");
				mng_number = rs.getInt(1);
				rs.close();
				return mng_number;
			}
			else // check retry
			{
				System.out.println("Wrong Password or ID... retry again? Y/N");
				retry = input.nextLine();
				if(!retry.toUpperCase().equals("Y")) // when not retry
				{
					rs.close();
					break;
				}
			}
		}
		return -1;
	}
	private void signUp(Statement stmt) throws SQLException {

		ResultSet rs;
		String Userid, User_password, User_name, Phone_number, Sex, User_address,birth;
		// check same user ID
		while (true) {
			System.out.print("ID : ");
			Userid = input.nextLine();
			Userid = "'" + Userid + "'";
			String same_id_sql = "SELECT User_id FROM PLAYER_USER WHERE User_id =";
			same_id_sql = same_id_sql + Userid;
			rs = stmt.executeQuery(same_id_sql);
			if (rs.next()) // when there is same id in PLAYER_USER table
			{
				System.out.println("same id exists retry again");
			} 
			else 
			{
				break;
			}
		}
		// get user's information
		System.out.print("Password : ");
		User_password = input.nextLine();
		User_password = "'" + User_password + "'";
		System.out.print("Name : ");
		User_name = input.nextLine();
		User_name = "'" + User_name + "'";
		System.out.print("Phone_number : ");
		Phone_number = input.nextLine();
		Phone_number = "'" + Phone_number + "'";
		System.out.print("Sex M or F: ");
		Sex = input.nextLine();
		Sex = "'" + Sex + "'";
		System.out.print("Address : ");
		User_address = input.nextLine();
		User_address = "'" + User_address + "'";
		System.out.print("Birth yyyy-mm-dd: ");
		birth = input.nextLine();
		birth = "'" + birth + "'";
		//insert account information
		String user_sql = "INSERT INTO PLAYER_USER(User_id,User_password,User_name,Phone_number,Sex,User_address,Birth_date)";
		user_sql = user_sql + "VALUES(" + Userid + "," + User_password + "," + User_name + "," + Phone_number + ","
				+ Sex + "," + User_address + ","+birth+")";

		stmt.executeUpdate(user_sql);

		User.insert_Playlist(stmt, "My Play List", Userid);
		System.out.println("Register Success!");
	}


	public static void main(String[] args) {
		try {
			
			Class.forName("org.mariadb.jdbc.Driver").newInstance();

			String url = "jdbc:mariadb://127.0.0.1:3306/MusicPlayer?serverTimezone=UTC";
			String user = "db";
			String psw = "db!";
			Connection con = DriverManager.getConnection(url, user, psw);
			Statement stmt = con.createStatement();
			PlayerProgram pr = new PlayerProgram();
			while(true)
			{
				int selection;
				System.out.println("0. Exit");
				System.out.println("1. Manager_mode");
				System.out.println("2. User_mode");
				System.out.println("3. Register");
				selection = pr.input.nextInt();	
				pr.input.nextLine();// eliminate new line
				if(selection == 0)
				{	
					break;
				}
				else if(selection ==1)
				{
					int mng_number = pr.manager_Login(stmt);
					if(mng_number !=-1) //when success in login
					{
						Manager manager = new Manager(stmt,con,pr.input,mng_number);
					}
				}
				else if(selection ==2)
				{
					String u_id = pr.user_Login(stmt);
					if(u_id != null) // when success in login 
					{
						User player_user = new User(stmt,con,u_id);
					}
				}
				else if(selection ==3)
				{
					pr.signUp(stmt);
				}
				else
				{
					System.out.println("wrong input!");
					System.out.println("");
				}
			}
			stmt.close();
			con.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
