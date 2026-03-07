package model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private Role role; // admin, user
    private String password_hashed;
    private String phone_number;
    private String avt_url;
    private String salt;
    private int verified;

    public User() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getPassword_hashed() { return password_hashed; }
    public void setPassword_hashed(String password_hashed) { this.password_hashed = password_hashed; }
    public String getPhone_number() { return phone_number; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }
    public String getAvt_url() { return avt_url; }
    public void setAvt_url(String avt_url) { this.avt_url = avt_url; }
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
    public int getVerified() { return verified; }
    public void setVerified(int verified) { this.verified = verified; }

    public boolean checkVerified() {
        return this.verified == 1;
    }
}