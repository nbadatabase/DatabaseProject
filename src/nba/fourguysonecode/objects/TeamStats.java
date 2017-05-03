package nba.fourguysonecode.objects;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by joshuasellers on 4/3/17.
 */
public class TeamStats
{
    int team_id;
    float tot_pts;
    int fg_att;
    int fg_made;
    int three_att;
    int three_made;
    int free_att;
    int free_made;
    int off_rebound;
    int def_rebound;
    int assists;
    int steals;
    int blocks;
    int turnovers;

    public TeamStats(int team_id, float tot_pts, int fg_att,
                     int fg_made, int three_att, int three_made, int free_att,
                     int free_made, int off_rebound, int def_rebound, int assists,
                     int steals, int blocks, int turnovers)
    {
        this.team_id = team_id;
        this.tot_pts = tot_pts;
        this.fg_att = fg_att;
        this.fg_made = fg_made;
        this.three_att = three_att;
        this.three_made = three_made;
        this.free_att = free_att;
        this.free_made = free_made;
        this.off_rebound = off_rebound;
        this.def_rebound = def_rebound;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.turnovers = turnovers;
    }

    public TeamStats(String[] data)
    {
        this.team_id = Integer.parseInt(data[0]);
        this.tot_pts = Float.parseFloat(data[1]);
        this.fg_att = Integer.parseInt(data[2]);
        this.fg_made = Integer.parseInt(data[3]);
        this.three_att = Integer.parseInt(data[4]);
        this.three_made = Integer.parseInt(data[5]);
        this.free_att = Integer.parseInt(data[6]);
        this.free_made = Integer.parseInt(data[7]);
        this.off_rebound = Integer.parseInt(data[8]);
        this.def_rebound = Integer.parseInt(data[9]);
        this.assists = Integer.parseInt(data[10]);
        this.steals =Integer.parseInt(data[11]);
        this.blocks =Integer.parseInt(data[12]);
        this.turnovers = Integer.parseInt(data[13]);
    }
    
    public TeamStats(ResultSet result) throws SQLException
    {
        this.team_id = result.getInt(1);
        this.tot_pts = result.getFloat(2);
        this.fg_att = result.getInt(3);
        this.fg_made = result.getInt(4);
        this.three_att = result.getInt(5);
        this.three_made = result.getInt(6);
        this.free_att = result.getInt(7);
        this.free_made = result.getInt(8);
        this.off_rebound = result.getInt(9);
        this.def_rebound = result.getInt(10);
        this.assists = result.getInt(11);
        this.steals = result.getInt(12);
        this.blocks = result.getInt(13);
        this.turnovers = result.getInt(14);
    }

    public int getTeam_id() {return team_id;}
    public float getTot_pts() {return tot_pts;}
    public int getFg_att() {return fg_att;}
    public int getFg_made() {return fg_made;}
    public int getThree_att() {return three_att;}
    public int getThree_made() {return three_made;}
    public int getFree_att() {return free_att;}
    public int getFree_made() {return free_made;}
    public int getOff_rebound() {return off_rebound;}
    public int getDef_rebound() {return def_rebound;}
    public int getAssists() {return assists;}
    public int getSteals() {return steals;}
    public int getBlocks() {return blocks;}
    public int getTurnovers() {return  turnovers;}
}
