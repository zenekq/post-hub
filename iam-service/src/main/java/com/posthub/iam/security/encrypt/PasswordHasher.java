package com.posthub.iam.security.encrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {

    static void main() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Note: BCrypt generates a different hash every time due to random salt.
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

        /*
        password pas1: $2a$10$esq3XddqYdSzyvfKmIXn1OXspdvzk98kDAUzmbE.1jjxY26D72quq
        password pas2: $2a$10$g6H7do4Txmgraarf9HwxHe4brj72TlPFfps78w/ThixIaOvPv1ZPK
        password pas3: $2a$10$HT/VouLOxW0EDuLLgPASsuSN9MeDTkP1V5zpCW0pN9rNkV/R5Vuma
        password pas4: $2a$10$a3iM0krwVEiECG3EbzxNBOTJBNVaHPVEjLPEOOp4ysvJSje54j/Be
        password pas5: $2a$10$eiiNTTyQTH8Aa8/UbCayZuPybkToefdtUswsYn6OumCfXyhSGyDuW
        */
    }

}
