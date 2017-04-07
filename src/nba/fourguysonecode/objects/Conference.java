package nba.fourguysonecode.objects;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Conference {
    int conf_id;
    String conf_name;


    public Conference(int conf_id, String conf_name) {
        this.conf_id = conf_id;
        this.conf_name = conf_name;
    }

    public Conference(String[] data){
        this.conf_id = Integer.parseInt(data[0]);
        this.conf_name = data[1];

    }

    public int getConf_id() {return conf_id;}
    public String getConf_name() {return conf_name;}
}
