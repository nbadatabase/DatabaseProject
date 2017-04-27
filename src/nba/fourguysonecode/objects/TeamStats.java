package nba.fourguysonecode.objects;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by joshuasellers on 4/3/17.
 */
public class TeamStats {
    private final SimpleIntegerProperty team_id;
    private final SimpleFloatProperty tot_pts;
    private final SimpleIntegerProperty fg_att;
    private final SimpleIntegerProperty fg_made;
    private final SimpleIntegerProperty three_att;
    private final SimpleIntegerProperty three_made;
    private final SimpleIntegerProperty free_att;
    private final SimpleIntegerProperty free_made;
    private final SimpleIntegerProperty off_rebound;
    private final SimpleIntegerProperty def_rebound;
    private final SimpleIntegerProperty assists;
    private final SimpleIntegerProperty steals;
    private final SimpleIntegerProperty blocks;
    private final SimpleIntegerProperty turnovers;

    public TeamStats(int team_id, float tot_pts, int fg_att,
                     int fg_made, int three_att, int three_made, int free_att,
                     int free_made, int off_rebound, int def_rebound, int assists,
                     int steals, int blocks, int turnovers){
        this.team_id = new SimpleIntegerProperty(team_id);
        this.tot_pts = new SimpleFloatProperty(tot_pts);
        this.fg_att = new SimpleIntegerProperty(fg_att);
        this.fg_made = new SimpleIntegerProperty(fg_made);
        this.three_att = new SimpleIntegerProperty(three_att);
        this.three_made = new SimpleIntegerProperty(three_made);
        this.free_att = new SimpleIntegerProperty(free_att);
        this.free_made = new SimpleIntegerProperty(free_made);
        this.off_rebound = new SimpleIntegerProperty(off_rebound);
        this.def_rebound = new SimpleIntegerProperty(def_rebound);
        this.assists = new SimpleIntegerProperty(assists);
        this.steals = new SimpleIntegerProperty(steals);
        this.blocks = new SimpleIntegerProperty(blocks);
        this.turnovers = new SimpleIntegerProperty(turnovers);
    }

    public TeamStats(String[] data){
        this.team_id = new SimpleIntegerProperty(Integer.parseInt(data[0]));
        this.tot_pts = new SimpleFloatProperty(Float.parseFloat(data[1]));
        this.fg_att = new SimpleIntegerProperty(Integer.parseInt(data[2]));
        this.fg_made = new SimpleIntegerProperty(Integer.parseInt(data[3]));
        this.three_att = new SimpleIntegerProperty(Integer.parseInt(data[4]));
        this.three_made = new SimpleIntegerProperty(Integer.parseInt(data[5]));
        this.free_att = new SimpleIntegerProperty(Integer.parseInt(data[6]));
        this.free_made = new SimpleIntegerProperty(Integer.parseInt(data[7]));
        this.off_rebound = new SimpleIntegerProperty(Integer.parseInt(data[8]));
        this.def_rebound = new SimpleIntegerProperty(Integer.parseInt(data[9]));
        this.assists = new SimpleIntegerProperty(Integer.parseInt(data[10]));
        this.steals =new SimpleIntegerProperty(Integer.parseInt(data[11]));
        this.blocks =new SimpleIntegerProperty(Integer.parseInt(data[12]));
        this.turnovers = new SimpleIntegerProperty(Integer.parseInt(data[13]));
    }

    public int getTeam_id() {return team_id.get();}
    public float getTot_pts() {return tot_pts.get();}
    public int getFg_att() {return fg_att.get();}
    public int getFg_made() {return fg_made.get();}
    public int getThree_att() {return three_att.get();}
    public int getThree_made() {return three_made.get();}
    public int getFree_att() {return free_att.get();}
    public int getFree_made() {return free_made.get();}
    public int getOff_rebound() {return off_rebound.get();}
    public int getDef_rebound() {return def_rebound.get();}
    public int getAssists() {return assists.get();}
    public int getSteals() {return steals.get();}
    public int getBlocks() {return blocks.get();}
    public int getTurnovers() {return  turnovers.get();}
}
