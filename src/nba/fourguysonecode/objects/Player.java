package nba.fourguysonecode.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Player {
    private final SimpleIntegerProperty player_id;
    private final SimpleIntegerProperty team_id;
    private final SimpleStringProperty first_name;
    private final SimpleStringProperty last_name;
    private final SimpleStringProperty dob;

    public Player(int id,
                  int team_id,
                  String first_name,
                  String last_name,
                  String dob)
    {
        this.player_id = new SimpleIntegerProperty(id);
        this.team_id = new SimpleIntegerProperty(team_id);
        this.first_name = new SimpleStringProperty(first_name);
        this.last_name = new SimpleStringProperty(last_name);
        this.dob = new SimpleStringProperty(dob);
    }

    public Player(String[] data) throws Exception
    {
        this.player_id = new SimpleIntegerProperty(Integer.parseInt(data[0]));
        this.team_id = new SimpleIntegerProperty(Integer.parseInt(data[1]));
        this.first_name = new SimpleStringProperty(data[2]);
        this.last_name = new SimpleStringProperty(data[3]);
        this.dob = new SimpleStringProperty(data[4]);
    }

    public int getPlayer_id()
    {
        return player_id.get();
    }

    public int getTeam_id()
    {
        return team_id.get();
    }

    public String getFirst_name()
    {
        return first_name.get();
    }

    public String getLast_name()
    {
        return last_name.get();
    }

    public String getDob()
    {
        return dob.get();
    }
}
