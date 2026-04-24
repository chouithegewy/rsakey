package rsa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import rsa.RSAKey;

class RSAEncrypt {
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
    }

    // static void Encrypt() {

    // }

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
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
        return en;
    }

}
