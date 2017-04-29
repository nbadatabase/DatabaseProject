package nba.fourguysonecode.objects;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by joshuasellers on 4/2/17.
 */
public class Conference
{
    private final SimpleIntegerProperty conf_id;
    private final SimpleStringProperty conf_name;


    public Conference(int conf_id, String conf_name)
    {
        this.conf_id = new SimpleIntegerProperty(conf_id);
        this.conf_name = new SimpleStringProperty(conf_name);
    }

    public Conference(String[] data)
    {
        this.conf_id = new SimpleIntegerProperty(Integer.parseInt(data[0]));
        this.conf_name = new SimpleStringProperty(data[1]);
    }

    public Conference(ResultSet rowData) throws SQLException
    {
        // Initialize fields from the row data.
        this.conf_id = new SimpleIntegerProperty(rowData.getInt(1));
        this.conf_name = new SimpleStringProperty(rowData.getString(2));
    }

    public int getConf_id() { return conf_id.get(); }
    public String getConf_name() { return conf_name.get(); }
}
