package com.tteam.movieland.security;

import com.tteam.movieland.dto.UserDto;
import com.tteam.movieland.dto.mapper.EntityMapper;
import com.tteam.movieland.entity.User;
import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.repository.UserRepository;
import com.tteam.movieland.security.model.Credentials;
import com.tteam.movieland.security.model.SecuredResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    private final EntityMapper mapper;

    private final PasswordEncoder passwordEncoder;
    private Map<UserDto, UUID> cachedUser = new HashMap<>();

    @Override
    public SecuredResponse login(Credentials credentials) {
        String email = credentials.getEmail();
        String passwordRaw = credentials.getPassword();

        UserDto userDto = cachedUser.keySet().stream()
                .filter(x -> x.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (userDto != null && passwordEncoder.matches(passwordRaw, userDto.getPassword())){
            return SecuredResponse
                    .builder()
                    .nickname(userDto.getNickname())
                    .uuid(cachedUser.get(userDto))
                    .build();
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(
                String.format("User not found by provided email: %s", email)));

        String password = user.getPassword();
        if (!passwordEncoder.matches(credentials.getPassword(), password)) {
            throw new UserNotFoundException(
                    String.format("Incorrect password: %s", credentials.getPassword()));
        }

        UserDto dto = mapper.entityToDto(user);
        UUID uuid = UUID.randomUUID();
        MDC.put("usrEmail", dto.getEmail());
        MDC.put("usrUUID", uuid.toString());
        cachedUser.put(dto, uuid);

        return SecuredResponse
                .builder()
                .nickname(user.getNickname())
                .uuid(uuid)
                .build();
    }

    @Override
    public UserDto registration(UserDto userDto) {
        User user = mapper.dtoToEntity(userDto);

        String password = userDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);
        userDto.setPassword(encodedPassword);
        MDC.put("usrEmail", user.getEmail());
        userRepository.save(user);
        return userDto;
    }

    @Scheduled(cron = "${cron.interval.token}")
    private void clearCache() {
        log.info("Clearing user token cache...");
        cachedUser = new HashMap<>();
    }
}
