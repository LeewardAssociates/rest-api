package com.rectraxx.rest.api.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rectraxx.api.common.util.AppUtil;
import com.rectraxx.rest.api.models.TokenModel;
import com.rectraxx.rest.api.models.UserAuthModel;
import com.rectraxx.rest.api.models.UserModel;
import com.rectraxx.rest.api.services.UserService;
import com.rectraxx.rest.api.util.TokenUtils;

@RestController
@RequestMapping(path="/user")
public class UserResource {

	@Autowired
	UserService userService;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@RequestMapping(method=RequestMethod.GET)
	public UserModel getUser(HttpServletResponse response)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			try {
				response.sendError(
						HttpServletResponse.SC_UNAUTHORIZED,
						"Unauthorized: Authentication token was either missing or invalid.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		UserDetails userDetails = (UserDetails) principal;

		return new UserModel(userDetails.getUsername(), this.createRoleMap(userDetails));
	}
	
	@RequestMapping(path="/authenticate", method=RequestMethod.POST)
	public TokenModel authenticate(@RequestBody UserAuthModel user, HttpServletRequest request)
	{
		System.out.println("request:" + request);
		System.out.println("username: " + user.getUsername());
		System.out.println("password: " + user.getPassword());
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(user.getUsername(), AppUtil.encodeSHA(user.getPassword()));
		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		/*
		 * Reload user as password of authentication principal will be null after authorization and
		 * password is needed for token generation
		 */
		UserDetails userDetails = this.userService.loadUserByUsername(user.getUsername());

		return new TokenModel(TokenUtils.createToken(userDetails));
	}

	private Map<String, Boolean> createRoleMap(UserDetails userDetails)
	{
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}
		return roles;
	}
}
