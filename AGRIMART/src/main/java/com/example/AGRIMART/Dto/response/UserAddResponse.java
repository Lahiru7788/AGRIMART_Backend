package com.example.AGRIMART.Dto.response;
import com.example.AGRIMART.Entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddResponse extends Response{
    private User user;

}
