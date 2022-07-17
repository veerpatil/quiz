import java.io.IOException;
import java.sql.*;
import java.util.*;

public class QuizConnection {

    private static final String QUERY = "SELECT * FROM questioninfomaster order by RAND()";
    public static void main(String[] argv) throws SQLException, IOException {
        HashMap<Integer, List<String>> questionCorrectAnswer = new HashMap<Integer ,List<String>>();
        Scanner scanner = new Scanner(System.in);
        int option=0;
        int userID=0;
        int correctAnswer=0, wrongAnswer=0;
        String firstName="",lastName="";
        do{
        System.out.println("Choose One of Following Options ");
        System.out.println("1: Register to Quiz");
        System.out.println("2: Appear for Quiz");
        System.out.println("3: Retrieve Result");
        System.out.println("4: Get All Result");
        System.out.println("5: exit");
        option = scanner.nextInt();


        switch (option) {
            case 1:
                System.out.println("Register to start the quiz");
                System.out.println("Enter First Name");
                firstName = scanner.next();
                System.out.println("Enter Last Name");
                lastName = scanner.next();
                QuizUtility.registerUser(firstName, lastName, QuizUtility.getConnection());
                System.out.println("Registration completed successfully");
                userID = QuizUtility.getUserID(firstName, QuizUtility.getConnection());
                break;
            case 2:
                ResultSet rs = QuizUtility.getData(QUERY, QuizUtility.getConnection());
                while (rs.next()) {
                    int id = rs.getInt("QuestionID");
                    String question = rs.getString("QuestionDescription");
                    String option1 = rs.getString("QuestionOption1");
                    String option2 = rs.getString("QuestionOption2");
                    String option3 = rs.getString("QuestionOption3");
                    String option4 = rs.getString("QuestionOption4");
                    String answer = rs.getString("CorrectAnswer");
                    System.out.println("*****************************************************");
                    System.out.println(question);
                    System.out.println("\t" + "A: " + option1);
                    System.out.println("\t" + "B: " + option2);
                    System.out.println("\t" + "C: " + option3);
                    System.out.println("\t" + "D: " + option4);

                    String ansGiven = scanner.next();
                    if (answer.equals(ansGiven)) {
                        questionCorrectAnswer.put(id, Arrays.asList("C", Integer.toString(userID)));
                    } else {
                        questionCorrectAnswer.put(id, Arrays.asList("W", Integer.toString(userID)));
                    }

                }
                QuizUtility.InsertResult(questionCorrectAnswer, QuizUtility.getConnection());
                System.out.println("**********Answer Submitted Successfully");
                break;
            case 3:
                ResultSet finalResult = QuizUtility.getResultForUser(userID, QuizUtility.getConnection());

                while (finalResult.next()) {

                    if (finalResult.getString("Answer").equals("C")) {
                        correctAnswer = finalResult.getInt("TotalQuestion");

                    } else if (finalResult.getString("Answer").equals("W")) {
                        wrongAnswer = finalResult.getInt("TotalQuestion");
                    }

                }
                int total = correctAnswer + wrongAnswer;
                String grade = "";
                switch (correctAnswer) {
                    case 10:
                    case 9:
                    case 8:
                        grade = "A";
                        break;
                    case 6:
                    case 7:
                        grade = "B";
                        break;
                    case 5:
                        grade = "C";
                        break;
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                    case 0:
                        grade = "D";
                        break;

                }
                System.out.println("First Name " + "\t" + "Last Name" + "\t" + "TotalQuestions" + "\t" + "CorrectAnswer" + "\t" + "WrongAnswer" + "\t" + "Grade");
                System.out.println(firstName + "\t\t\t" + lastName + "\t\t\t" + total + "\t\t\t" + correctAnswer + "\t\t\t\t" + wrongAnswer + "\t\t\t" + grade);
                break;
            case 4:
                ResultSet allUser = QuizUtility.getData("Select distinct firstname, lastname,count(questionID) as TotalQuestion, count(if(Answer=\"W\",1,null)) WrongAnswer,count(if(Answer=\"C\",1,null)) CorrectAnswer  from quizdatabase.userinfomaster u, quizdatabase.quizuseranswers q where \n" +
                        "u.userID = q.userID \n" +
                        "group by  u.userID", QuizUtility.getConnection());
                System.out.printf("%10s %10s %10s %10s %10s %8s", "FIRST_NAME", "LAST_NAME", "TOTAL_QUESTIONS", "CORRECT_ANSWER", "WRONG_ANSWERS", "GRADE");
                System.out.println();
                while (allUser.next()) {
                    if (allUser.getInt("CorrectAnswer") >= 8)
                        grade = "A";
                    else if (allUser.getInt("CorrectAnswer") < 8 && allUser.getInt("CorrectAnswer") >= 6)
                        grade = "B";
                    else if (allUser.getInt("CorrectAnswer") == 5)
                        grade = "C";
                    else
                        grade = "D";
                    System.out.format("%10s %10s %10s %10s %15s %13s", allUser.getString("firstname"), allUser.getString("lastname"), allUser.getInt("TotalQuestion"), allUser.getInt("CorrectAnswer"), allUser.getInt("WrongAnswer"), grade);
                    System.out.println();


                }
            case 5:
                break;

        }
        System.out.println("______________________________________________________________________");
        }while (option!=5);
    }


}

