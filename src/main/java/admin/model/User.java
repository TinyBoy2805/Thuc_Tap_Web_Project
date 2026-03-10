package admin.model;

import admin.Enums.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
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

    public boolean checkVerified() {
        return this.verified == 1;
    }
}