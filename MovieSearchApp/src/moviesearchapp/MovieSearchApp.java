package moviesearchapp;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MovieSearchApp extends JFrame {
    private JTextField txtTitle;
    private JButton butSearch;
    private JList list;
    private JScrollPane scrollResults;
    private ArrayList<String> movieList;

    public MovieSearchApp() {
        setTitle("Movie Search App");
        setLayout(null);

        DefaultListModel dlm = new DefaultListModel();
        list = new JList(dlm);
        movieList = new ArrayList();
        txtTitle = new JTextField();
        txtTitle.setBounds(30, 30, 150, 25);
        butSearch = new JButton("Title Search");
        butSearch.setBounds(200, 30, 120, 25);
        scrollResults = new JScrollPane(list);
        scrollResults.setBounds(30, 85, 290, 150);

        try {
            Properties props = new Properties();
            //Locate text properties file
            String fileName = "config.properties";//in local folder of package
            FileInputStream fis = new FileInputStream(fileName);
            //load the content of properties into java.util.Properties class
            props.load(fis);
            //A MysqlDataSource is created
            MysqlDataSource ds = new MysqlDataSource();
            //datasource properties are set
            ds.setURL(props.getProperty("mysql.url"));
            ds.setUser(props.getProperty("mysql.username"));
            ds.setPassword(props.getProperty("mysql.password"));

            String query = ("SELECT * FROM movies");
            //create jdbc connection object data source
            Connection con = ds.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                //get the second column (title) and display it
                String movieName = rs.getString(2);
                movieList.add(movieName);
            }
        } catch (IOException | SQLException e) {
            txtTitle.setText("Failed to Build Movie List");
            System.out.println(e);
        }

        butSearch.addActionListener((ActionEvent ae) -> {
            try {
                dlm.removeAllElements();

                for (String curVal : movieList) {
                    String tempVal = curVal.toLowerCase();
                    if (tempVal.contains(txtTitle.getText().toLowerCase())) {
                        dlm.addElement(curVal);
                    }
                }
            } catch (Exception e) {
                txtTitle.setText("Something Went Wrong!");
                System.out.println(e);
            }
        });

        add(txtTitle);
        add(butSearch);
        add(scrollResults);

        setSize(360, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MovieSearchApp();
    }
}
