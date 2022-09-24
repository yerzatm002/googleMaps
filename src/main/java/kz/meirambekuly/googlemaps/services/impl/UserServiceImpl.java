package kz.meirambekuly.googlemaps.services.impl;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import kz.meirambekuly.googlemaps.config.jwt.Jwt;
import kz.meirambekuly.googlemaps.constants.Provider;
import kz.meirambekuly.googlemaps.models.User;
import kz.meirambekuly.googlemaps.repositories.RoleRepository;
import kz.meirambekuly.googlemaps.repositories.UserRepository;
import kz.meirambekuly.googlemaps.services.UserService;
import kz.meirambekuly.googlemaps.utils.ObjectMapper;
import kz.meirambekuly.googlemaps.utils.PasswordEncoder;
import kz.meirambekuly.googlemaps.utils.SecurityUtils;
import kz.meirambekuly.googlemaps.web.dto.ResponseDto;
import kz.meirambekuly.googlemaps.web.dto.UserCreatorDto;
import kz.meirambekuly.googlemaps.web.dto.UserDto;
import kz.meirambekuly.googlemaps.web.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String CLIENT_SECRET;

    @Override
    public ResponseDto<?> findAllUsers() {
        List<UserDto> userDtoList = userRepository.findAll().stream().map(ObjectMapper::convertToUserDto).collect(Collectors.toList());
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(userDtoList)
                .build();
    }

    @Override
    public ResponseDto<?> findByEmail(String email) {
        Optional<User> user = userRepository.findUsersByEmail(email);
        if(user.isPresent()){
            UserDto dto = ObjectMapper.convertToUserDto(user.get());
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data(dto)
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.NO_CONTENT.value())
                .errorMessage("User with such email does not exist!")
                .build();
    }

    @Override
    public ResponseDto<?> signInWithGoogle(String authCode) throws IOException {

//        if (request.getHeader("X-Requested-With") == null) {
//            // Without the `X-Requested-With` header, this request could be forged. Aborts.
//        }

// Set path to the Web application client_secret_*.json file you downloaded from the
// Google API Console: https://console.developers.google.com/apis/credentials
// You can also find your Web application client ID and client secret from the
// console and specify them directly when you create the GoogleAuthorizationCodeTokenRequest
// object.
//        String CLIENT_SECRET_FILE = "/path/to/client_secret.json";

// Exchange auth code for access token
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(
//                        JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE));
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
//                        clientSecrets.getDetails().getClientId(),
//                        clientSecrets.getDetails().getClientSecret(),
                        CLIENT_ID,
                        CLIENT_SECRET,
                        authCode,
                        "http://localhost:8080")  // Specify the same redirect URI that you use with your web
                        // app. If you don't have a web version of your app, you can
                        // specify an empty string.
                        .execute();

        String accessToken = tokenResponse.getAccessToken();

// Use access token to call API
//        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//        Drive drive =
//                new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
//                        .setApplicationName("Auth Code Exchange Demo")
//                        .build();
//        File file = drive.files().get("appfolder").execute();

// Get profile info from ID token
        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getUserId();  // Use this value as a key to identify a user.
        String email = payload.getEmail();
        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");

        Optional<User> existsUser = userRepository.findUsersByEmail(email);
        if(existsUser.isPresent()){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + existsUser.get().getRole().getName()));
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data("Bearer " + Jwt.generateJwt(existsUser.get().getEmail(),authorities))
                    .build();
        }
        User user = User.builder()
                .email(email)
                .provider(Provider.GOOGLE)
                .enabled(true)
                .role(roleRepository.findById(1L).get())
                .build();
        user = userRepository.save(user);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data("Bearer " + Jwt.generateJwt(user.getEmail(),authorities))
                .build();
    }

    @Override
    public ResponseDto<?> register(UserCreatorDto dto) {
        Optional<User> user = userRepository.findUsersByEmail(dto.getEmail());
        if(user.isPresent()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorMessage("User already exists with such email!!")
                    .build();
        }
        User newUser = User.builder()
                .email(dto.getEmail())
                .password(PasswordEncoder.hashcode(dto.getPassword()))
                .enabled(true)
                .provider(Provider.LOCAL)
                .role(roleRepository.findById(1L).get())
                .build();
        userRepository.save(newUser);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + newUser.getRole().getName()));
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data("Bearer " + Jwt.generateJwt(newUser.getEmail(),authorities))
                .build();
    }

    @Override
    public ResponseDto<?> login(UserLoginDto dto) {
        Optional<User> user = userRepository.getUserByEmailAndPassword(dto.getEmail(), PasswordEncoder.hashcode(dto.getPassword()));
        if(user.isPresent()){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.get().getRole().getName()));
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data("Bearer " + Jwt.generateJwt(dto.getEmail(),authorities))
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .errorMessage("Email or password INCORRECT!")
                .build();
    }

    @Override
    public ResponseDto<?> getToken() {
        if (SecurityUtils.isAuthenticated()) {
            String token = Jwt.generateJwt(SecurityUtils.getCurrentUserLogin(), SecurityUtils.getAuthorities());
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data(token)
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("UNAUTHORIZED")
                .build();
    }

    @Override
    public ResponseDto<?> getLoggedUserInformation() {
        Optional<User> user = userRepository.findUsersByEmail(SecurityUtils.getCurrentUserLogin());
        if (user.isPresent()) {
            UserDto dto = ObjectMapper.convertToUserDto(user.get());
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data(dto)
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("UNAUTHORIZED!")
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> updateUser(UserCreatorDto dto) {
        Optional<User> user = userRepository.findUsersByEmail(SecurityUtils.getCurrentUserLogin());
        if (user.isPresent()) {
            if (Objects.nonNull(dto.getEmail())) {
                user.get().setEmail(dto.getEmail());
            }
            UserDto newUserDto = ObjectMapper.convertToUserDto(userRepository.save(user.get()));
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data(newUserDto.getId())
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("UNAUTHORIZED!")
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> changePassword(String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findUsersByEmail(SecurityUtils.getCurrentUserLogin());
        if(user.isPresent()){
            if(Objects.nonNull(oldPassword) && Objects.nonNull(newPassword)){
                if(!PasswordEncoder.hashcode(newPassword).equals(user.get().getPassword())){
                    user.get().setPassword(PasswordEncoder.hashcode(newPassword));
                    userRepository.save(user.get());
                    return ResponseDto.builder()
                            .isSuccess(true)
                            .httpStatus(HttpStatus.OK.value())
                            .data(user.get().getId())
                            .build();
                }
                return ResponseDto.builder()
                        .isSuccess(false)
                        .httpStatus(HttpStatus.NO_CONTENT.value())
                        .errorMessage("NOT_VALID_FIELDS")
                        .build();
            }
            return ResponseDto.builder()
                    .isSuccess(false)
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorMessage("INCORRECT_PASSWORD")
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("UNAUTHORIZED!")
                .build();
    }
}
