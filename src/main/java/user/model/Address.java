package user.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Address implements Serializable
{
    private long id;
    private long userId;
    private String houseNumber;
    private String road;
    private String district;
    private String city;
    private String hamlet;
    private String ward;
    private boolean isDefault;

    public Address() {}
    
    public Address(long id, long userId, String houseNumber, String road, String district, String city, String hamlet, String ward, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.houseNumber = houseNumber;
        this.road = road;
        this.district = district;
        this.city = city;
        this.hamlet = hamlet;
        this.ward = ward;
        this.isDefault = isDefault;
    }

    public boolean getIsDefault()
    {
        return this.isDefault;
    }



}
