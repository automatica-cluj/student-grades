package aut.utcluj.studentgrades;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StudentCodeGeneratorUtil {
    public final static String STUDENT_EMAIL = "demo.student@test.com";
    public static String getStudentCode(String input) {
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

    public static void main(String[] args) {
        //find your student code by executing following command and replace demo.student@test.com with your email address in STUDENT_EMAIL variable.
        System.out.println(getStudentCode(STUDENT_EMAIL));
    }

}
