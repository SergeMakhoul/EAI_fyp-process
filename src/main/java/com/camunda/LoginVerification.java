package com.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.*;

public class LoginVerification implements JavaDelegate {
    Connection createConnection() throws SQLException{
        String driver = "org.h2.Driver";
        String Url = "jdbc:h2:~/camunda-db";
        try{
            Class.forName(driver);
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Connection connection = DriverManager.getConnection(Url, "","");
        return connection;
    }
    void closeConnection(Connection connection) throws SQLException{
        connection.close();
    }

    public Boolean notMain(String username, String password) throws SQLException {
        Boolean success = false;
        Connection connection;
        connection = createConnection();
        String query = "SELECT * FROM LOGIN";
        Statement st = connection.createStatement();
        ResultSet res = st.executeQuery(query);

        while (res.next()){
            String user = res.getString(2);
            String pwd = res.getString(3);
            if(username.equals(user) && password.equals(pwd)) {
                success = true;
                break;
            }
        }
        closeConnection (connection);
        return success;
    }


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("username");
        String password = (String) execution.getVariable("password");
        Boolean success = notMain(username,password);
        execution.setVariable("success",success);
    }
}
