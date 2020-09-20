import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.StringTokenizer;

public class User {

	Scanner input;
	private String user_id;
	
	User(Statement stmt, Connection con, String u_id) throws SQLException 
	{
		input = new Scanner(System.in);
		user_id = u_id; // initialize user id
		System.out.println("Welcome "+user_id+"!");
		System.out.println("");
		
		while(true)
		{
			int selection;
			System.out.println("");
			System.out.println("0. Logout");
			System.out.println("1. MusicPlayer");
			System.out.println("2. Search/Add music");
			System.out.println("3. Subscription");
			System.out.println("4. Delete ID");
			
			selection = input.nextInt();
			input.nextLine(); // eliminate next line
			if(selection == 0)
			{
				System.out.println("");
				break;
			}
			else if(selection ==1)
			{
				music_player(stmt,con);
			}
			else if(selection ==2)
			{
				search_add(stmt,con);
			}
			else if(selection ==3)
			{
				subscription(stmt,con);
			}	
			else if(selection ==4) // delete account
			{
				System.out.print("Do you want delete your ID ? (Y/N)");
				String delete = input.nextLine();
				
				if(delete.toUpperCase().equals("Y")) 
				{
					//delete your id 
					String delete_account_sql = "DELETE FROM PLAYER_USER WHERE User_id = " + user_id ;
					stmt.executeUpdate(delete_account_sql);
					System.out.println("your account is delete...");
					System.out.println("");
					break;//log out
				}
			}
			else 
			{
				System.out.println("wrong input");
			}
		}
	}
	
