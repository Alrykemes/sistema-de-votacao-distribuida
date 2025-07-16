package com.votingsystem.rmi.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class PasswordEncoder {

    private static final Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);

    public String hashPassword(String plainPassword) {
        try {
            return argon2.hash(4, 65536, 1, plainPassword.toCharArray());
        } finally {
            argon2.wipeArray(plainPassword.toCharArray());
        }
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return argon2.verify(hashedPassword, plainPassword.toCharArray());
        } finally {
            argon2.wipeArray(plainPassword.toCharArray());
        }
    }
}
