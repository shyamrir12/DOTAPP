package com.example.awizom.dotapp.Models;

public class UserLogin {
    public class Login {

        public String access_token;
        public String token_type;
        public int expires_in;
        public String userName;


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

    public class RootObject {
        public String Message;
        public boolean Status;
        public String Role;
        public boolean Active;
        public String UserID;

        public boolean isActive() {
            return Active;
        }

        public void setActive(boolean active) {
            Active = active;
        }

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

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            this.UserID = userID;
        }
        public Login login;
    }

}