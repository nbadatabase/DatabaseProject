package nba.fourguysonecode.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Division
{
    private final SimpleIntegerProperty div_id;
    private final SimpleIntegerProperty conf_id;
    private final SimpleStringProperty div_name;


    public Division(int div_id, int conf_id, String div_name)
    {
        this.div_id = new SimpleIntegerProperty(div_id);
        this.conf_id = new SimpleIntegerProperty(conf_id);
        this.div_name = new SimpleStringProperty(div_name);
    }

    public Division(String[] data)
    {
        this.div_id = new SimpleIntegerProperty(Integer.parseInt(data[0]));
        this.conf_id = new SimpleIntegerProperty(Integer.parseInt(data[1]));
        this.div_name = new SimpleStringProperty(data[2]);
    }

    public Division(ResultSet rowData) throws SQLException
    {
        // Initialize fields based on the row data.
        this.div_id = new SimpleIntegerProperty(rowData.getInt(1));
        this.conf_id = new SimpleIntegerProperty(rowData.getInt(2));
        this.div_name = new SimpleStringProperty(rowData.getString(3));
    }

    public int getDiv_id() { return div_id.get(); }
    public int getConf_id() { return conf_id.get(); }
    public String getDiv_name() { return div_name.get(); }
}
