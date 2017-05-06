package nba.fourguysonecode.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Team {
    private final SimpleIntegerProperty team_id;
    private final SimpleIntegerProperty div_id;
    private final SimpleStringProperty team_name;
    private final SimpleStringProperty location;
    private final SimpleIntegerProperty win;
    private final SimpleIntegerProperty loss;

    public Team(int team_id, int div_id, String team_name, String location, int win, int loss) {
        this.team_id = new SimpleIntegerProperty(team_id);
        this.div_id = new SimpleIntegerProperty(div_id);
        this.team_name = new SimpleStringProperty(team_name);
        this.location = new SimpleStringProperty(location);
        this.win = new SimpleIntegerProperty(win);
        this.loss = new SimpleIntegerProperty(loss);
    }

    public Team(String[] data){
        this.team_id = new SimpleIntegerProperty(Integer.parseInt(data[0]));
        this.div_id = new SimpleIntegerProperty(Integer.parseInt(data[1]));
        this.team_name = new SimpleStringProperty(data[2]);
        this.location = new SimpleStringProperty(data[3]);
        this.win = new SimpleIntegerProperty(Integer.parseInt(data[4]));
        this.loss = new SimpleIntegerProperty(Integer.parseInt(data[5]));
    }

    public Team(ResultSet result) throws SQLException
    {
        // Initialize the fields using the result set.
        this.team_id = new SimpleIntegerProperty(result.getInt(1));
        this.div_id = new SimpleIntegerProperty(result.getInt(2));
        this.team_name = new SimpleStringProperty(result.getString(3));
        this.location = new SimpleStringProperty(result.getString(4));
        this.win = new SimpleIntegerProperty(result.getInt(5));
        this.loss = new SimpleIntegerProperty(result.getInt(6));
    }

    public int getTeam_id() {return team_id.get();}
    public int getDiv_id() {return div_id.get();}
    public int getWin(){return win.get();}
    public int getLoss(){return loss.get();}
    public String getTeam_name() {return team_name.get();}
    public String getLocation() {return location.get();}
}
