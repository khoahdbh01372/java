import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student {
    private String id;
    private String name;
    private double marks;

    public Student(String id, String name, double marks) {
        if (marks < 0 || marks > 10) {
            throw new IllegalArgumentException("Marks should be between 0 and 10."); // Ngoại lệ kiểm tra điểm
        }
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        if (marks < 0 || marks > 10) {
            throw new IllegalArgumentException("Marks should be between 0 and 10."); // Ngoại lệ kiểm tra điểm
        }
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Marks=" + marks +
                '}';
    }
}


public class StudentManagementSystem {
    private static List<Student> students = new ArrayList<>();

    public static void addStudent(String id, String name, double marks) {
        students.add(new Student(id, name, marks));
        System.out.println("Student added successfully!");
    }


    public static void editStudent(String id, double newMarks) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                student.setMarks(newMarks);
                System.out.println("Student marks updated successfully!");
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public static void deleteStudent(String id) {
        students.removeIf(student -> student.getId().equals(id));
        System.out.println("Student removed successfully!");
    }

    public static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    public static void sortStudentsBubbleSort() {
        int n = students.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (students.get(j).getMarks() > students.get(j + 1).getMarks()) {
                    // Swap
                    Student temp = students.get(j);
                    students.set(j, students.get(j + 1));
                    students.set(j + 1, temp);
                }
            }
        }
        System.out.println("Students sorted by marks (Bubble Sort).");
    }

    public static void quickSortStudents(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);

            // Recursively sort elements before and after partition
            quickSortStudents(low, pi - 1);
            quickSortStudents(pi + 1, high);
        }
    }

    private static int partition(int low, int high) {
        double pivot = students.get(high).getMarks();
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            if (students.get(j).getMarks() <= pivot) {
                i++;

                // Swap students[i] and students[j]
                Student temp = students.get(i);
                students.set(i, students.get(j));
                students.set(j, temp);
            }
        }

        // Swap students[i+1] and students[high] (or pivot)
        Student temp = students.get(i + 1);
        students.set(i + 1, students.get(high));
        students.set(high, temp);

        return i + 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student Marks");
            System.out.println("3. Delete Student");
            System.out.println("4. View Students");
            System.out.println("5. Sort Students by Marks (Bubble Sort)");
            System.out.println("6. Sort Students by Marks (Quick Sort)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Student Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Marks: ");
                    double marks = scanner.nextDouble();
                    try {
                        addStudent(id, name, marks);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter Student ID to edit: ");
                    id = scanner.nextLine();
                    System.out.print("Enter new Marks: ");
                    marks = scanner.nextDouble();
                    try {
                        editStudent(id, marks);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter Student ID to delete: ");
                    id = scanner.nextLine();
                    deleteStudent(id);
                    break;
                case 4:
                    viewStudents();
                    break;
                case 5:
                    sortStudentsBubbleSort();
                    viewStudents();
                    break;
                case 6:
                    quickSortStudents(0, students.size() - 1);
                    System.out.println("Students sorted by marks (Quick Sort).");
                    viewStudents();
                    break;
                case 7:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }
}

