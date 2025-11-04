package com.example.loyalty.tools;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Simple helper to encrypt a plaintext using Jasypt algorithm.
 * Usage: mvn exec:java
 * -Dexec.mainClass=com.example.loyalty.tools.JasyptEncryptor
 * -Dexec.args="plaintext masterPassword"
 */
public class JasyptEncryptor {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: <plaintext> <masterPassword>");
            System.exit(2);
        }
        String plaintext = args[0];
        String master = args[1];

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(master);
        encryptor.setAlgorithm("PBEWithMD5AndDES");

        String encrypted = encryptor.encrypt(plaintext);
        System.out.println(encrypted);
    }
}
