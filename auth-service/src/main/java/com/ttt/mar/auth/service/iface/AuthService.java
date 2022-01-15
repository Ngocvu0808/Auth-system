package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.dto.app.ClientDetailDto;
import com.ttt.mar.auth.dto.app.ClientRequestDto;
import com.ttt.mar.auth.dto.app.ClientResponseDto;
import com.ttt.mar.auth.dto.app.ClientWhiteListResponseDto;
import com.ttt.mar.auth.dto.app.RefreshTokenDto;
import com.ttt.mar.auth.dto.app.TokenRequestDto;
import com.ttt.mar.auth.dto.app.UpdateClientDto;
import com.ttt.mar.auth.dto.app.UserActivityRequestDto;
import com.ttt.mar.auth.dto.auth.GuestAccessRequestDto;
import com.ttt.mar.auth.dto.auth.GuestAccessResponseDto;
import com.ttt.mar.auth.dto.auth.LoginRequestDto;
import com.ttt.mar.auth.dto.auth.LoginResponseDto;
import com.ttt.mar.auth.dto.auth.UserLoginResponseDto;
import com.ttt.mar.auth.entities.application.Client;
import com.ttt.mar.auth.entities.enums.ClientStatus;
import com.ttt.mar.auth.entities.enums.RefreshTokenStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.CryptoException;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.UnAuthorizedException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

  LoginResponseDto checkAuthenticate(HttpServletRequest request) throws UnAuthorizedException;

  LoginResponseDto reloadPermission(HttpServletRequest request)
      throws UnAuthorizedException, IdentifyBlankException;

  Boolean validateToken(HttpServletRequest request) throws UnAuthorizedException;

  void logout(HttpServletRequest request, int userId)
      throws UnAuthorizedException, ServletException;

  LoginResponseDto login(HttpServletRequest request, LoginRequestDto loginRequestDto)
      throws ResourceNotFoundException, IdentifyBlankException, UnAuthorizedException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, CryptoException;

  void reloadPermission(List<Integer> userIdList);

  String genAPIKey(int userId) throws ResourceNotFoundException, NoSuchAlgorithmException;

  String getExistAPIKey(int userId) throws ResourceNotFoundException, NoSuchAlgorithmException;

  String reloadAPIKey(int userId) throws ResourceNotFoundException, NoSuchAlgorithmException;

  Client addClient(ClientRequestDto clientRequestDto, Integer creatorId)
      throws IdentifyBlankException, OperationNotImplementException, DuplicateEntityException, ResourceNotFoundException;

  DataPagingResponse<ClientResponseDto> getAllClient(Integer userId, Integer page, Integer limit,
      String search, ClientStatus status, String sort, Boolean isGetAll);

  ClientDetailDto getClientById(Integer id) throws ResourceNotFoundException;

  void updateClient(Integer id, UpdateClientDto dto) throws ResourceNotFoundException;

  void deleteClient(Integer id) throws ResourceNotFoundException, OperationNotImplementException;

  Client changeStatusClient(Integer clientId, ClientStatus status)
      throws ResourceNotFoundException, OperationNotImplementException;

  // add, remove ip
  void addIp(Integer userId, Integer clientId, List<String> ipList)
      throws ResourceNotFoundException;

  void removeIp(Integer userId, Integer clientId, List<String> ipList)
      throws ResourceNotFoundException;

  ClientWhiteListResponseDto getAllIpOfClient(Integer userId, Integer clientId)
      throws ResourceNotFoundException;

  Map<String, Object> getToken(HttpServletRequest request, TokenRequestDto tokenRequestDto)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException, UnAuthorizedException;

  // Approve, un approve refresh token
  void approveToken(HttpServletRequest request, Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void unApproveToken(Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void changeTokenStatus(Long refreshTokenId, Integer userId, RefreshTokenStatus status)
      throws ResourceNotFoundException, OperationNotImplementException;

  void deleteToken(Long refreshTokenId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  Map<String, Object> getAccessTokenFromRefreshToken(HttpServletRequest request,
      String refreshToken)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException, UnAuthorizedException;

  DataPagingResponse<RefreshTokenDto> getAllRefreshTokenOfUser(Integer userId, Integer page,
      Integer limit, String status, String sort) throws ResourceNotFoundException;

  GuestAccessResponseDto guestAccess(GuestAccessRequestDto request) throws NoSuchAlgorithmException;

  void temporaryClose(HttpServletRequest request) throws ResourceNotFoundException;

  void saveUserActivity(UserActivityRequestDto dto);
}
