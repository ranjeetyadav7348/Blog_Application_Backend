package com.blogapplication.application.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    String resouceName;
    String fieldName;
    long fieldValue;
    public ResourceNotFoundException(String resouceName, String fieldName, long fieldValue) {
       
        super(String.format("%s not found with %s : %d",resouceName,fieldName,fieldValue));
        this.resouceName = resouceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    
    
}
