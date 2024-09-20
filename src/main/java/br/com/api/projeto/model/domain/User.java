package br.com.api.projeto.model.domain;

import java.util.Collection;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_users")
@Data
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private String id;
	
	@Column(name = "email",unique = true)
	@Email
	@NotBlank
	private String email;
	
	@Column(name = "username",unique = true)
	@NotBlank
	private String username;
	
	@Column(name = "password")
	@NotBlank
	private String password;
	
	@Column(name = "document",unique = true)
	@CPF
	@NotBlank
	private String document;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_users_roles",
	joinColumns = @JoinColumn(name="user_id"),
	inverseJoinColumns = @JoinColumn(name="role_id"))
	private Collection<Roles> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
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

	@Override
	public String toString() {
		return "{" +
                "\"id\":\"" + id+ "\"," +
                "\"username\":\"" + username + "\"," +
                "\"document\":\"" + document + "\"," +
                "\"roles\":" + roles.toString() +
                "}";
	}
	
    
    
	

}
