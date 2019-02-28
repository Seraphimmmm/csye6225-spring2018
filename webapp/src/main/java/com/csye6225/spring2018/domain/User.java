package com.csye6225.spring2018.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int Id;

    @NotNull
    private String emailAddress;

    @NotNull
    @Size(min=6)
    private String password;

    @Size(max=140)
    private String aboutMe;

    public User(){}

    public User(String emailAddress,String password) {
        this.emailAddress =emailAddress;
        this.password = password;
    }

    public int getId() {
        return Id;
    }

    public String getAboutMe() { return aboutMe;}

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAboutMe(String aboutMe) {this.aboutMe = aboutMe;}



}
