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

    public Division(String[] data){
        this.div_id = Integer.parseInt(data[0]);
        this.conf_id = Integer.parseInt(data[1]);
        this.div_name = data[2];
    }

    public int getDiv_id() {return div_id;}
    public int getConf_id() {return conf_id;}
    public String getDiv_name() {return div_name;}
}
