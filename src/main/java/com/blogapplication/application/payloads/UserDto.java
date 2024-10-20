package com.blogapplication.application.payloads;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private int id;

    @NotEmpty
    @Size(min=4,message = "Username must be min is 4 characters!!")
    private String name;
    @Email(message = "Email is not valid")
    private String email;
    @NotEmpty
    @Size(min = 3,max = 10,message = "Password must be in between 4 to 10 character")
    private String password;
    @NotEmpty
    private String about;

    private Set< RoleDto > roles = new HashSet<>();

    public UserDto(@NotEmpty @Size(min = 4, message = "Username must be min is 4 characters!!") String name,
            @Email(message = "Email is not valid") String email,
            @NotEmpty @Size(min = 3, max = 10, message = "Password must be in between 4 to 10 character") String password,
            @NotEmpty String about) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.about = about;
    }
    
}
