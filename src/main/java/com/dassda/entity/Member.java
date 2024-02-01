package com.dassda.entity;

import com.dassda.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "members")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String gender;

    private String age_range;

    private String profile_image_url;

    private String birthday;

    private OAuthProvider oAuthProvider;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public Member(String email, String nickname, String gender,
                  String age_range, String profile_image_url, String birthday, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.age_range = age_range;
        this.profile_image_url = profile_image_url;
        this.birthday = birthday;
        this.oAuthProvider = oAuthProvider;
    }

}
