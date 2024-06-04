package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	
	private List<GrantedAuthority> authorities;
	
	public UserDetailsImpl(Usuario user) {
		
		this.userName = user.getUsuario();
		this.password = user.getSenha();
	}
	
	public UserDetailsImpl() {}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// autorizações de acesso ao usuário
		return authorities;
	}

	@Override
	public String getPassword() {
		// retorna a senha do usuário
		return password;
	}

	@Override
	public String getUsername() {
		// retorna o usuário que está tentando logar
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// se a conta não expirou ele acesso - true
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// se a conta não esta bloqueada - true
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// se a credencial não estiver expirada - true
		return true;
	}

	@Override
	public boolean isEnabled() {
		// se o usuário está habilitado - true
		return true;
	}

}

