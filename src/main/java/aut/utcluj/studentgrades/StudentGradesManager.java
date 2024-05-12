package aut.utcluj.studentgrades;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class StudentGradesManager {
    private static final String SERIALIZED_FILE_PATH = "studentGrades_lab_test2_v1.ser";
    public final static String STUDENT_EMAIL = "demo.student@test.com";

    /**
     * Method to read the CSV and serialize the data
     * @param csvFilePath
     * @throws IOException
     */
    public void saveGrades(String csvFilePath) throws IOException {
        Map<String, Double> studentGrades = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String studentCode = parts[0].trim();
                    Double grade = Double.parseDouble(parts[1].trim());
                    studentGrades.put(getStudentCode(studentCode), grade);
                }
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SERIALIZED_FILE_PATH))) {
            out.writeObject(studentGrades);
        }
    }

    /**
     * Method to deserialize and get grade by student code
     * @param studentCode
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public StudentGrade getGradeByStudentCode(String studentCode) throws IOException, ClassNotFoundException {
        System.out.println("Getting grade for student: " + studentCode);
        Map<String, Double> studentGrades;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE_PATH))) {
            studentGrades = (Map<String, Double>) in.readObject();
        }

        return new StudentGrade(studentCode, studentGrades.get(studentCode));
    }

    /**
     * Method to generate student code
     * @param input
     * @return
     */
    public String getStudentCode(String input) {
        try {
            input = input.trim().toLowerCase();
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        try {
            StudentGradesManager sgm = new StudentGradesManager();
            //sgm.saveGrades("demo_note2.txt");
            //Retrieve and print a grade by student code
            StudentGrade grade = sgm.getGradeByStudentCode(sgm.getStudentCode(STUDENT_EMAIL));
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
