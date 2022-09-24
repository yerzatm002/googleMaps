package kz.meirambekuly.googlemaps.services.impl;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
    public ResponseDto<?> findByUsername(String username) {
        Optional<User> user = userRepository.findUsersByUsername(username);
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
                .errorMessage("User with such name does not exist!")
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
                .username(dto.getUsername())
                .password(PasswordEncoder.hashcode(dto.getPassword()))
                .enabled(true)
                .provider(Provider.LOCAL)
                .role(roleRepository.findById(1L).get())
                .build();
        newUser = userRepository.save(newUser);
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(newUser)
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
        return null;
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
    public ResponseDto<?> updateUser(UserDto dto) {
        Optional<User> user = userRepository.findUsersByEmail(SecurityUtils.getCurrentUserLogin());
        if (user.isPresent()) {
            if (Objects.nonNull(dto.getUsername())) {
                user.get().setUsername(dto.getUsername());
            }
            UserDto newUserDto = ObjectMapper.convertToUserDto(userRepository.save(user.get()));
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data(user.get().getId())
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
