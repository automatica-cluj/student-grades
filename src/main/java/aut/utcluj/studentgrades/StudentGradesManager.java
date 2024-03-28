package aut.utcluj.studentgrades;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StudentGradesManager {
    private static final String SERIALIZED_FILE_PATH = "studentGrades_lab_test1_v1.ser";
    private static final String STUDENT_CODE = "900e0265abcc9c0833823a46b602d2aa";
    // Method to read the CSV and serialize the data
    public void generateGrades(String csvFilePath) throws IOException {
        Map<String, Double> studentGrades = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String studentCode = parts[0].trim();
                    Double grade = Double.parseDouble(parts[1].trim());
                    studentGrades.put(StudentCodeGeneratorUtil.getStudentCode(studentCode), grade);
                }
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SERIALIZED_FILE_PATH))) {
            out.writeObject(studentGrades);
        }
    }

    // Method to deserialize and get grade by student code
    public StudentGrade getGradeByStudentCode(String studentCode) throws IOException, ClassNotFoundException {
        Map<String, Double> studentGrades;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE_PATH))) {
            studentGrades = (Map<String, Double>) in.readObject();
        }

        return new StudentGrade(studentCode, studentGrades.get(studentCode));
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        try {
            StudentGradesManager sgm = new StudentGradesManager();
            //Retrieve and print a grade by student code
            StudentGrade grade = sgm.getGradeByStudentCode(STUDENT_CODE);
            if (grade.grade() != null) {
                System.out.println(grade);
            } else {
                System.out.println("Student code not found.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
