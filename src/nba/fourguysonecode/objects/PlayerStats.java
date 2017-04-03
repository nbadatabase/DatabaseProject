package nba.fourguysonecode.objects;

/**
 * Created by joshuasellers on 4/3/17.
 */
public class PlayerStats {
    int player_id;
    int games_played;
    int tot_mins;
    int tot_pts;
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

    public PlayerStats(int player_id, int games_played, int tot_mins, int tot_pts, int fg_att, int fg_made,
            int three_att, int three_made, int free_att, int free_made, int off_rebound, int def_rebound,
            int assists, int steals, int blocks, int turnovers){
        this.player_id = player_id;
        this.games_played = games_played;
        this.tot_mins = tot_mins;
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
    public int getPlayer_id() {return player_id;}
    public int getGames_played() {return games_played;}
    public int getTot_mins() {return tot_mins;}
    public int getTot_pts() {return tot_pts;}
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
