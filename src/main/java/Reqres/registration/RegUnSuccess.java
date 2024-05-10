package Reqres.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegUnSuccess {


    public String error;

    public RegUnSuccess() {
    }

    public RegUnSuccess(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
