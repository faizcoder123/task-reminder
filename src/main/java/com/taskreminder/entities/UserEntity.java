package com.taskreminder.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class UserEntity implements Serializable {
        @Id
        @GeneratedValue
        @Column(name = "ownerId", updatable = false)
        private long ownerId;

        @NotBlank
        @Size(min=3, max = 20)
        private String userName;

        @NotBlank
        @Size(max = 100)
        @Email
        @Column(unique=true)
        private String email;

        @NotBlank
        @Size(min=6, max = 100)
        private String password;

        @NotBlank
        @Size(min=10, max = 10)
        private String phoneNo;

        @PostPersist
        @PostUpdate
        @PostRemove
        @PostLoad
        public void operations() { SecurityContextHolder.clearContext(); }
}
