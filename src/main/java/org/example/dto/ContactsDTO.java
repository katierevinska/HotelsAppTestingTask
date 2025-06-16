package org.example.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContactsDTO {
    private String phone;
    private String email;
}
