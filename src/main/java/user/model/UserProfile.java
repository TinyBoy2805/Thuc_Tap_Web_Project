package user.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserProfile
{
    private String name, email, phone_number, avt_url;
    private List<Address> myAddresses;

    public UserProfile(String name, String email, String phone_number, String avt_url)
    {
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.avt_url = avt_url;
    }

    public UserProfile()
    {

    }
}
