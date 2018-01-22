package my.com.maxmoney.scanpayment.common;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by iqbalbaharum on 16/01/2018.
 */

public class CodeGenerator {

    public static String generateHex() {

        SecureRandom random = new SecureRandom();
        String randomCode = new BigInteger(30, random).toString(32).toUpperCase();

        return randomCode;
    }

    public static String generateNumber() {

        Random random = new Random();
        int number = random.nextInt(999 - 101) + 101;

        return String.valueOf(number);
    }
}
