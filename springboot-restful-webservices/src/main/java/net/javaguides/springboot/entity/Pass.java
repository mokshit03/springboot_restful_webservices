package net.javaguides.springboot.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pass {

    private String oldPassword;
    private String newPassword;
}
