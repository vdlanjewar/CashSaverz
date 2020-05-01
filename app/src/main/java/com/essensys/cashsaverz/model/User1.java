package com.essensys.cashsaverz.model;
import java.util.ArrayList;
import java.util.List;
public class User1 {
    public int userId;
    public String userName;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static List<User1> getUserList(){
        List<User1> userList = new ArrayList<>();
        for(int i=0; i<200000; i++){
            User1 user = new User1();
            user.setUserId(0+i);
            user.setUserName("Name: Ashu"+i);
            userList.add(user);
        }
        return userList;
    }


}
