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

class RSAEncrypt {
    static private String encoding = "abcdefghijklmnopqrstuvwxyz ";

    static void main(String[] args) {
        // t, h, i
        BigInteger plaintext = BigInteger.valueOf(190708);
        // e =
        // n =
        BigInteger e = BigInteger.valueOf(8311);
        BigInteger n = BigInteger.valueOf(31005883);
        BigInteger c = plaintext.modPow(e, n);
        BigInteger d = BigInteger.valueOf(11296191);
        BigInteger p = c.modPow(d, n);
        System.out.println(getPubKey());
        encryptFile("test.txt");
    }

    static HashMap<String, BigInteger> getPubKey() {
        HashMap<String, BigInteger> en = new HashMap<>();
        try (BufferedReader bf = new BufferedReader(new FileReader("pub_key.txt"))) {
            String s;

            while ((s = bf.readLine()) != null) {
                System.out.println("s: " + s);
                String[] split = s.split(" = ");
                if (en.get("e") == null) {
                    en.put("e", BigInteger.valueOf(Long.valueOf(split[1])));
                } else {
                    en.put("n", BigInteger.valueOf(Long.valueOf(split[1])));
                    if (Long.valueOf(split[1]) < 262626) {
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

    static String encryptFile(String path) {
        String encrypted = "";

        HashMap<String, BigInteger> pubKey = getPubKey();

        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String s;

            while ((s = bf.readLine()) != null) {
                System.out.println("s: " + s);
                String encodingValString = "";
                for (int i = 0, j = 1; i < s.length(); ++i, ++j) {
                    char c = s.charAt(i);
                    int encodingVal = encoding.indexOf(c);
                    encodingValString += String.format("%02d", encodingVal);
                    if (j % 3 == 0) {
                        // do work on encoded integer then reset
                        System.out.println("encodingValString: " + encodingValString);

                        BigInteger encodingAsBigInt = BigInteger.valueOf(Long.parseLong(encodingValString));
                        BigInteger ciphertext = encodingAsBigInt.modPow(pubKey.get("e"), pubKey.get("n"));

                        File f = new File("test.enc");
                        if (f.exists()) {
                            // delete here so as to use append below
                            // prob some better / more efficient way to do this
                            f.delete();
                        }

                        try (BufferedWriter out = new BufferedWriter(new FileWriter("test.enc", true))) { // append
                            out.write(ciphertext.toString());
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            System.exit(-1);
                        }

                        encodingValString = "";
                    } else {
                        // keep building string
                    }
                }

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

        return encrypted;
    }
}
