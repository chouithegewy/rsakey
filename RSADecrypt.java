
package rsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.text.Format;
import java.util.HashMap;

class RSADecrypt {
    static private String encoding = "abcdefghijklmnopqrstuvwxyz ";
    static private String prikeyPath;
    static private String inputPath;
    static private HashMap<String, BigInteger> prikey = new HashMap<>();

    static void main(String[] args) {
        try {
            parseArgs(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        prikey = getPriKey(prikeyPath);
        // t, h, i
        // BigInteger plaintext = BigInteger.valueOf(190708);
        // e =
        // n =
        // BigInteger e = BigInteger.valueOf(8311);
        BigInteger d = prikey.get("d");
        // BigInteger n = BigInteger.valueOf(31005883);
        BigInteger n = prikey.get("n");
        // BigInteger c = plaintext.modPow(d, n);
        // BigInteger d = BigInteger.valueOf(11296191);
        // BigInteger p = c.modPow(d, n);
        decryptFile(inputPath);
    }

    static HashMap<String, BigInteger> getPriKey(String path) {
        HashMap<String, BigInteger> en = new HashMap<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String s;

            while ((s = bf.readLine()) != null) {
                String[] split = s.split(" = ");
                if (en.get("d") == null) {
                    en.put("d", BigInteger.valueOf(Long.valueOf(split[1])));
                } else {
                    en.put("n", BigInteger.valueOf(Long.valueOf(split[1])));
                    if (Long.valueOf(split[1]) <= 262626) {
                        System.err.println("n must be larger than 262626. n: " + Long.valueOf(split[1]));
                        System.exit(-1);
                    }
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
        return en;
    }

    static String decryptFile(String path) {
        String decrypted = "";

        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String s;
            try (BufferedWriter out = new BufferedWriter(new FileWriter("test.dec"))) {
                while ((s = bf.readLine()) != null) {
                    s = s.replace(" ", ""); // join string, keep previous logic
                    String encodingValString = "";
                    for (int i = 0, j = 6; i < s.length(); i = i + 6, j = j + 6) {
                        BigInteger cipherBlock = BigInteger.valueOf(Long.valueOf(s.substring(i, j)));
                        // retrieve plaintext then decode
                        String plaintext = cipherBlock.modPow(prikey.get("d"), prikey.get("n")).toString();
                        String encodedPlaintext = "";
                        for (int k = 0; k < plaintext.length() - 1; k = k + 2) {
                            int index = Integer.parseInt(plaintext.substring(k, k + 2));
                            char encodedVal = encoding.charAt(index);
                            encodedPlaintext += "" + encodedVal;
                        }
                        out.write(encodedPlaintext);

                        encodedPlaintext = "";
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

        return decrypted;
    }

    static void parseArgs(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Incorrect number of arguments. Expected 2. <input> <prikey>");
        } else {
            inputPath = args[0];
            prikeyPath = args[1];
        }
    }
}
