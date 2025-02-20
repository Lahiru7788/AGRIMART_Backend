package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Entity.UserCategories;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCategeriesAddResponse extends Response{
    private UserCategories userCategories;
}
