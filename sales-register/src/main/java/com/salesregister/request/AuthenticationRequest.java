package com.salesregister.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    public String email;
    public String password;
}
