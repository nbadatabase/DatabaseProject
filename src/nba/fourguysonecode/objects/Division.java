package nba.fourguysonecode.objects;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Division {
    int div_id;
    int conf_id;
    String div_name;


    public Division(int div_id, int conf_id, String div_name) {
        this.div_id = div_id;
        this.conf_id = conf_id;
        this.div_name = div_name;
    }

    public int getDiv_id() {return div_id;}
    public int getConf_id() {return conf_id;}
    public String getDiv_name() {return div_name;}
}