	// user can select functions related to search/ and
	private void search_add(Statement stmt ,Connection con)
	{
			while(true)
			{
				try
				{
					int selection;
					System.out.println("");
					System.out.println("0. Previous ");
					System.out.println("1. Search Music ");
					System.out.println("2. Search Album ");
					System.out.println("3. Search Artist ");
					System.out.println("4. Add musics in playlist ");
					selection = input.nextInt();
					input.nextLine(); // eliminate next line
				
					if(selection == 0)
					{
						break;
					}
					else if(selection ==1)
					{
						search_Music(stmt,con);
					}
					else if(selection ==2)
					{
						search_Album(stmt,con);
					}
					else if(selection ==3)
					{
						search_Artist(stmt);
					}
					else if(selection ==4)
					{
						insert_Played(stmt);
					}
					else // when select wrong input
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
	
	// user can select function related to subscription
	private void subscription(Statement stmt, Connection con)
	{
		while(true)
		{
			try 
			{
				int selection;
				System.out.println("");
				System.out.println("0. Previous ");
				System.out.println("1. Show my subscription ");
				System.out.println("2. Show list of subcription ");
				System.out.println("3. Purchase subcription ");
				System.out.println("4. Cncel subscription ");
				
				selection = input.nextInt();
				input.nextLine(); // eliminate next line
				if(selection == 0)
				{
					System.out.println("");
					break;
				}
				else if(selection ==1)
				{
					show_User_Subscription(stmt);
				}
				else if(selection ==2)
				{
					show_All_Subscription(stmt,con);
				}
				else if(selection ==3)
				{
					use_Subscription(stmt);
				}
				else if(selection ==4)
				{
					cancel_Subscription(stmt);
				}
				else
				{
					System.out.println("wrong input!");
				}
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	// method to user can select new subscription
	private void use_Subscription(Statement stmt) throws SQLException 
	{
		System.out.println("");
		//check already using subscription
		String check_subscription = "SELECT Sub_name FROM PLAYER_USER WHERE User_id = " + user_id;
		ResultSet check = stmt.executeQuery(check_subscription);
		check.next();
		//when user already have subscription
		if(!(check.getString(1) == null))
		{
			System.out.println("You are already using "+ check.getString(1) + " subscription");
			return;
		}
		check.close();
		System.out.println("Select subscription name :"); // select subscription
		String sub_name = input.nextLine();
		sub_name = "'"+ sub_name + "'";
		String find_subscription_name_sql = "SELECT COUNT(*) FROM SUBSCRIPTION WHERE subscription_name = " + sub_name ;
		ResultSet rs = stmt.executeQuery(find_subscription_name_sql);
		rs.next();
		if(rs.getInt(1) > 0) // when select subscription
		{
			//Calculate expiry date 
			SimpleDateFormat day_format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar time = Calendar.getInstance();
			String payment_date = "'"+day_format.format(time.getTime())+"'";
			time.add(Calendar.DATE,29); // including expiry day after 30 days which including payment day
			String expiry_time = "'" + day_format.format(time.getTime())+"'";
			String add_subscriptoin_name_sql = "UPDATE PLAYER_USER SET Sub_name = " + sub_name +", Payment_date = "+payment_date + ", Expiry_date = " +expiry_time + " WHERE User_id = " + user_id;
			stmt.executeUpdate(add_subscriptoin_name_sql); // 
			System.out.println("new subscription added!");
		}
		else // when get wrong input
		{
			System.out.println("That subscription do not exist");
		}
		System.out.println("");
		rs.close();
	}
	
	// method to cancel user subscripotion
	private void cancel_Subscription(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("Do you want to cancel this subscription Y/N");
		String selection  = input.nextLine();
		if(selection.toUpperCase().equals("Y"))
		{
			
			String cancel_sub_sql = "UPDATE PLAYER_USER SET Sub_name = NULL, Payment_date = NULL , Expiry_date = NULL WHERE User_id = " + user_id;
			stmt.executeUpdate(cancel_sub_sql); //cancel subscription 
			System.out.println("cancel success!");
		}
		System.out.println("");
	}
	
	//method to show user about subcription information
	private void show_All_Subscription(Statement stmt, Connection con) throws SQLException
	{
		System.out.println("");
		System.out.println("-----------------------------list of subscription----------------------------------------");
		String show_subscriptoin_sql = "SELECT Subscription_name, Price, Device_number FROM SUBSCRIPTION"+" ORDER BY Price DESC"
				+ "";
		ResultSet rs = stmt.executeQuery(show_subscriptoin_sql);
		System.out.println("Subscription_name       Price            Device_at_the_same_time         Possible_Device");
		while(rs.next())
		{
			System.out.printf("%-20s  %-5d WON              %7d                ", rs.getString(1),rs.getInt(2),rs.getInt(3)); //print subscription table information
			Statement stmt2 = con.createStatement();
			String device_sql = "SELECT Possible_device FROM SUBSCR_DEVICE, SUBSCRIPTION WHERE Subscription_name = Subsc_name AND Subsc_name = "+"'"+rs.getString(1)+"'" + " ORDER BY Possible_device";
			ResultSet rs2 = stmt2.executeQuery(device_sql);
			while(rs2.next())
			{
				System.out.printf("%-7s ",rs2.getString(1)); //print subcriptoin device information
			}
			System.out.println("");
			rs2.close();
		}
		System.out.println("-----------------------------------------------------------------------------------------"
				+ "");
		System.out.println("");
		rs.close();
	}
	// show information user subscription status
	private void show_User_Subscription(Statement stmt) throws SQLException
	{
		System.out.println("");
		String show_subscripton_sql = "SELECT Sub_name, Payment_date,Expiry_date FROM PLAYER_USER WHERE User_id ="+user_id;
		ResultSet rs = stmt.executeQuery(show_subscripton_sql);
	
		rs.next();
		if(!(rs.getString(1)==null)) // when using subscription
		{
			System.out.println("-----------------------your subscription information--------------------------");
			System.out.println("Subscription name : "+rs.getString(1));
			System.out.println("Payment_date : "+rs.getString(2));
			System.out.println("Expiry_date : "+rs.getString(3));
			System.out.println("------------------------------------------------------------------------------");
		}
		else// not using subscription2
		{
			System.out.println("you are not using subscription");
		}
	
	
		System.out.println("");
	}
	
	// delete user's specific playlist
	private void delete_Playlist(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("What number of playlist do you want to delete : ");
		int list_number = input.nextInt();
		input.nextLine(); // eliminate new line
		
		String count_list_sql = "SELECT COUNT(*) FROM PLAYLIST WHERE U_id ="+user_id;
		ResultSet rs = stmt.executeQuery(count_list_sql);
		rs.next();	
		if((rs.getInt(1)>1)) //need at least one play list
		{
			//delete playlist 
			String delete_playlist_sql = "DELETE FROM PLAYLIST WHERE Playlist_number = " + list_number +" AND U_id = " + user_id;
			stmt.executeUpdate(delete_playlist_sql);
			if(list_number < 10) 
			{
				String update_list_number = "UPDATE PLAYLIST SET Playlist_number = Playlist_number -1 WHERE Playlist_number > "+list_number+" AND U_id = " + user_id;
				stmt.executeUpdate(update_list_number);
			}
			System.out.println("deletion success!");
			System.out.println("");
		}
		else
		{
			System.out.println("at least one playlist needed!");
		}
		System.out.println("");
		rs.close();
	}
	
	
	// user can select function related with music player
	private void music_player(Statement stmt, Connection con) 
	{
		
		while(true)
		{
			try
			{
				int selection;
				System.out.println("");
				System.out.println("0. Previous ");
				System.out.println("1. Show all playlist ");
				System.out.println("2. Show music in playlist");
				System.out.println("3. Add new play list");
				System.out.println("4. Delete play list");
				System.out.println("5. Delete musics in play list");
				System.out.println("6. Change Playlist name ");
				selection = input.nextInt();
				input.nextLine(); // eliminate next line
			
				if(selection == 0) //previous menu
				{
					break;
				}
				else if(selection ==1)
				{
					show_Playlist(stmt);
				}
				else if(selection ==2)
				{
					int list_number;
					System.out.print("What number of play list do you want ? ");
					list_number = input.nextInt();
					System.out.println("");
					show_Pl_Music(stmt,con,list_number);
				}
				else if(selection ==3)
				{
					System.out.print("insert new name of play list :");
					String title = input.nextLine();
					insert_Playlist(stmt,title,user_id);
					System.out.println();
				}
				else if(selection ==4)
				{
					delete_Playlist(stmt);
				}
				else if(selection ==5)
				{
					delete_Played(stmt);
				}
				else if(selection == 6)
				{
					change_Playlist_Name(stmt);
				}
				else
				{
					System.out.println("Wrong input!");
					System.out.println(" ");
				}

			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	// search music with specific genre and title
	private void search_Music(Statement stmt,Connection con) throws SQLException
	{
		String genre, music_title,search_music_sql;
		System.out.println("");
		System.out.println("Select Genre one of them (HIPHOP,BALLADE,POP,DANCE,INDI,ALL): ");
		genre = input.nextLine();
		genre = "'" + genre.toUpperCase() + "'";
		
		System.out.print("Search music with music title (Enter -> show all music in selected genre) :");
		music_title = input.nextLine();
		music_title = "'%"+music_title.toLowerCase()+"%'";
		if(genre.toUpperCase().equals("'ALL'"))
		{
			 search_music_sql = "SELECT Music_number,Music_title FROM MUSIC WHERE  Music_title LIKE"+music_title;
		}
		else
		{
			search_music_sql = "SELECT Music_number, Music_title FROM MUSIC, MUSIC_GENRE WHERE Music_number = Music_num AND M_genre = " +genre+" AND Music_title LIKE "+music_title;
		}
		
		ResultSet rs1 = stmt.executeQuery(search_music_sql);
		Statement stmt2 = con.createStatement();
		System.out.println("----------------------------------------------------Result from" + genre+ "genre------------------------------------------------------------");
		System.out.printf("music_number   %-20s   %-20s   %-40s\n","music_title","Album_name","Artist");
		while(rs1.next())
		{
			
			System.out.printf("%-10d    %-20s  ",rs1.getInt(1),rs1.getString(2)); // print music number , music title
		
			//find music title
			String music_album_sql = "SELECT Album_title FROM MUSIC, ALBUM WHERE Album_number = Album_num AND Music_number = " + rs1.getInt(1);
			ResultSet r_album = stmt2.executeQuery(music_album_sql);
			if(r_album.next()) //when this music has album
			{
				System.out.printf("%-20s   ", r_album.getString(1)); //print album title
			}
			else //when this music doesn't have album
			{
				System.out.printf
				("%-20s","");
			}
			r_album.close();
			
			//find artist name
			String music_artist_sql = "SELECT Artist_name FROM PERFORM, ARTIST WHERE At_num = Artist_number AND M_num =" + rs1.getInt(1);
			ResultSet rs2 = stmt2.executeQuery(music_artist_sql);
			//print artist
			while(rs2.next())
			{
				System.out.printf("%-20S ",rs2.getString(1));
			}
			System.out.println("");
			rs2.close();
		}
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
		rs1.close();
		stmt2.close();
	}
	//search artist and show specific artist information
	private void search_Artist(Statement stmt) throws SQLException
	{
		System.out.println("");
		System.out.println("Search_Artist_name (Enter -> show all): ");
		String artist = input.nextLine();
		artist ="'%"+artist.toLowerCase()+"%'";
		String search_artist_sql = " SELECT Artist_number, Artist_name FROM ARTIST WHERE Artist_name LIKE" +artist;
		ResultSet rs = stmt.executeQuery(search_artist_sql);
		System.out.println("----------------------------Artist------------------------------");
		System.out.println("Artist_number   Artist_name");
		while(rs.next())
		{
			System.out.println(rs.getInt(1)+"               "+rs.getString(2));
		}
		System.out.println("Do you want to check specific artsit information? Y/N ");
		String select = input.nextLine();
		if(select.toUpperCase().equals("Y")) //want to show artist information
		{
			System.out.println("artist number : ");
			int at_num = input.nextInt();
			input.nextLine(); //eliminate nextline
			show_Artist(stmt,at_num); //show artist information
		}
		System.out.println("");
		rs.close();
	}
	
	//search specific album and show album information
	private void search_Album(Statement stmt, Connection con) throws SQLException 
	{
		System.out.println("");
		System.out.println("Search_Album_name (Enter -> show all): ");
		String album = input.nextLine();
		album = "'%" + album.toLowerCase() + "%'";
		String search_album_sql = " SELECT Album_number, Album_title FROM ALBUM WHERE Album_title LIKE " +album;
		ResultSet rs = stmt.executeQuery(search_album_sql);
		System.out.println("----------------------------Album------------------------------");
		System.out.println("Album_number   Album_title           Artist");
		Statement stmt2 = con.createStatement();
		while(rs.next())
		{
			System.out.printf("%d             %-20s  ",rs.getInt(1),rs.getString(2));
			String album_artist_sql = "SELECT Artist_name FROM ARTIST,RELEASED WHERE Artist_number = At_number AND Ab_number = "+rs.getInt(1);

			ResultSet rs2 = stmt2.executeQuery(album_artist_sql);
			while(rs2.next())
			{
				System.out.printf("%-20s ",rs2.getString(1));
			}
			System.out.println("");
			rs2.close();
		}
		System.out.println("Do you want to check specific album information? Y/N ");
		String select = input.nextLine();
		if(select.toUpperCase().equals("Y")) //want to show album information
		{
			System.out.println("album number : ");
			int ab_num = input.nextInt();
			input.nextLine(); //eliminate nextline
			show_Album(stmt,con,ab_num); //show alubm information
		}
		
		System.out.println("");
		stmt2.close();
		rs.close();
	}
	
	// show play lists and its names which enrolled by user
	private void show_Playlist(Statement stmt) throws SQLException
	{
		String show_list_sql = "SELECT Playlist_number, Playlist_title FROM PLAYLIST ";
		show_list_sql = show_list_sql + "WHERE U_id =" + user_id  + "ORDER BY Playlist_number"; //print to in order of playlist number
		ResultSet rs = stmt.executeQuery(show_list_sql);
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println("your play list ");
		while(rs.next())
		{
			System.out.printf("(%d)  title : %-20S \n", rs.getInt(1), rs.getString(2));
		}
		System.out.println("-----------------------------------------------------------------------------------------");
		rs.close();
	}
	
	// show music information in play list
	private void show_Pl_Music(Statement stmt, Connection con, int list_number ) throws SQLException
	{
		System.out.println("");
		//get number of musics in play list
		String number_of_musics_sql = "SELECT COUNT(*) FROM PLAYED WHERE Pl_number = "+list_number + "  AND Us_id = "+ user_id;
		ResultSet r = stmt.executeQuery(number_of_musics_sql);
		r.next();
		int number_of_musics = r.getInt(1);
		r.close();
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println(""+list_number+" - "+"Playlist / There are " + number_of_musics+ " musics in playlist"); //print number of musics in list
		
		Statement stmt2 = con.createStatement();
		// find musics in play list in stored order
		String show_music_sql = " SELECT Music_number, Music_title FROM PLAYED, MUSIC WHERE M_number = Music_number AND  Pl_number ="+ list_number+ " AND  Us_id ="+ user_id + "ORDER BY Play_number";
		ResultSet rs = stmt.executeQuery(show_music_sql);
		System.out.println("Play_number  Music_number   Music_title             Artist");
		int i =0;
		while(rs.next())
		{

			System.out.printf("    (%d)          %-10d  %-20s   ",(++i),rs.getInt(1), rs.getString(2)); //print music information
			//get artist name
			String find_artist_information = "SELECT Artist_name FROM ARTIST,PERFORM  WHERE At_num = Artist_number AND M_num = "+rs.getInt(1);
			ResultSet rs2 = stmt2.executeQuery(find_artist_information);
			while(rs2.next())
			{
				System.out.printf("%-20s ",rs2.getString(1)); //print artist name
			}
			System.out.println("");
		}
		System.out.println("-----------------------------------------------------------------------------------------");
		rs.close();
		stmt2.close();
		System.out.println("");
	}
	
	// delete music in palyist 
	private void delete_Played(Statement stmt) throws SQLException
	{
		int music_number , list_number , play_number;
		System.out.println("");
		System.out.print("Select play list : ");
		list_number = input.nextInt();
		input.nextLine();// eliminate new line
		System.out.print("Delete music numbers :");
		String music_numbers = input.nextLine(); // get deleted music numbers from input
		StringTokenizer token = new StringTokenizer(music_numbers);
		while(token.hasMoreTokens())
		{
			music_number = Integer.parseInt(token.nextToken());
			//get music's playnumber in playlist
			String get_play_number_sql = "SELECT Play_number FROM PLAYED WHERE Us_id = "+user_id + " AND Pl_number = " + list_number +" AND M_number = "+ music_number;
			ResultSet pn = stmt.executeQuery(get_play_number_sql);
			pn.next();
			play_number  = pn.getInt(1);
			pn.close();
			
			//update play_number of musics in playlist
			String update_pl_sql = "UPDATE PLAYED SET Play_number = Play_number -1 WHERE  Us_id = "+user_id + " AND Pl_number = " + list_number + " AND Play_number > " + play_number;
			stmt.executeUpdate(update_pl_sql);
			String delete_music_sql = "DELETE FROM PLAYED WHERE Us_id = "+user_id + " AND Pl_number = " + list_number +" AND M_number = "+ music_number;
			stmt.executeUpdate(delete_music_sql);
			System.out.println(music_number + " deleted in playlist!");
		}
		
		System.out.println();
	}
	//change play list name
	private void change_Playlist_Name(Statement stmt) throws SQLException
	{
		int list_number;
		System.out.println("");
		System.out.print("select play list : ");
		list_number = input.nextInt();
		input.nextLine(); //eliminate new line
		System.out.print("new playlist name : ");
		String new_name = input.nextLine();
		new_name = "'"+new_name+"'";
		//Update Playlist's new name 
		String change_playlist_name_sql = "UPDATE PLAYLIST SET Playlist_title = " + new_name + " WHERE U_id = " + user_id + " AND Playlist_number = " +list_number;
		stmt.executeUpdate(change_playlist_name_sql);
		System.out.println("you changed playlist name");
		System.out.println("");
	}
	private void show_Album(Statement stmt, Connection con, int ab_num) throws SQLException
	{
		String show_album_sql = "SELECT Album_title, Sell_date FROM ALBUM WHERE Album_number =" +ab_num;
		ResultSet abrs = stmt.executeQuery(show_album_sql);
		if(abrs.next())
		{
			System.out.println();
			System.out.println("-----------------------------------------------------------------------------------");
			System.out.println("Detail of Album");
			
			//print basic information of album
			System.out.println("Album title : " + abrs.getString(1));
			System.out.println("Album Sell_date : " + abrs.getString(2));
			abrs.close();
			Statement stmt2 = con.createStatement(); 
			//print artist 
			String show_artist_sql = "SELECT Artist_name FROM ARTIST,RELEASED WHERE Artist_number = At_number AND Ab_number =" + ab_num;
			ResultSet  ars = stmt2.executeQuery(show_artist_sql);
			System.out.print("Artist : ");
			while(ars.next())
			{
				System.out.print(ars.getString(1)+"  ");
			}
			System.out.println("");//change line
			ars.close();
			
			//print music in album
			System.out.println("========================== Music list in this Album ==========================");
			System.out.println("Tack_number   Music_number  Music_title           Artist");
			String show_music_sql = " SELECT Track_number, Music_number, Music_title FROM MUSIC WHERE Album_num ="+ ab_num +" ORDER BY Track_number";
			ResultSet mrs = stmt2.executeQuery(show_music_sql);
			Statement stmt3 = con.createStatement();
			while(mrs.next())
			{
				System.out.printf("%-11d   %-11d   %-20s   ", mrs.getInt(1), mrs.getInt(2), mrs.getString(3));
				//print artist in music
				String show_music_artist_sql = "SELECT Artist_name FROM ARTIST , PERFORM WHERE Artist_number = At_num AND M_num = " + mrs.getInt(2);
				ResultSet art_rs = stmt3.executeQuery(show_music_artist_sql);
				while(art_rs.next())
				{
					System.out.printf("%-20s  ",art_rs.getString(1));
				}	
				System.out.println("");	
				art_rs.close();
			}
			System.out.println("-----------------------------------------------------------------------------------");
			System.out.println(""); // change line
			mrs.close();
			stmt3.close();
			stmt2.close();
		}
		else 
		{
			System.out.println("There is no such Album!");
			System.out.println("");
			abrs.close();
		}
	}
	// show artist's information and artist ,musics related with 
	private void show_Artist(Statement stmt,  int at_num) throws SQLException
	{
		String show_artist_sql = "SELECT Artist_name FROM Artist WHERE Artist_number =" +at_num;
		ResultSet atrs = stmt.executeQuery(show_artist_sql);
		if(atrs.next())
		{
			System.out.println();
			System.out.println("-----------------------------------------------------------------------------------");
			System.out.println("Detail of Artist");
			//print artist name
			System.out.println("Artist name : " + atrs.getString(1));
			atrs.close();
			//print artist 
			String show_genre_sql = "SELECT Art_genre FROM ARTIST_GENRE WHERE Artist_num =" + at_num;
			ResultSet  agrs = stmt.executeQuery(show_genre_sql);
			System.out.print("Genre : ");
			while(agrs.next())
			{
				System.out.print(agrs.getString(1)+"  ");
			}
			System.out.println("");//change line
			agrs.close();
			
			//print musics in artist
			System.out.println("========================== Music list in this Artist ==========================");
			String show_music_sql = " SELECT Music_number, Music_title FROM MUSIC,PERFORM WHERE M_num = Music_number AND At_num ="+ at_num +" ORDER BY Music_title";
			ResultSet mrs = stmt.executeQuery(show_music_sql);
			System.out.println("Music_number   Music_title");
			while(mrs.next())
			{
				System.out.println(mrs.getInt(1)+"              "+mrs.getString(2));
			}
		
			System.out.println(""); // change line
			mrs.close();
			
			// print album in artist
			System.out.println("========================== Album list in this Artist ==========================");
			String show_album_sql = " SELECT Album_number, Album_title FROM Album,RELEASED WHERE Ab_number = Album_number AND At_number ="+ at_num +" ORDER BY Album_title";
			ResultSet abrs = stmt.executeQuery(show_album_sql);
			System.out.println("Album_number   Album_title");
			while(abrs.next())
			{
				System.out.println(abrs.getInt(1)+"              "+abrs.getString(2));
			}
			System.out.println("");
			
			System.out.println("-----------------------------------------------------------------------------------");
			System.out.println(""); // change line
			abrs.close();
		}
		else //when failed to search
		{
			System.out.println("There is no such artist!");
			System.out.println("");
			atrs.close();
		}
		
	}
	
	//insert specific user's new play list with title 
	public static void insert_Playlist(Statement stmt, String title, String user_id) throws SQLException {
		title = "'" + title + "'";
		String counting_list_sql = "SELECT COUNT(*) FROM PLAYLIST WHERE U_id=" + user_id; // check number of user's playlist 
		ResultSet rs = stmt.executeQuery(counting_list_sql);
		rs.next();
		int number_of_list = rs.getInt(1);
		if (number_of_list >= 0 && number_of_list <= 10) // maximum number of playlist is 10
		{
			String insert_list_sql = "INSERT INTO PLAYLIST(Playlist_number,PlayList_title,U_id)";
			insert_list_sql = insert_list_sql + "VALUES(" + (number_of_list + 1) + "," + title + "," + user_id + ")";
			stmt.executeUpdate(insert_list_sql);
		} else {
			System.out.println("already have full play list");
		}
	}
	
	//insert new music in play list
	private void insert_Played(Statement stmt) throws SQLException
	{ 
		ArrayList<Integer> m_number_list = new ArrayList<Integer>();
		int max_play_number, list_number , number_of_musics;
		String music_number;
		
		System.out.print("Select play_list number : ");
		list_number= input.nextInt();
		input.nextLine(); // eliminate next line
		//get music numbers from input
		System.out.print("input music numbers : ");
		music_number = input.nextLine();
		StringTokenizer token = new StringTokenizer(music_number);
		while(token.hasMoreTokens())
		{
			m_number_list.add(Integer.parseInt(token.nextToken()));
		}
		
		//get number of musics in playlist and Max play_number
		String get_playlist_information_sql = "SELECT COUNT(*), MAX(Play_number) FROM PLAYED "+ "WHERE Pl_number = " + list_number + " AND Us_id = " + user_id;
		ResultSet r = stmt.executeQuery(get_playlist_information_sql);
		r.next();
		number_of_musics = r.getInt(1);
		max_play_number = r.getInt(2);
		
		r.close();
		
		if((number_of_musics + m_number_list.size()) > 1000) // when exceeds limit of number of musics 
		{
			System.out.println("it exceed 1000 musics limit!");
			return;
		}
		
		//insert played_sql tuple
		String Played_sql_foramt= "INSERT INTO PLAYED(Pl_number,Us_id,M_number,Play_number) VALUES(";
		for(int i = 0 ; i <m_number_list.size();i++)
		{
			//check that same music is in play list
			String check_same = "SELECT COUNT(*) FROM PLAYED WHERE Pl_number = " + list_number + " AND Us_id = "+user_id+ " AND M_number = "+m_number_list.get(i);
			ResultSet result = stmt.executeQuery(check_same);
			result.next();
			if(result.getInt(1) > 0) //when same music already in playlist
			{
				System.out.println(m_number_list.get(i)+" music is already in playlist");
			}
			else // add music in playlist 
			{
				String insert_Played_sql =  Played_sql_foramt +list_number+"," +user_id+","+m_number_list.get(i)+","+(++max_play_number)+")";
				stmt.executeUpdate(insert_Played_sql);
				System.out.println("success in inserting music number : " +m_number_list.get(i));
			}
			result.close();
		}
	}
}
