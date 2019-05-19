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
    private Statement st1;
    private ResultSet rs;

    /**
     * Initialize SQLBackend Class which connects to DataBase and extends AbstractTableModel
     */
    public SQLBackend () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + sql_server+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", sql_user, sql_password);
            st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st1 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.executeQuery("use "+sql_db);
            rs = getTable();
            fireTableDataChanged();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns whole table from SQL as ResultSet
     * @return ResultSet
     */
    public ResultSet getTable(){
        String sql = "SELECT * FROM "+ sql_table;
        try {
            return st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Following method inserts metropolis information and updates table on frame.
     * @param metropolis Name of Metropolis
     * @param continent Name of Continent
     * @param population Number of Population
     */
    public void insert(String metropolis, String continent, long population) {
        String sql = "INSERT INTO " + sql_table + " VALUES('" + metropolis +"','"+ continent +"','"+population+"')";
        try {
            st.executeUpdate(sql);
            rs = getTable();
            fireTableDataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search inside SQL Table using given filters.
     * @param metropolis Name of Metropolis
     * @param continent Name of Continent
     * @param population Number of Population
     * @param population_filter if searched population is less or great than given
     * @param else_filter if metropolis or continent is in exact match or partial.
     */
    public void search(String metropolis, String continent, long population, String population_filter, String else_filter){
        String sql = "SELECT * FROM " + sql_table;
        String equalitySymbol = "=";
        String likeSymbol = "";
        String compareSign = ">";
        boolean isAnother = false;

        if(else_filter != "Exact Match") {
            equalitySymbol = "like";
            likeSymbol = "%";
        }

        if(population_filter != "Population Larger Than") {
            compareSign = "<=";
        }

        if(metropolis != ""||continent != ""||population >= 0) sql += " WHERE ";
        if(metropolis != ""){
            isAnother = true;
            sql += " metropolis " + equalitySymbol + " '" + likeSymbol + metropolis + likeSymbol+"' ";
        }
        if(continent != ""){
            if(isAnother) sql += " AND ";
            isAnother = true;
            sql += " continent " + equalitySymbol + " '" + likeSymbol + continent + likeSymbol+"' ";
        }
        if(population >= 0){
            if(isAnother) sql += " AND ";
            isAnother = true;
            sql += " population " + compareSign + " " + population;
        }

        try {
            rs = st.executeQuery(sql);
            fireTableDataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search inside SQL Table using given filters.
     * @param columnIndex Number of index to return header name
     * @return String at column index returns name.
     */
    public String getHeaderNameAt(int columnIndex) {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+ sql_table +"'";
        try {
            ResultSet headerSet = st1.executeQuery(sql);
            int i = 0;
            while(headerSet.next()){
                if(i == columnIndex) {
                    return headerSet.getString(1);
                }
                i++;
            }
            headerSet.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
        Object result = null;
        try {
            int k = 0;
            while(rs.next()) {
                k++;
                if(k == rowIndex+1) {
                    result = rs.getObject(columnIndex+1);
                }
            }
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
