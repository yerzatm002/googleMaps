package kz.meirambekuly.googlemaps.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class SecurityUtils {
    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(""));
        }
        return false;
    }

//    public static String getRequestRemoteAddr() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//                    .getRequest();
//            StringBuilder sb = new StringBuilder();
//            String remoteAddr = request.getHeader("X-FORWARDED-FOR");
//            if (StringUtils.isNotEmpty(remoteAddr)) {
//                sb.append(remoteAddr);
//            }
//            if (sb.length() > 0) {
//                sb.append("|");
//            }
//            sb.append(request.getRemoteAddr());
//            return sb.toString();
//        } catch (Exception e) {
//            return "unknown";
//        }
//    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
        }
        return false;
    }

    public static boolean hasAnyRole(String... roles) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            Set<String> roleSet = new HashSet<>(Arrays.asList(roles));
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> roleSet.contains(grantedAuthority.getAuthority()));
        }
        return false;
    }

    public static Set<String> getRoles() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Get authorities of authenticatedUser
     *
     * @return Collection of GrantedAuthorities
     */
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities();
        }
        return Collections.emptySet();
    }

//    public static String getCurrentUserLoginOrThrow() {
//        String login = getCurrentUserLogin();
//        if (StringUtils.isEmpty(login)) {
//            throw new FLCException(Errors.NO_USER);
//        }
//        return login;
//    }
}
