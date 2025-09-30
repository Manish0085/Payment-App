package com.paypal.user.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msg){
        super(msg);
    }




//    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue){
//        super(String.format("%s not found with the given input data %s: '%s'", resourceName, fieldName, fieldValue));
//    }
}
