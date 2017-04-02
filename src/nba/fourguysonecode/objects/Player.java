package nba.fourguysonecode.objects;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Player {
    int player_id;
    int team_id;
    String first_name;
    String last_name;
    String dob;

    public Player(int id, int team_id, String first_name, String last_name, String dob) {
        this.player_id = id;
        this.team_id = team_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
    }

    public int getPlayer_id() {return player_id;}
    public int getTeam_id() {return team_id;}
    public String getFirst_name() {return first_name;}
    public String getLast_name() {return last_name;}
    public String getDob() {return dob;}
}
