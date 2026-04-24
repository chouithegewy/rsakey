package rsa;

import java.math.BigInteger;

import rsa.RSAKey;

class RSAEncrypt {
    static void main(String[] args) {
        RSAKey key = new RSAKey();
        key.run(null, null, BigInteger.valueOf(12));
    }
}
