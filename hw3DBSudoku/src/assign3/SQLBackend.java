package assign3;

import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class SQLBackend extends AbstractTableModel {

    private String sql_user = MyDBInfo.MYSQL_USERNAME;
    private String sql_server = MyDBInfo.MYSQL_DATABASE_SERVER;
    private String sql_password = MyDBInfo.MYSQL_PASSWORD;
    private String sql_db = MyDBInfo.MYSQL_DATABASE_NAME;
    private String sql_table = "metropolises";

    private Statement st;
    private ResultSet rs;

    public SQLBackend () {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + sql_server, sql_user, sql_password);
            st = connection.createStatement();
            st.executeQuery("use "+sql_db);
            rs = getTable();
            fireTableDataChanged();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getTable(){
        String sql = "SELECT * FROM "+ sql_table;
        try {
            return st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(String metropolis, String continent, long population) {
        String sql = "INSERT INTO " + sql_table + " VALUES(" + metropolis +","+ continent +","+population+")";
        try {
            rs = st.executeQuery(sql);
            fireTableDataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void search(String metropolis, String continent, long population, )

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
