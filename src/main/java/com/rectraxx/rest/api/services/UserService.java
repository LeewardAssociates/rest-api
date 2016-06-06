package com.rectraxx.rest.api.services;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rectraxx.api.incidentaccident.dao.UserRolesDAO;
import com.rectraxx.api.incidentaccident.dao.UsersDAO;
import com.rectraxx.api.incidentaccident.models.UsersModel;
import com.rectraxx.rest.api.models.UserModel;
import com.rectraxx.rest.api.resources.Role;

@Service("userService")
public class UserService implements UserDetailsService {

	@Autowired
	UsersDAO usersDao;
	
	@Autowired
	UserRolesDAO userRolesDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UsersModel user = usersDao.getUserCredsByName(username);
    	UserModel userModel = null;
    	if (user != null) {
    		userModel = new UserModel(user.getUsername(), user.getPassword());
    		TreeSet<String> roles = userRolesDao.getUserRoles(user.getUsername());
    		for (String role : roles) {
    			Role r = getRole(role);
    			if (r != null) {
    				userModel.addRole(r);
				}
			}
		}
    	return userModel;
	}
	
	private Role getRole(String role) {
		for (Role r : Role.values()) {
			if (r.getAuthority().equals(role)) {
				return r;
			}
		}
		return null;
	}
}
