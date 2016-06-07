package com.rectraxx.rest.api.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rectraxx.rest.api.resources.Role;

public class UserModel implements Serializable, UserDetails {

	private String name = "";
	private String password="";

	private Set<Role> roles = new HashSet<Role>();
	private Map<String, Boolean> setRoles = new HashMap<String, Boolean>();


	public UserModel(String userName, String password)
	{
		this.name = userName;
		this.password = password;
	}

	public UserModel(String userName, Map<String, Boolean> roles)
	{
		this.name = userName;
		this.setRoles = roles;
	}

	public Set<Role> getRoles()
	{
		return this.roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	public void addRole(Role role)
	{
		this.roles.add(role);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.getRoles();
	}


	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.name;
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
	
	
}
