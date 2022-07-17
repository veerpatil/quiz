import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizUtility {
    public static void InsertResult(HashMap<Integer, List<String>> result, Connection connection) throws SQLException {


        PreparedStatement statement = connection.prepareStatement("Insert into quizuseranswers (QuestionID, Answer,UserID) Values (?,?,?)");

        try {
            for (Map.Entry<Integer, List<String>> entry : result.entrySet()) {
                statement.setInt(1, entry.getKey());
                statement.setString(2, entry.getValue().get(0));
                statement.setInt(3, Integer.parseInt( entry.getValue().get(1)));
                statement.executeUpdate();
            }
        } catch (SQLException ex) {

        }
    }

    public static int getUserID(String firstName, Connection connection)
    {
        int userID=0;
        try {
            PreparedStatement statement = connection.prepareStatement("Select * from  userinfomaster where firstname=?");
            statement.setString(1, firstName);
          ResultSet resultSet=    statement.executeQuery();

          while (resultSet.next())
          {
              userID = resultSet.getInt("userId");
          }

        }
        catch (SQLException ex)
        {
            System.out.println("Error while executing user details");
        }

        return userID;
    }

    public static void registerUser(String firstName, String lastName , Connection connection)
    {

        try {
            PreparedStatement statement = connection.prepareStatement("Insert into userinfomaster (firstname, lastname) Values (?,?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.executeUpdate();

        }
        catch (SQLException ex)
        {
            System.out.println("Error while executing user details");
        }
    }

    public  static  ResultSet getData(String query, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        System.out.println(preparedStatement);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }

    public static Connection getConnection() throws SQLException {
       Connection  connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/quizdatabase?useSSL=false", "root", "root");
       return  connection;
    }

    public static  ResultSet getResultForUser(int userID, Connection connection )
  {
      ResultSet rs =null;
      try {
          String query = "Select count(questionID) as TotalQuestion, userID, Answer FROM quizdatabase.quizuseranswers " +
                  "where userID= ? group by userID, answer";
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setInt(1,userID);
          rs = preparedStatement.executeQuery();
      }
      catch (SQLException exception)
      {

      }
      return rs;
  }
}
