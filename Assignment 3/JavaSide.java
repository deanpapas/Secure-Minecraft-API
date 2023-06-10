import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class JavaSide {
    public static void main(String[] args) {

        String InputString = "K4qBYWq2a+3aptmggyCe6oVDHDC0iENTnLEpWe9TEKrMH3RViiHH4HpgT3OHroxs,e3dc52c1454be84084b8963a1442294c3cb3b235b61e2a089925dfe9d0c69478";

        // Keys
        String ekey = "B&E)H@McQeThWmZq";
        String mkey = "UTYn4JIOaz";

        // Split line into message and MAC
        String[] parts = InputString.split(",");
        String cipherString = parts[0];
        String macString = parts[1];

        // DEBUG PRINTS
        System.out.println("==============");
        System.out.println("Received: " + InputString);
        System.out.println("Cipher: " + cipherString);
        System.out.println("MAC: " + macString);
        System.out.println("==============");


        try {
            // ChiperString -> CipherBytes -> EncryptedBytes
            byte[] cipherbytes = Base64.getDecoder().decode(cipherString);
            byte[] iVec = Arrays.copyOfRange(cipherbytes, 0, 16);
            byte[] encryptedbytes = Arrays.copyOfRange(cipherbytes, 16, cipherbytes.length);

            // Making IV and Key objects
            IvParameterSpec iv = new IvParameterSpec(iVec);
            SecretKeySpec eKeyObj = new SecretKeySpec(ekey.getBytes("UTF-8"), "AES");

            // Decrypting EncryptedBytes -> DecryptedBytes -> Plaintext
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, eKeyObj, iv);
            byte[] decryptedbytes = cipher.doFinal(encryptedbytes);
            String plaintext = new String(decryptedbytes, StandardCharsets.UTF_8);

            // Making MAC and Key objects
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec mKeyObj = new SecretKeySpec(mkey.getBytes(), "HmacSHA256");
            mac.init(mKeyObj);

            // Calculating MAC from cipherString
            byte[] macBytes = mac.doFinal(cipherString.getBytes());
            String calculatedMac = bytesToHex(macBytes);

            // DEBUG PRINTS
            System.out.println("Received Cipher: " + cipherString);
            System.out.println("Decrypted Cipher: " + plaintext);
            System.out.println("==============");
            System.out.println("Received MAC:   " + macString);
            System.out.println("Calculated MAC: " + calculatedMac);


            if (calculatedMac.equals(macString)) {
                System.out.println("MAC verification successful!");
                System.out.println("==============");

                // System.out.println(line);
                String methodName = plaintext.substring(0,
                plaintext.indexOf("("));
                System.out.println("Method: " + methodName);
                String[] argus = plaintext
                        .substring(plaintext.indexOf("(") + 1, plaintext.length() - 1)
                        .split(",");
                System.out.println("Args: " + Arrays.toString(argus));
                System.out.println("==============");
                // handleCommand(methodName, args);

            } else {
                System.out.println("MAC verification failed!");
            }

        } catch (NoSuchAlgorithmException | java.security.InvalidKeyException | UnsupportedEncodingException
                | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }

    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}