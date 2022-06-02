package storybird.security;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ToonflixUsers implements UserDetails{
	
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	
	private static final Log logger = LogFactory.getLog(ToonflixUsers.class);
	
	private int user_no;
	private String nickname;
	private String id;
	private String password;
	private String userType;
	private String sub_email;
	private String profile_path;
	private int user_level;
	private Set<GrantedAuthority> authority;
	private HashMap<String, Object> other_info;
	
	@Builder.Default
	private List<String> roles = new ArrayList<>();
	
	public ToonflixUsers(int user_no, String nickname, String id, String password, String sub_email, int user_level,
						 String profile_path, Collection<? extends GrantedAuthority> authority, HashMap<String, Object> otherInfo) {
		this.user_no = user_no;
		this.nickname = nickname;
		this.password = password;
		this.id = id;
		this.sub_email = sub_email;
		this.user_level = user_level;
		this.profile_path = profile_path;
		this.other_info = otherInfo;
		
		this.authority = Collections.unmodifiableSet(sortAuthorities(authority));
	}
	public static ToonflixUsers getAuthorizedUsers(HttpServletRequest request) throws AuthException {
		return getAuthorizedUsers();
	}
	public static ToonflixUsers getAuthorizedUsers() throws AuthException {
		//SecurityContextHolder.getContext().getAuthentication().getPrincipal() Filter에서 토큰을 통해 저장했던 유저정보
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof ToonflixUsers) {
			checkAuthorizedByRole((ToonflixUsers) user, "BASIC");
			return (ToonflixUsers) user;
		}

		throw new UsernameNotFoundException("Not AuthorizedUser");
	}


	public static ToonflixUsers getAuthorizedAdmin(HttpServletRequest request) throws AuthException {
		return getAuthorizedAdmin();
	}
	public static ToonflixUsers getAuthorizedAdmin() throws AuthException {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof ToonflixUsers){
			checkAuthorizedByRole((ToonflixUsers) user, "ADMIN");
			return (ToonflixUsers) user;
		}
		throw new UsernameNotFoundException("Not AuthorizedUser");
	}
	public static ToonflixUsers getAuthorizedCompanyOrAuthor() throws AuthException {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user instanceof ToonflixUsers){
			checkAuthorizedByRole((ToonflixUsers) user, "PARTNER","AUTHOR","EMPLOYEE");
			return (ToonflixUsers) user;
		}
		throw new UsernameNotFoundException("Not AuthorizedUser");
	}

	private static void checkAuthorizedByRole(ToonflixUsers user, String... role){
		for(String str : role){
			if(user.getRoles().get(0).contains(str)) return;
		}
		throw new UsernameNotFoundException("Not AuthorizedUser");
	}

	public void setUser_no(int user_no) {
		this.user_no = user_no;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.sub_email = email;
	}

	public void setUserType(String userType) { this.userType = userType; }

	public void setProfile_path(String profile_path) {
		this.profile_path = profile_path;
	}

	public void setAuthority(Set<GrantedAuthority> authority) {
		this.authority = authority;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authority = Collections.unmodifiableSet(sortAuthorities(authorities));
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return getId();
	}
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isEnabled() {
		return true;
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities){
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		
		SortedSet<GrantedAuthority> sortedAuthorities = 
				new TreeSet<GrantedAuthority>(new AuthorityComparator());
		
		for(GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GratedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}
		
		return sortedAuthorities;
	}
	
	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable{
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
		
		@Override
		public int compare(GrantedAuthority o1, GrantedAuthority o2) {
			if (o2.getAuthority() == null) return -1;
			if(o1.getAuthority() == null) return 1;
			
			return o1.getAuthority().compareTo(o2.getAuthority());
		}
	}
}
