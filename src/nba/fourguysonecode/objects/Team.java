package nba.fourguysonecode.objects;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Team {
    int team_id;
    int div_id;
    String team_name;
    String location;

    public Team(int team_id, int div_id, String team_name, String location) {
        this.team_id = team_id;
        this.div_id = div_id;
        this.team_name = team_name;
        this.location = location;
    }

    public Team(String[] data){
        this.team_id = Integer.parseInt(data[0]);
        this.div_id = Integer.parseInt(data[1]);
        this.team_name = data[2];
        this.location = data[3];
    }

    public int getTeam_id() {return team_id;}
    public int getDiv_id() {return div_id;}
    public String getTeam_name() {return team_name;}
    public String getLocation() {return location;}
}
