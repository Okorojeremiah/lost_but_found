package com.winners.lostbutfound.utils;


import static com.winners.lostbutfound.utils.UserValidator.ValidationResult.*;
import java.util.function.Function;


public interface UserValidator
        extends Function<String, UserValidator.ValidationResult> {

//    static UserValidator isValidName(){
//        return name -> name
//    }
    static UserValidator isValidEmail1(){
        return email -> email != null && email
                .matches("^[A-Za-z0-9+_.-]+@(gmail|outlook|hotmail|livingfaith)\\" +
                        ".(com|net|org|edu|gov|mil|co\\.uk)$") ?
                SUCCESS : EMAIL_NOT_VALID;
    }

    static UserValidator isValidEmail(){
        return email -> email != null && email
                .matches("^[A-Za-z0-9+_.-]+@(livingfaith)\\" +
                        ".(org)$") ?
                SUCCESS : EMAIL_NOT_VALID;
    }

    static UserValidator isValidPhoneNumber() {
        return phoneNumber -> phoneNumber != null && phoneNumber
                .matches("^((081|090|091|080|070|071)[0-9]{8})$") ?
                SUCCESS : PHONE_NUMBER_NOT_VALID;
    }

    enum ValidationResult{
        SUCCESS,
        EMAIL_NOT_VALID,
        PHONE_NUMBER_NOT_VALID
    }
}
