package Reqres.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Register {
    public String email;
    public String password;

    public Register(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
