import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Date;

public class test {
	public static int Manager() {
		Scanner sc = new Scanner(System.in);
		int cmd=-1;
		System.out.println("0.delete song");
		System.out.println("1.add song");
		System.out.println("2.view user's information");
		System.out.println("3.update user's information"); //decrement the left days of users
		System.out.println("4.birthday event");
		cmd=sc.nextInt();
		return cmd;
	}
	public static int User() {
		Scanner sc = new Scanner(System.in);
		int cmd=-1;
		System.out.println("0.make a new playlist");
		System.out.println("1.view your list of playlists");
		System.out.println("2.view the songs of a playlist");
		System.out.println("3.delete the playlist");
		System.out.println("4.add or delete the song of the playlist");
		System.out.println("5.search the song");
		System.out.println("6.show the melon chart");
		cmd = sc.nextInt();
		return cmd;
	}
	public static void main(String[] args) {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url="jdbc:mariadb://localhost:3306/melon";
			String user = "root";
			String psw="thpine613";
			Connection con = DriverManager.getConnection(url, user, psw);
			Statement stmt = con.createStatement();
			ResultSet chk;
			while(true) {
				System.out.println("---------------------");
				System.out.println("Who are you?");
				System.out.println("0.Exit");
				System.out.println("1.Manager");
				System.out.println("2.User");
				int cmd = -1;
				Scanner s = new Scanner(System.in);
				cmd=s.nextInt();
				if(cmd==0) {
					con.close();
					stmt.close();
					return; //program returns
				}
				else if(cmd==1) { //manager
					int tmp = Manager();
					switch(tmp) {
					case 0: //delete song
						s.nextLine();//
						System.out.println("input the title of the song");
						String tit = s.nextLine();
						tit= "'"+tit+"'";
						chk=stmt.executeQuery("Select Song_num from song where Title=" +tit);
						if(!chk.next()) {
							System.out.println("no such song!");
							continue;
						}
						chk=stmt.executeQuery("Select Song_num,Title,Singer from "
								+ "song where Title="+tit);
						int k=1;
						System.out.println("=====================");
						while(chk.next()) {
							tit=chk.getString("Title");
							String singer = chk.getString("Singer");
							int snum=chk.getInt("Song_num");
							System.out.println(k+"."+tit+"-"+singer+" #snum: "+snum);
							k++;
						}
						System.out.println("=====================");
						System.out.println("input the ssn of the song that you want to delete");
						int ssnum=s.nextInt();
						stmt.executeUpdate("Delete from include where Snum="+ssnum);
						stmt.executeUpdate("Delete from song where Song_num="+ssnum);
						System.out.println("#ssn : "+ssnum +" deleted...");
						break;
					case 1: //add song
						s.nextLine();//
						System.out.println("input today's date(ex>'2020-06-09')");
						String date = s.nextLine();
						date="'"+date+"'";
						System.out.println("input the length(seconds) of the song(ex>180)");
						int len = s.nextInt();
						s.nextLine();//
						System.out.println("input the genre of song(ex> 'k-pop')");
						String gen = s.nextLine();
						gen="'"+gen+"'";
						System.out.println("input the title of song(ex> 'dynamite')");
						String title = s.nextLine();
						title = "'"+title+"'";
						System.out.println("input the singer of song(ex> 'BTS')");
						String singer = s.nextLine();
						singer="'"+singer+"'";
						ResultSet rs =stmt.executeQuery("select Max(Song_num) from song");
						if(rs.next()) {
							int snum=rs.getInt(1)+1; 
							stmt.executeUpdate("Insert into song(Enrolldate,Length,Genre,Title,Playnum,"
									+ "Song_num,Singer) values("+
									date+","+len+","+gen+","+title+","+0+","+snum+","+singer+")");
						}
						break;
					case 2: // view user's information
						s.nextLine();
						System.out.println("input the user's id");
						String id = s.nextLine();
						id="'"+id+"'";
						rs= stmt.executeQuery("Select Name,Sex,Leftdays From user Where Id="+id);
						if(!rs.next()) {
							System.out.println("no such user!");
							System.out.println("input the user's id again");
							id=s.nextLine();
							id="'"+id+"'";
						}
						rs= stmt.executeQuery("Select Name,Sex,Leftdays From user Where Id=" + id);
						while(rs.next()) {
						String name = rs.getString("Name");
						String sex = rs.getString(2);
						int days = rs.getInt(3);
						System.out.println("#Name: "+name+" Sex: "+sex+" left days: "+days);}
						
						break;
					case 3://update users information
						s.nextLine();
						System.out.println("enter your ssn");
						String ssn = s. nextLine();
						ssn="'"+ssn+"'";
						ResultSet rs2=stmt.executeQuery("Select Name from manager where Ssn="+ssn);
						if(!rs2.next()) {
							System.out.println("no such manager!");
							continue;
						}
						rs = stmt.executeQuery("Select Id From user where Mgr_ssn="+ssn);
						System.out.println("<Id list of your users>");
						if(rs.next()) {
							String i = rs.getString("Id");
							System.out.println("- "+ i);
							while(rs.next()) {
								i=rs.getString("Id");
								System.out.println("- "+i);
							}
						}else {
							System.out.println("--EMPTY--");
							continue;
						}
						System.out.println(".... Decrement 1 day from the leftdays of your users ....");
						System.out.println(".... Users whose left day is 0 are deleted ....");
						stmt.executeUpdate("Update user set Leftdays=Leftdays-1 where Mgr_ssn="+ssn);
						stmt.executeUpdate("Delete from user where Leftdays<=0");
						break;
					case 4: //birthday event
						Calendar cal=Calendar.getInstance();
						int year = cal.get(cal.YEAR);
						int month = cal.get(cal.MONTH)+1;
						String m = ""+month;
						if(month<=9)
							m="0"+month;
						int dat=cal.get(cal.DATE);
						String d =""+dat;
						if(dat<=9)
							d="0"+dat;
						String today = m+"-"+d; 
						//System.out.println(today);
						System.out.println("#EVENT : +10 days for users whose birthday is today!");
						ResultSet r = stmt.executeQuery("Select Bdate,Name From user");
						while(r.next()) {
							if(today.equals(r.getString(1).substring(5))) {
								System.out.println("<Users' name list>");
								stmt.executeUpdate("Update user set Leftdays=Leftdays+10");
								String n = r.getString("Name");
								System.out.println("- "+n);
							}
						}
						break;
					}
				}
				else if(cmd==2) { //user
					System.out.println("enter your id please(ex> 'nnfe87' )");
					s.nextLine();
					String uid = s.nextLine();
					uid="'"+uid+"'";
					chk = stmt.executeQuery("Select Id,Passwd from user where Id="+uid);
					String rps;
					if(!chk.next()) {
						System.out.println("! Not registered user !");
						continue;
					}else {
						rps=chk.getString(2);
						//System.out.println(rps);
					}
					System.out.println("enter your password (ex> 54f8e78 )");
					String ps = s.nextLine();
					
					if(ps.equals(rps)==false) {
						System.out.println("wrong password!");
						continue;
					}
					int tmp= User();
					switch(tmp) {
					case 0: // create a new playlist
						System.out.println("Create the title of your playlist");
						String pname = s.nextLine();
						pname="'"+pname+"'";
						ResultSet rs = stmt.executeQuery("Select Max(Pl_num) from playlist");
						int newplnum=0;
						if(rs.next()) {
							newplnum = rs.getInt(1)+1;
						}
						stmt.executeUpdate("Insert into playlist(Title,Pl_num,Creater_id) values("+pname+","+
						newplnum+","+uid+")");
						System.out.println("New playlist is created. You can add songs at menu");
						break;
					case 1://show the playlists of an user
						rs=stmt.executeQuery("Select Title from playlist where Creater_id="+uid);
						if(rs.next()) {
							System.out.println("<Your playlists>");
							String t=rs.getString(1);
							System.out.println("- "+t);
							while(rs.next()) {
								t = rs.getString(1);
								System.out.println("- "+t);
							}
						}else {
							System.out.println("--EMPTY--");
						}
						break;
					case 2: //show the songs of the playlist
						rs=stmt.executeQuery("Select Title,Pl_num from playlist where Creater_id="+uid);
						if(rs.next()) {
							System.out.println("<Your playlists>");
							String rt=rs.getString(1);
							int en = rs.getInt(2);
							System.out.println("Playlist: "+rt+" , Pnum : "+en);
							while(rs.next()) {
								rt = rs.getString(1);
								en = rs.getInt(2);
								System.out.println("Playlist: "+rt+" , Pnum : "+en);
							}
						}else {
							System.out.println("<Your playlists>");
							System.out.println("--EMPTY--");
							continue;
						}
						System.out.println("enter a Pnum of the playlist that you want to view");
						int epn = s.nextInt();
						rs=stmt.executeQuery("Select Title,Singer from song where Song_num in ( select"
								+ " Snum from include where Pnum="+epn+")");
						while(rs.next()) {
							String t = rs.getString(1);
							String sg = rs.getString(2);
							System.out.println("#Title : "+t+" #Singer : "+sg);
						}
						break;
					case 3: //delete the playlist
						rs=stmt.executeQuery("Select Title,Pl_num from playlist where Creater_id="+uid);
						if(rs.next()) {
							System.out.println("<Your playlists>");
							String t=rs.getString(1);
							int pn = rs.getInt(2);
							System.out.println("Playlist: "+t+" , Pnum : "+pn);
							while(rs.next()) {
								t = rs.getString(1);
								pn = rs.getInt(2);
								System.out.println("Playlist: "+t+" , Pnum : "+pn);
							}
						}else {
							System.out.println("<Your playlists>");
							System.out.println("--EMPTY--");
							continue;
						}
						System.out.println("enter a Pnum of the playlist that you want to delete");
						int delnum = s.nextInt();
						stmt.executeUpdate("Delete from include where Pnum="+delnum);
						stmt.executeUpdate("Delete from playlist where Pl_num="+delnum);
						System.out.println(".... Deleting ....");
						break;
					case 4: //add or delete song to playlist
						rs=stmt.executeQuery("Select Title,Pl_num from playlist where Creater_id="+uid);
						if(rs.next()) {
							System.out.println("<Your playlists>");
							String t=rs.getString(1);
							int pn = rs.getInt(2);
							System.out.println("Playlist: "+t+" , Pnum : "+pn);
							while(rs.next()) {
								t = rs.getString(1);
								pn = rs.getInt(2);
								System.out.println("Playlist: "+t+" , Pnum : "+pn);
							}
						}else {
							System.out.println("<Your playlists>");
							System.out.println("--EMPTY--");
							continue;
						}
						System.out.println("enter the Pnum of the playlist that you want to edit");
						int num = s.nextInt();
						System.out.println("0.add song 1.delete song");
						
						int cmdd = s.nextInt();
						
						if(cmdd==0) { //add song
							ResultSet kk = stmt.executeQuery("select Pnum,count(*) from "
									+ "include where Pnum="+num+" group by Pnum");
							if(kk.next()) {
							int pnn = kk.getInt(2);
							if(pnn >=100) {
								System.out.println("maximum number of songs is 100");
								continue;
								}
							}
							s.nextLine();
							System.out.println("input the title of the song that you want to add");
							String tit = s.nextLine();
							tit="'"+tit+"'";
							rs=stmt.executeQuery("Select Title,Singer,Song_num from song where Title="+tit);
							if(rs.next()) {
								String title = rs.getString(1);
								String singer= rs.getString(2);
								int snum= rs.getInt(3);
								System.out.println("#Title: "+title+" #Singer : "+singer +" #Snum : "+snum);
								while(rs.next()) {
									title = rs.getString(1);
									singer= rs.getString(2);
									snum=rs.getInt(3);
									System.out.println("#Title: "+title+" #Singer : "+singer+" #Snum : "+snum);
								}
							}else {
								System.out.println("no such song!");
								continue;
							}
							System.out.println("enter the number of the song that you want to add");
							int addsong = s.nextInt();
							stmt.executeUpdate("insert into include(Snum,Pnum) values("+addsong+","+num+")");
						}
						else if(cmdd==1) { //delete song
							rs=stmt.executeQuery("Select Title,Singer,Song_num from song "
									+ "where Song_num In(select Snum "
									+ "from include where Pnum="+num+")");
							if(rs.next()) {
								String st=rs.getString(1);
								String sg=rs.getString(2);
								int dn = rs.getInt(3);
								System.out.println("#Title: "+st+" #Singer : "+sg +" #Snum : "+dn);
								while(rs.next()) {
									st=rs.getString(1);
									sg=rs.getString(2);
									dn = rs.getInt(3);
									System.out.println("#Title: "+st+" #Singer : "+sg +" #Snum : "+dn);
								}
							}else {
								System.out.println("this playlist is empty");
								continue;
							}
							System.out.println("enter the number of the song that"
									+ " you want to delete from the playlist");
							int ddn = s.nextInt();
							stmt.executeUpdate("delete from include where Pnum="+num+" AND Snum="+ddn);
						}
						break;
					case 5: //search the song
						System.out.println("input the title of the song");
						String tit = s.nextLine();
						tit="'"+tit+"'";
						rs=stmt.executeQuery("Select Title,Singer from song where Title="+tit);
						if(rs.next()) {
							String title = rs.getString(1);
							String singer= rs.getString(2);
							System.out.println("#Title: "+title+" #Singer : "+singer);
							while(rs.next()) {
								title = rs.getString(1);
								singer= rs.getString(2);
								System.out.println("#Title: "+title+" #Singer : "+singer);
							}
						}else {
							System.out.println("no such song!");
						}
						stmt.executeUpdate("Update song set Playnum=Playnum+1 where Title="+tit);
						break;
					case 6: // top-10 chart
						System.out.println("<melon top-10>");
						rs=stmt.executeQuery("Select Title,Singer from song order by Playnum desc");
						int rank=1;
						while(rs.next()) {
							String title = rs.getString(1);
							String singer =rs.getString(2);
							System.out.printf("%3d",rank);
							System.out.println(": "+title+" - "+singer);
							rank++;
							if(rank==11)
								break;
						}
						break;
					}
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
