package com.blo.security6.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserCreationRequest {
    @NotBlank(message = "NOTBANK")
    @Email(message = "EMAIL")
     String email;
    @Size(min = 8, message = "PASSWORD_INVALID")
    @NotBlank(message = "NOTBANK")
     String password;
     String firtname;
     String lastname;
     LocalDate dob;
}
