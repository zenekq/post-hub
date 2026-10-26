package com.posthub.iam.security.encrypt;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class PasswordHasher {

    static void main() {

        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        // Always use encoder.matches(rawPassword, hashedPassword) for verification.

        String password1 = encoder.encode("pas1");
        String password2 = encoder.encode("pas2");
        String password3 = encoder.encode("pas3");
        String password4 = encoder.encode("pas4");
        String password5 = encoder.encode("pas5");

        IO.println("password pas1: " + password1);
        IO.println("password pas2: " + password2);
        IO.println("password pas3: " + password3);
        IO.println("password pas4: " + password4);
        IO.println("password pas5: " + password5);

    }

}
