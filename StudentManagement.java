import java.sql.*;
import java.util.Scanner;

// Model Class
class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}

// Controller Class
class StudentController {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void createStudent(Student student) throws SQLException {
        String query = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, student.getStudentID());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setDouble(4, student.getMarks());
            pstmt.executeUpdate();
            System.out.println("Student added successfully.");
        }
    }

    public static void readStudents() throws SQLException {
        String query = "SELECT * FROM Student";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("StudentID | Name | Department | Marks");
            while (rs.next()) {
                System.out.println(rs.getInt("StudentID") + " | " + rs.getString("Name") + " | " + rs.getString("Department") + " | " + rs.getDouble("Marks"));
            }
        }
    }

    public static void updateStudent(int id, String name, String department, double marks) throws SQLException {
        String query = "UPDATE Student SET Name = ?, Department = ?, Marks = ? WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, department);
            pstmt.setDouble(3, marks);
            pstmt.setInt(4, id);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Student updated successfully." : "Student not found.");
        }
    }

    public static void deleteStudent(int id) throws SQLException {
        String query = "DELETE FROM Student WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Student deleted successfully." : "Student not found.");
        }
    }
}

// View Class
public class StudentManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. Create Student");
            System.out.println("2. Read Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Student ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Department: ");
                        String department = scanner.nextLine();
                        System.out.print("Enter Marks: ");
                        double marks = scanner.nextDouble();
                        StudentController.createStudent(new Student(id, name, department, marks));
                    }
                    case 2 -> StudentController.readStudents();
                    case 3 -> {
                        System.out.print("Enter Student ID to update: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter new Department: ");
                        String department = scanner.nextLine();
                        System.out.print("Enter new Marks: ");
                        double marks = scanner.nextDouble();
                        StudentController.updateStudent(id, name, department, marks);
                    }
                    case 4 -> {
                        System.out.print("Enter Student ID to delete: ");
                        int id = scanner.nextInt();
                        StudentController.deleteStudent(id);
                    }
                    case 5 -> exit = true;
                    default -> System.out.println("Invalid choice, try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }
}
