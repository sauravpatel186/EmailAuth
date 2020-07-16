package com.example.emailauth;

public class UsersFire {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UsersFire{" +
                "id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }

    public String id;
    public String fname;

    public String address;

    public String mobile;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }



    public String mail;

    public UsersFire()
    {

    }
    public UsersFire(String fname,String address,String mobile,String mail) {
        this.fname = fname;
        this.address = address;
        this.mobile = mobile;
        this.mail = mail;
    }
}