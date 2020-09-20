import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Manager {

	private int manager_number;
	Scanner input;
	Manager(Statement stmt, Connection con, Scanner in , int mng_num) throws SQLException
	{
		manager_number = mng_num;
		input = in;
		
		while(true)
		{
			System.out.println("");
			System.out.println("Manager mode!");
			System.out.println("0. Previous");
			System.out.println("1. Insert music/artist/album/subscription ");
			System.out.println("2. Delete music/artist/album/subscription ");
			System.out.println("3. Show music/artist/album/user");
			int selection = input.nextInt();
			
			if(selection ==0) 
			{
				System.out.println("");
				break;
			}
			else if(selection ==1)
			{
				insert(stmt,con);
			}
			else if(selection ==2)
			{
				delete(stmt,con);
			}
			else if(selection ==3)
			{
				show_registered(stmt);
			}
			else 
			{
				System.out.println("wrong input");
			}
		}
	}
	
	private void show_registered(Statement stmt)
	{
		while(true)
		{
			try
			{
				System.out.println("");
				System.out.println("0. Previous");
				System.out.println("1. Show musics you registered");
				System.out.println("2. Show artists you registered ");
				System.out.println("3. Show album you registered ");
				System.out.println("4. Show all user ");
				int selection = input.nextInt();
				input.nextLine();
				if(selection ==0) 
				{
					System.out.println("");
					break;
				}
				else if(selection ==1)
				{
					show_Registered_Music(stmt);
				}
				else if(selection ==2)
				{	
					show_Registered_Artist(stmt);
				}
				else if(selection ==3)
				{
					show_Registered_Album(stmt);
				}
				else if(selection ==4)
				{
					show_Registered_User(stmt);
				}
				else 
				{
					System.out.println("wrong input");
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	private void insert(Statement stmt, Connection con)
	{
		while(true)
		{
			try
			{
				System.out.println("");
				System.out.println("0. previous ");
				System.out.println("1. Insert music ");
				System.out.println("2. Insert artist ");
				System.out.println("3. Insert album ");
				System.out.println("4. Insert subscription ");
				int selection = input.nextInt();
				input.nextLine();
				if(selection ==0) 
				{
					System.out.println("");
					break;
				}
				else if(selection ==1)
				{
					insert_Music(stmt);
				}
				else if(selection ==2)
				{	
					insert_Artist(stmt);
				}
				else if(selection ==3)
				{
					insert_Album(stmt);
				}
				else if(selection ==4)
				{
					insert_Subscription(stmt);
				}
				else 
				{
					System.out.println("wrong input");
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	private void delete (Statement stmt, Connection con)
	{
		while(true)
		{
			try
			{
				System.out.println("");
				System.out.println("0. Previous ");
				System.out.println("1. Delete music ");
				System.out.println("2. Delete artist ");
				System.out.println("3. Delete album ");
				System.out.println("4. Delete subscription ");
				int selection = input.nextInt();
				input.nextLine();
				if(selection ==0) 
				{
					System.out.println("");
					break;
				}
				else if(selection ==1)
				{
					delete_Music(stmt);
				}
				else if(selection ==2)
				{	
					delete_Artist(stmt,con);
				}
				else if(selection ==3)
				{
					delete_Album(stmt);
				}
				else if(selection ==4)
				{
					delete_Subscription(stmt);
				}
				else 
				{
					System.out.println("wrong input");
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void insert_Music(Statement stmt) throws SQLException {
		ArrayList<String> genre_list = new ArrayList<String>();
		ArrayList<Integer> artist_list = new ArrayList<Integer>();
		String title, genre, artist;
		int number;
		
		System.out.println("");
		//get needed data from input
		System.out.print("Music number : ");
		number = input.nextInt();
		input.nextLine();// eliminate next line
		System.out.print("Music title : ");
		title = input.nextLine();
		title = "'"+title.toLowerCase()+"'";
		System.out.print("Music genre : ");
		genre = input.nextLine();
		genre = genre.toUpperCase();
		System.out.print("artist numbers : ");
		artist = input.nextLine();
		
		//insert Music_genre tuple
		StringTokenizer token = new StringTokenizer(genre);
		while(token.hasMoreTokens())
		{
				genre_list.add("'"+token.nextToken()+"'");
		}
		//when not enter 1~2 genre input
		if((genre_list.size() >2) ||  (genre_list.size() < 1))
		{
			System.out.println("You didn't enter 1~2 genre input");
			return;
		}
		//insert Perform tuple
		token  = new StringTokenizer(artist);
		while(token.hasMoreTokens())
		{
				artist_list.add(Integer.parseInt(token.nextToken()));
		}
		//when not enter artist
		if(artist_list.size() < 1)
		{
			System.out.println("You should enter at least one artist");
			return;
		}
		
		//insert Music tuple
		String insert_Music_sql = "INSERT INTO MUSIC(Music_number,Music_title,Mng_number)";
		insert_Music_sql = insert_Music_sql +"VALUES("+number+","+title+","+manager_number+")";
		stmt.executeUpdate(insert_Music_sql);
		
		//insert music genre
		String insert_genre_format = "INSERT INTO MUSIC_GENRE(Music_num, M_genre)";
		for(int i =0; i < genre_list.size(); i++)
		{
			String insert_genre_sql = insert_genre_format + "VALUES("+number+","+genre_list.get(i)+")";
			stmt.executeUpdate(insert_genre_sql);
		}
		
		//insert music artist
		String insert_Perform_format = "INSERT INTO PERFORM(At_num,M_num) VALUES(";
		if(artist_list.size() == 0) //when not set artist
			
		{
			String insert_Perform_sql = insert_Perform_format + 0 +","+ number+")";
			stmt.executeUpdate(insert_Perform_sql);
		}
		else
		{	for(int i = 0; i< artist_list.size() ;i++)
			{
				String insert_Perform_sql = insert_Perform_format + artist_list.get(i) +","+ number+")";
				stmt.executeUpdate(insert_Perform_sql);
			}
		}
		System.out.println(number+" number music is inserted");
		System.out.println("");
	}
	
	private void delete_Subscription(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.print("Subscription name should be deleted :");
		String subscription_name = input.nextLine();
		subscription_name = "'" + subscription_name + "'";
		
		//check whether this subscription exist
		String check_subscription_sql = "SELECT Subscription_name FROM SUBSCRIPTION WHERE Subscription_name = " + subscription_name;
		ResultSet rs = stmt.executeQuery(check_subscription_sql);
		if(rs.next()) //when subscription exists
		{
			//update player user's subscription informtion
			String update_subscription_sql = "UPDATE PLAYER_USER SET Sub_name = NULL, Payment_date = NULL , Expiry_date = NULL WHERE  Sub_name = " + subscription_name;
			stmt.executeUpdate(update_subscription_sql);
			//delete subscription
			String delete_subscription_Sql = "DELETE FROM SUBSCRIPTION WHERE Subscription_name = " +subscription_name;
			stmt.executeUpdate(delete_subscription_Sql);
		}
		else
		{
			System.out.println("this subscription does not exist!");
		}
		rs.close();
		System.out.println("");
	}
	private void insert_Subscription(Statement stmt) throws SQLException {
		int price, device_number;
		StringTokenizer token;
		String name, device_t;
		ArrayList<String> device_type = new ArrayList<String>();
		System.out.print("Sub scriptoin name : ");
		name = input.nextLine();
		name = "'" + name + "'";
		System.out.print("Price : ");
		price = input.nextInt();
		System.out.print("Number of devices that can play at the same time : ");
		device_number = input.nextInt();
		input.nextLine(); // eliminate change line input
		System.out.println("possible device types(PHONE TABLET PC): ");
		device_t = input.nextLine();

		// insert Subscription tuple
		String insert_subc_sql = "INSERT INTO SUBSCRIPTION(Subscription_name,Price,Device_number)";
		insert_subc_sql = insert_subc_sql + "VALUES(" + name + "," + price + "," + device_number + ")";
		stmt.executeUpdate(insert_subc_sql);

		token = new StringTokenizer(device_t);
		while (token.hasMoreTokens()) {
			device_type.add("'" + token.nextToken() + "'");
		}

		// insert Subscription_device tuple
		for (int i = 0; i < device_type.size(); i++) {
			String insert_subcdevice_sql = "INSERT INTO SUBSCR_DEVICE(Subsc_name,Possible_device)";
			insert_subcdevice_sql = insert_subcdevice_sql + "VALUES(" + name + "," + device_type.get(i) + ")";
			stmt.executeUpdate(insert_subcdevice_sql);
		}
	}

	private void delete_Album(Statement stmt) throws SQLException
	{
		ArrayList<Integer> album_number_list = new ArrayList<Integer>();
		
		System.out.println("");
		System.out.print("Album numbers should be deleted : ");
		String album_numbers = input.nextLine();
		StringTokenizer token = new StringTokenizer(album_numbers);
		int album_number;
		//check album number whether manager registered this album
		while(token.hasMoreTokens())
		{
			album_number = Integer.parseInt(token.nextToken());
			String check_register_sql = "SELECT Album_number FROM ALBUM WHERE Album_number = "+album_number + " AND Manager_num = "+ manager_number;
			ResultSet rs = stmt.executeQuery(check_register_sql);
			if(rs.next()) // when this music was registered from specific manager
			{
				album_number_list.add(album_number);
			}
			else
			{
				System.out.println("you did't register "+album_number +" number album");
			}
			rs.close();
		}
		//before delete album, update musics related with this album
		
		for(int i = 0 ; i < album_number_list.size(); i++)
		{
			String update_music_sql = "UPDATE Music SET Track_number = NULL, Album_num = NULL WHERE Album_num = " + album_number_list.get(i);
			stmt.executeUpdate(update_music_sql); // update music information to null
			String delete_album_sql = "DELETE FROM ALBUM WHERE Album_number = " + album_number_list.get(i);
			stmt.executeUpdate(delete_album_sql); // delete album
			System.out.println("Success in deleting album " + album_number_list.get(i));
		}
		System.out.println("");
	}
	private void insert_Album(Statement stmt) throws SQLException {
		String title, date, artist, music;
		ArrayList<Integer> artist_list = new ArrayList<Integer>();
		ArrayList<Integer> music_list = new ArrayList<Integer>();
		int number, m_number, track_number;
		
		// get needed data from input
		System.out.print("Album number : ");
		number = input.nextInt();
		input.nextLine(); // eliminate next new line 
		System.out.print("Album title : ");
		title = input.nextLine();
		title = "'" + title.toLowerCase() + "'";
		System.out.print("Sell date yyyy-mm-dd : ");
		date = input.nextLine();
		date = "'" + date + "'";
		System.out.print("Artist numbers : ");
		artist = input.nextLine();
		System.out.print("Music numbers : ");
		music = input.nextLine();

		//parsing multiple number data
		StringTokenizer token = new StringTokenizer(artist);
		while (token.hasMoreTokens())
		{
			artist_list.add(Integer.parseInt(token.nextToken()));
		}
		
		token = new StringTokenizer(music);
		while(token.hasMoreTokens())
		{
			music_list.add(Integer.parseInt(token.nextToken()));
		}
		
		//when didn't enter artist
		if( artist_list.size() < 1 )
		{
			System.out.println("you should enter at least one artist");
			return;
		}
		
		if((music_list.size() < 1 ))
		{
			System.out.println("you should enter at least one music");
			return;
		}
		
		// insert Album tuple
		String insert_Album_sql = "INSERT INTO Album(Album_number,Album_title,Sell_date,Manager_num)";
		insert_Album_sql = insert_Album_sql + "VALUES(" + number + "," + title + "," + date + "," + manager_number + ")";
		stmt.executeUpdate(insert_Album_sql);
		
		//insert Released tuple
		
		String artist_sql_format = "INSERT INTO RELEASED(At_number ,Ab_number ) ";
		 
		for (int i = 0; i < artist_list.size(); i++) 
		{
			String insert_artist_sql = artist_sql_format + "VALUES(" + artist_list.get(i) + "," + number + ")";
			stmt.executeUpdate(insert_artist_sql);
		}
		
		
		//update music tuple
		track_number = 0;
		for(int i =0 ; i < music_list.size(); i++)
		{
			track_number++;
			m_number = music_list.get(i);
			String update_music_sql = "UPDATE MUSIC SET Album_num = " + number+",Track_number = "+ track_number+ " WHERE Music_number = "+m_number;
			stmt.executeUpdate(update_music_sql);
		}
		System.out.println(number+" number album is inserted");
	}
	
	private void delete_Artist(Statement stmt, Connection con) throws SQLException
	{
		ArrayList<Integer> artist_number_list = new ArrayList<Integer>();
		System.out.print("artist numbers which should be deleted : ");
		String artist_numbers = input.nextLine();

		StringTokenizer token = new StringTokenizer(artist_numbers);
		int artist_number;
		Statement stmt2 = con.createStatement();
		
		//check artist numbers whether manager registered this artist
		while(token.hasMoreElements())
		{
			artist_number = Integer.parseInt(token.nextToken());
			String check_manager_sql = "SELECT Artist_number FROM ARTIST WHERE Mg_number = "+manager_number +" AND Artist_number = " + artist_number;
			ResultSet r = stmt.executeQuery(check_manager_sql);
			if(r.next()) // when manager number same with 
			{
				artist_number_list.add(artist_number);
			}
			else
			{
				System.out.println("you didn't register "+artist_number+" number artist!");
			}
			r.close();
		}
		
		for(int i =0 ; i < artist_number_list.size(); i++)
		{
			
			//  before delete artist, make at least one artist in album, music
			//find music which has no artist when it's artist is deleted;
			String connected_music_sql = "SELECT M_num ,COUNT(*) FROM PERFORM WHERE M_num IN (SELECT M_num FROM PERFORM WHERE At_num = "+artist_number_list.get(i) +" ) GROUP BY M_num HAVING COUNT(*) = 1";
			ResultSet rs = stmt.executeQuery(connected_music_sql);
			
			//update music's artist in default 
			while(rs.next())
			{
				String update_music_sql = "UPDATE PERFORM SET At_num = 0 WHERE M_num = " + rs.getInt(1); // set default artist 
				stmt2.executeUpdate(update_music_sql); // update in 0
			}
			rs.close();
			
			
			//find music which has no album when it's artist is deleted;
			String connected_album_sql = "SELECT Ab_number, COUNT(*) FROM RELEASED WHERE Ab_number IN (SELECT Ab_number FROM RELEASED WHERE At_number = "+ artist_number_list.get(i) + ") GROUP BY Ab_number HAVING COUNT(*) =1";
			ResultSet rs2 = stmt.executeQuery(connected_album_sql);
			
			//update music's artist in default 
			while(rs2.next())
			{
				String update_album_sql = "UPDATE RELEASED SET At_number = 0 WHERE Ab_number = "+ rs2.getInt(1);
				stmt2.executeUpdate(update_album_sql); // update in 0
			}
			rs2.close();
			
			String delete_artist_sql = "DELETE FROM ARTIST WHERE Artist_number = " + artist_number_list.get(i);
			stmt.executeUpdate(delete_artist_sql); // delete artist
			System.out.println(artist_number_list.get(i) + " number artist is deleted");
			
		}
		
		stmt2.close();
	}
	
	private void delete_Music(Statement stmt) throws SQLException
	{
		ArrayList<Integer> music_number_list = new ArrayList<Integer>();
		int music_number , album_number;
		System.out.print("Delete music numbers :");
		String music_numbers = input.nextLine(); // get deleted music numbers from input
		StringTokenizer token = new StringTokenizer(music_numbers);
		
		//check manager register this music
		while(token.hasMoreElements())
		{
			music_number = Integer.parseInt(token.nextToken());
			String check_register = "SELECT Music_number FROM MUSIC WHERE Music_number = "+music_number + " AND Mng_number = "+ manager_number;
			ResultSet reg = stmt.executeQuery(check_register);
			if(reg.next())
			{
				music_number_list.add(music_number);
			}
			else
			{
				System.out.println("You didn't register " + music_number + " music");
			}
			reg.close();
		}
		
		for(int i = 0 ; i < music_number_list.size(); i++)
		{
			music_number = music_number_list.get(i);
			//get album number
			String get_album_number = "SELECT Album_num FROM MUSIC WHERE Music_number = "+music_number;
			ResultSet rs = stmt.executeQuery(get_album_number);
			rs.next();
			album_number = rs.getInt(1);
			rs.close();
			//get number of musics in specific album
			String get_album_music = "SELECT COUNT(*) FROM MUSIC WHERE Album_num = "+ album_number;
			ResultSet rs1 = stmt.executeQuery(get_album_music);
			rs1.next();
			
			//when album will not have music, delete album 
			if(rs1.getInt(1) == 1)
			{
				//delete album
				System.out.println("only 1 music left in alubm including "+ music_number + " music ");
				String delete_album = "DELETE FROM ALBUM WHERE Album_number = " + album_number;
				stmt.executeUpdate(delete_album);
				System.out.println("Album including "+ music_number+" music is deleted");
			}
			rs1.close();
			
			//delete music
			String delete_music = "DELETE FROM MUSIC WHERE Music_number = "+music_number;
			stmt.executeUpdate(delete_music);
			System.out.println(music_number + " music is deleted");
		}
	}
	
	private void insert_Artist(Statement stmt) throws SQLException {
		String name, genre;
		ArrayList<String> genre_list = new ArrayList<String>();
		int number;
		// get needed data from input
		System.out.print("Artist number : ");
		number = input.nextInt();
		input.nextLine(); // eliminate new line
		System.out.print("Artist name : ");
		name = input.nextLine();
		name = name.toLowerCase();
		name = "'" + name + "'";
		System.out.print("Insert Genre : ");
		genre = input.nextLine();
		genre = genre.toUpperCase();
		StringTokenizer token = new StringTokenizer(genre);

		while (token.hasMoreTokens()) {
			genre_list.add("'" + token.nextToken() + "'");
		}
		
		//when not enter 1~2 genre input
		if(genre_list.size() <1 || genre_list.size() > 2)
		{
			System.out.println("you didn't input 1~2 artist genre");
			return;
		}
		String insert_Artist_sql = "INSERT INTO ARTIST(Artist_number,Artist_name,Mg_number)";
		insert_Artist_sql = insert_Artist_sql + "VALUES(" + number + "," + name + "," + manager_number + ")";
		stmt.executeUpdate(insert_Artist_sql);

		// insert Artist_Genre tuple
		for (int i = 0; i < genre_list.size(); i++) {
			String insert_Artist_genre_sql = "INSERT INTO ARTIST_GENRE(Artist_num,Art_genre)";
			insert_Artist_genre_sql = insert_Artist_genre_sql + "VALUES(" + number + "," + genre_list.get(i) + ")";
			stmt.executeUpdate(insert_Artist_genre_sql);
		}
		System.out.println(number + " number artist inserted");
	}
	
	private void show_Registered_Music(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("---------------------------------------------------------------------");
		System.out.println("You registered these musics");
		String get_user_sql =  "SELECT Music_number, Music_title FROM Music WHERE Mng_number = "+manager_number;
		System.out.printf("%-11s   %-20s\n", "Music_number", "Music_title");
		ResultSet rs= stmt.executeQuery(get_user_sql);
		//show registered music information from specific manager 
		while(rs.next())
		{
			System.out.printf("%-11d   %-20s\n", rs.getInt(1), rs.getString(2));
		}
		System.out.println("---------------------------------------------------------------------");
		System.out.println("");
		rs.close();
	}
	private void show_Registered_Artist(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("---------------------------------------------------------------------");
		System.out.println("You registered these artists");
		String get_user_sql =  "SELECT Artist_number, Artist_name FROM Artist WHERE Mg_number = "+manager_number;
		System.out.printf("%-11s   %-20s\n", "Artist_number", "Artist_name");
		ResultSet rs= stmt.executeQuery(get_user_sql);
		//show registered artist information from specific manager 
		while(rs.next())
		{
			System.out.printf("%-11d   %-20s\n", rs.getInt(1), rs.getString(2));
		}
		System.out.println("---------------------------------------------------------------------");
		System.out.println("");
		rs.close();
	}
	private void show_Registered_Album(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("---------------------------------------------------------------------");
		System.out.println("You registered these albums");
		String get_user_sql =  "SELECT Album_number, Album_title FROM Album WHERE Manager_num = "+manager_number;
		System.out.printf("%-11s   %-20s\n", "Album_number", "Album_title");
		ResultSet rs= stmt.executeQuery(get_user_sql);
		//show registered artist information from specific manager 
		while(rs.next())
		{
			System.out.printf("%-11d   %-20s\n", rs.getInt(1), rs.getString(2));
		}
		System.out.println("---------------------------------------------------------------------");
		System.out.println("");
		rs.close();
	}
	
	private void show_Registered_User(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("---------------------------------------------------------------------");
		String get_user_sql =  "SELECT User_id, User_password FROM PLAYER_USER";
		System.out.printf("%-15s   %-15s\n", "User_id", "User_password");
		ResultSet rs= stmt.executeQuery(get_user_sql);
		//show registered user information 
		while(rs.next())
		{
			System.out.printf("%-15s   %-15s\n", rs.getString(1), rs.getString(2));
		}
		System.out.println("---------------------------------------------------------------------");
		System.out.println("");
	}
}
