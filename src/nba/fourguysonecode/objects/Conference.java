package NBA.db.objects;

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

    public int getConf_id() {return conf_id;}
    public String getConf_name() {return conf_name;}
}
