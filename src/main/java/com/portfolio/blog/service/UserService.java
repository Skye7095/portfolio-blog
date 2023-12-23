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
	
	// 회원가입
	public String join(String email, String password, String nickName, MultipartFile file) {
		
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
		
		return "SUCCESS";
	}
	
	// 로그인
	public UserTokenResponse login(String email, String password) {
		// email 없음
		User selectedUser = userRepository.findByEmail(email)
					.orElseThrow(() -> {
						throw new AppException(ErrorCode.EMAIL_NOT_FOUND, " 이메일은 존재하지 않습니다.");
					});
		// password 틀림
		if(!encoder.matches(password, selectedUser.getPassword())) {
			throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
		}
		
		// 토큰 발행
		String accessToken = JwtUtil.createAccessToken(selectedUser.getEmail(), key);
		String refreshToken = JwtUtil.createRefreshToken(selectedUser.getEmail(), key);
		
		// Refresh토큰 있는지 확인
		Optional<Token> token = jwtRepository.findByUserId(selectedUser.getId());
		
		if(token.isPresent()) {
			// refreshtoken 있으면 db 갱신
            jwtRepository.save(token.get().updateToken(refreshToken));
        }else {
        	// refreshtoken 없으면 db 새로 생성
    		Token newToken = Token.builder()
    				.userId(selectedUser.getId())
    				.refreshToken(refreshToken)
    				.build();
    		jwtRepository.save(newToken);
        }
		
		// 사용자의 필수정보를 객체에 담아서 리턴		
		UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.id(selectedUser.getId())
				.email(selectedUser.getEmail())
				.nickName(selectedUser.getNickName())
				.userImg(selectedUser.getUserImg())
				.createdAt(selectedUser.getCreatedAt())
				.updatedAt(selectedUser.getUpdatedAt())
				.build();
		
		
		UserTokenResponse userToken = UserTokenResponse.builder()
				.userId(selectedUser.getId())
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
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.EMAIL_NOT_FOUND, email + "는 찾을 수 없는 회원입니다.");
				});
		
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
		UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.id(user.getId())
				.email(user.getEmail())
				.nickName(user.getNickName())
				.userImg(user.getUserImg())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
		
		return userInfoResponse;
	}
	
	// 사용자 정보 조회
	public UserInfoResponse getUserInfo(int userId){
		
		// 만약 사용자가 없으면
		User user = userRepository.findById(userId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.USER_NOT_FOUND, userId + "는 찾을 수 없는 회원입니다.");
				});
		
		// 정보를 userInfoResponse에 담아서 리턴
		UserInfoResponse userInfoResponse = UserInfoResponse.builder()
				.id(user.getId())
				.email(user.getEmail())
				.nickName(user.getNickName())
				.userImg(user.getUserImg())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
		
		return userInfoResponse;
	}
}
