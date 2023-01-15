package com.cybersecurity.incidentresponsereadinessexcercises.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cybersecurity.incidentresponsereadinessexcercises.utils.RolesHelper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/**" })
public class HttpBasicFilter implements jakarta.servlet.Filter {

	private final MainConfiguration cfg;

	public HttpBasicFilter(MainConfiguration cfg) {
		this.cfg = cfg;
	}

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (token != null && token.toLowerCase().startsWith("basic ")) {
			String userPass = new String(Base64.getDecoder().decode(token.substring(6)));
			int sepPos = userPass.indexOf(':');
			if (sepPos==-1) {
				return currentAuthentication();
			}
			String user = userPass.substring(0, sepPos);
			String tkn = DigestUtils.sha256Hex(userPass);
			if (Arrays.asList(cfg.getAccessTokens().split("\\,")).contains(tkn)) {
				final List<GrantedAuthority> roles = new LinkedList<>();
				roles.add(new SimpleGrantedAuthority("ROLE_USER"));
				if (!"user".equals(user)) {
					roles.add(new SimpleGrantedAuthority("ROLE_"+user.toUpperCase()));
				}
				RolesHelper.rewriteRoles(roles);
				return new UsernamePasswordAuthenticationToken(user, "", roles);
			}
		}
		return currentAuthentication();
	}

	private Authentication currentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Authentication auth = attemptAuthentication((HttpServletRequest)request, (HttpServletResponse)response);
		if (auth!=null) {
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);
	}
}