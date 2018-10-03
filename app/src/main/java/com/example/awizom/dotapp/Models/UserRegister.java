package com.example.awizom.dotapp.Models;

import java.util.List;

public class UserRegister {

    public class Login
    {
        public String access_token ;
        public String token_type ;
        public int expires_in ;
        public String userName ;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public class DataIdentityResult
    {
        public boolean Succeeded ;
        public List<String> Errors ;

        public boolean isSucceeded() {
            return Succeeded;
        }

        public void setSucceeded(boolean succeeded) {
            Succeeded = succeeded;
        }

        public List<String> getErrors() {
            return Errors;
        }

        public void setErrors(List<String> errors) {
            Errors = errors;
        }
    }

    public class RootObject
    {
        public String Message ;
        public boolean Status ;
        public String token ;
        public String UserName ;
        public String Role ;
        public Login login ;
        public DataIdentityResult dataIdentityResult ;

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getRole() {
            return Role;
        }

        public void setRole(String role) {
            Role = role;
        }

        public Login getLogin() {
            return login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }

        public DataIdentityResult getDataIdentityResult() {
            return dataIdentityResult;
        }

        public void setDataIdentityResult(DataIdentityResult dataIdentityResult) {
            this.dataIdentityResult = dataIdentityResult;
        }
    }
}
