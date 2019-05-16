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
            st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

    public void search(String metropolis, String continent, long population, String population_filter, String else_filter){
        String sql = "SELECT * FROM " + sql_table +_" WHERE ";
        String equalitySymbol = "=";
        if(else_filter != "Exact Match") {
            equalitySymbol = "like";
        }
        if(metropolis != ""){
            sql += "metropolis = " + metropolis;
        }
        if(continent != ""){
            sql += "continent = " + continent;
        }
        if(metropolis != ""){
            sql += "metropolis = " + metropolis;
        }
    }

    @Override
    public int getRowCount() {
        int rowCount = 0;
        try {
            rs.last();
            rowCount = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            return rsmd.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            int k = 0;
            while(rs.next()) {
                k++;
                if(k == rowIndex+1) {
                    return rs.getObject(columnIndex+1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
