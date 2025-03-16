package assignment.assignment.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    private String username;  
    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String fullname;
    private String password;
    private String email;
    private String photo;
    private Boolean activated;
    private Boolean admin;

     // Mã kích hoạt tài khoản (dùng khi đăng ký)
     @Column(name = "activation_code")
     private String activationCode;
 
     // Token đặt lại mật khẩu (dùng khi quên mật khẩu)
     @Column(name = "reset_token")
     private String resetToken;

    @OneToMany(mappedBy = "account")
    List<Order> orders;
}
