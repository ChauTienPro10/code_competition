package myself.programing.coding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.datasource.impl.OracleDataSource;

public class Oracle {

    public static void main(String[] args) throws SQLException {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
        String username = "DEVELOPER";
        String password = "tien22012003";

        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@localhost:1521/FREEPDB1"); // jdbc:oracle:thin@[hostname]:[port]/[DB service name]
        ods.setUser("developer");
        ods.setPassword("tien22012003");
        Connection conn = ods.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT 'Hello World!' FROM dual");
        ResultSet rslt = stmt.executeQuery();
        while (rslt.next()) {
            System.out.println(rslt.getString(1));
        }
    }
}
