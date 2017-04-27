package nba.fourguysonecode.objects;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by joshuasellers on 4/3/17.
 */
public class PlayerStats {
    private final SimpleIntegerProperty player_id;
    private final SimpleIntegerProperty games_played;
    private final SimpleFloatProperty tot_mins;
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

    public PlayerStats(int player_id, int games_played, float tot_mins, float tot_pts, int fg_att, int fg_made,
            int three_att, int three_made, int free_att, int free_made, int off_rebound, int def_rebound,
            int assists, int steals, int blocks, int turnovers){
        this.player_id = new SimpleIntegerProperty(player_id);
        this.games_played = new SimpleIntegerProperty(games_played);
        this.tot_mins = new SimpleFloatProperty(tot_mins);
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

    public PlayerStats(String[] data){
        this.player_id = new SimpleIntegerProperty(Integer.parseInt(data[0]));
        this.games_played = new SimpleIntegerProperty(Integer.parseInt(data[1]));
        this.tot_mins = new SimpleFloatProperty(Float.parseFloat(data[2]));
        this.tot_pts = new SimpleFloatProperty(Float.parseFloat(data[3]));
        this.fg_att = new SimpleIntegerProperty(Integer.parseInt(data[4]));
        this.fg_made = new SimpleIntegerProperty(Integer.parseInt(data[5]));
        this.three_att = new SimpleIntegerProperty(Integer.parseInt(data[6]));
        this.three_made = new SimpleIntegerProperty(Integer.parseInt(data[7]));
        this.free_att = new SimpleIntegerProperty(Integer.parseInt(data[8]));
        this.free_made = new SimpleIntegerProperty(Integer.parseInt(data[9]));
        this.off_rebound = new SimpleIntegerProperty(Integer.parseInt(data[10]));
        this.def_rebound = new SimpleIntegerProperty(Integer.parseInt(data[11]));
        this.assists = new SimpleIntegerProperty(Integer.parseInt(data[12]));
        this.steals = new SimpleIntegerProperty(Integer.parseInt(data[13]));
        this.blocks = new SimpleIntegerProperty(Integer.parseInt(data[14]));
        this.turnovers = new SimpleIntegerProperty(Integer.parseInt(data[15]));
    }
    public int getPlayer_id() {return player_id.get();}
    public int getGames_played() {return games_played.get();}
    public float getTot_mins() {return tot_mins.get();}
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
