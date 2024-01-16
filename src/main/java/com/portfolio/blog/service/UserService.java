package com.portfolio.blog.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.blog.config.FileManagerService;
import com.portfolio.blog.dto.Token;
import com.portfolio.blog.dto.User;
import com.portfolio.blog.dto.request.UserUpdateRequest;
import com.portfolio.blog.dto.response.UserInfoResponse;
import com.portfolio.blog.dto.response.UserTokenResponse;
import com.portfolio.blog.exception.AppException;
import com.portfolio.blog.exception.ErrorCode;
import com.portfolio.blog.repository.JWTRepository;
import com.portfolio.blog.repository.UserRepository;
import com.portfolio.blog.utils.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final JWTRepository jwtRepository;
	
	// 비번 encoding
	private final BCryptPasswordEncoder encoder;
	
	// jwt토큰발행시 필요한 key. application.yml에 있음
	@Value("${jwt.secret}")
	private String key;
	
	// id를 통해 user찾기
	public User getUserById(int userId) {
		 return userRepository.findById(userId)
	                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, userId + " 해당 회원정보를 찾을 수 없습니다."));
	}
	
	// email를 통해 user찾기
	public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND, email + " 이메일은 찾을 수 없습니다."));
    }
	
	// nickName을 통해 user찾기
	public User getUserByNickName(String nickName) {
		return userRepository.findByNickName(nickName)
				.orElseThrow(() -> new AppException(ErrorCode.NICKNAME_NOT_FOUND, nickName + " 닉네임 찾을 수 없습니다."));
	}
	
	// userInfoResponse 객체 생성 및 정보 매핑
	public UserInfoResponse userInfoResponse(User user) {
        return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickName(), 
        		user.getUserImg(), user.getCreatedAt(), user.getUpdatedAt());
    }
	
	// 회원가입
	public void join(String email, String password, String nickName, MultipartFile file) {
		
		// email 중복체크
		userRepository.findByEmail(email)
			.ifPresent(user -> {
				throw new AppException(ErrorCode.EMAIL_DUPLICATED, email + " 이메일은 사용중입니다.");
			});
		
		// nickName 중복체크
		userRepository.findByNickName(nickName)
			.ifPresent(user -> {
                throw new AppException(ErrorCode.NICKNAME_DUPLICATED, nickName + " 닉네임은 사용중입니다.");
			});
		
		// userImg 경로
		String userImg = "null";
		if (file != null) {
			userImg = FileManagerService.saveUserFile(email, file);
		}
		
		// 저장
		User user = User.builder()
				.email(email)
				.password(encoder.encode(password))
				.nickName(nickName)
				.userImg(userImg)
				.role(Collections.singletonList("ROLE_USER")) // 가입시 모두 user 부여
				.build();
		userRepository.save(user);
		
	}
	
	// 로그인
	public UserTokenResponse login(String email, String password) {		
		// email 없음
		User user = getUserByEmail(email);
		
		// password 틀림
		if(!encoder.matches(password, user.getPassword())) {
			throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
		}
		
		// 토큰 발행
		String accessToken = JwtUtil.createAccessToken(user.getEmail(), key);
		String refreshToken = JwtUtil.createRefreshToken(user.getEmail(), key);
		
		// Refresh토큰 있는지 확인
		Optional<Token> token = jwtRepository.findByUserId(user.getId());
		
		if(token.isPresent()) {
			// refreshtoken 있으면 db 갱신
            jwtRepository.save(token.get().updateToken(refreshToken));
        }else {
        	// refreshtoken 없으면 db 새로 생성
    		Token newToken = Token.builder()
    				.userId(user.getId())
    				.refreshToken(refreshToken)
    				.build();
    		jwtRepository.save(newToken);
        }
		
		// 사용자의 필수정보를 객체에 담아서 리턴		
		UserInfoResponse userInfoResponse = userInfoResponse(user);
		
		
		UserTokenResponse userToken = UserTokenResponse.builder()
				.userId(user.getId())
				.accessToken(accessToken)
				.userInfoResponse(userInfoResponse)
				.build();
		
		return userToken;
	}
	
	// 정보 수정
	public UserInfoResponse update(
			String email
			, UserUpdateRequest dto) {
		
		// 만약 사용자가 없으면
		User user = getUserByEmail(email);
		
		// nickName 중복체크
		userRepository.findByNickName(dto.getNickName())
			.ifPresent(existingUser -> {
                if (existingUser.getId() != (user.getId())) {
                    throw new AppException(ErrorCode.NICKNAME_DUPLICATED, dto.getNickName() + " 닉네임은 사용중입니다.");
                }
			});
		
		// 수정 저장 
		user.setNickName(dto.getNickName());
		
		// 비번 변경하는 경우에만 비번 업데이트
		if(dto.getNewPw() != null) {
			user.setPassword(encoder.encode(dto.getNewPw()));
		}
		
		// userImg 변경하는 경우에만 img 업데이트
		if(dto.getUserImg() != null ) {
			String userImg = FileManagerService.saveUserFile(email, dto.getUserImg());
			user.setUserImg(userImg);
		}
		
		userRepository.save(user);		
		
		// userInfoResponse 담아서 리턴		
		return userInfoResponse(user);
	}
	
	// 개별 userInfoResponse 리턴
	public UserInfoResponse getUserInfoById(int userId) {
		// user 찾기
		User user = getUserById(userId);
        
        // userInfoResponse 담아서 리턴	
        return userInfoResponse(user);
    }
}
