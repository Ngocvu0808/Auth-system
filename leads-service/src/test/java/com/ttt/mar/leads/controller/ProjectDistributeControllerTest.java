package com.ttt.mar.leads.controller;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.DistributeByProjectResponse;
import com.ttt.mar.leads.dto.ProjectDistributeResponseDto;
import com.ttt.mar.leads.dto.ProjectSourceResponse;
import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author KietDT
 * @created_date 28/04/2021
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectDistributeControllerTest {

  @Mock
  private ProjectDistributeController projectDistributeController;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletResponse httpServletResponse;

  @Mock
  private AuthGuard authGuard;

  private final Integer prjectId = 1;

  @Before
  public void setUp() throws UserNotFoundException, ProxyAuthenticationException {
    when(httpServletRequest.getHeader("Authorization")).thenReturn("123");
    when(authGuard.getUserId(httpServletRequest)).thenReturn(1);
  }

  @Test
  public void getProjectDistributeFilterTest() throws ProxyAuthenticationException {
    List<ProjectDistributeResponseDto> projectDistributeResponseDtos = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ProjectDistributeResponseDto distributeResponseDto = new ProjectDistributeResponseDto();
      distributeResponseDto.setId(i + 1);
      distributeResponseDto.setName("name " + (i + 1));
      distributeResponseDto.setMethod(ApiMethod.GET);
      distributeResponseDto.setStatus(DistributeApiStatus.ACTIVE);
      distributeResponseDto.setUrl("url: " + (i + 1));
      projectDistributeResponseDtos.add(distributeResponseDto);
    }
    DataPagingResponse<ProjectDistributeResponseDto> data = new DataPagingResponse<>();
    data.setList(projectDistributeResponseDtos);
    data.setCurrentPage(1);
    data.setNum(projectDistributeResponseDtos.size());
    data.setTotalPage(1);

    ResponseEntity responseEntity = ResponseEntity.ok(GetMethodResponse.builder()
        .status(true).data(data).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.GET_PROJECT_BY_ID)).thenReturn(true);
    when(projectDistributeController
        .getProjectDistributeFilter(httpServletRequest, httpServletResponse, prjectId, "", 1, 10))
        .thenReturn(responseEntity);

    ResponseEntity resp = projectDistributeController
        .getProjectDistributeFilter(httpServletRequest, httpServletResponse, prjectId, "", 1, 10);
    GetMethodResponse<DataPagingResponse<ProjectSourceResponse>> body = (GetMethodResponse<DataPagingResponse<ProjectSourceResponse>>) resp
        .getBody();
    DataPagingResponse<ProjectSourceResponse> bodyData = body.getData();
    List<ProjectSourceResponse> projectList = bodyData.getList();

    Assertions.assertEquals(projectList.size(), 5);
  }

  @Test
  public void getDistributeNotExistProjectTest() throws ProxyAuthenticationException {
    List<DistributeByProjectResponse> data = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      DistributeByProjectResponse response = new DistributeByProjectResponse();
      response.setId(i);
      response.setName("name " + i);
      data.add(response);
    }
    ResponseEntity responseEntity = ResponseEntity.ok(GetMethodResponse.builder()
        .status(true).data(data).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.GET_PROJECT_BY_ID)).thenReturn(true);
    when(projectDistributeController
        .getDistributeNotExistProject(httpServletRequest, httpServletResponse, prjectId,
            DistributeApiStatus.ACTIVE)).thenReturn(responseEntity);

    ResponseEntity response = projectDistributeController
        .getDistributeNotExistProject(httpServletRequest, httpServletResponse, prjectId,
            DistributeApiStatus.ACTIVE);
    GetMethodResponse<List<DistributeByProjectResponse>> body = (GetMethodResponse<List<DistributeByProjectResponse>>) response
        .getBody();
    List<DistributeByProjectResponse> distributeByProjectResponses = body.getData();
    Assertions.assertEquals(data.size(), distributeByProjectResponses.size());
  }

  @Test
  public void deleteProjectSourceTest() throws ProxyAuthenticationException {
    Integer projectSourceId = 1;
    ResponseEntity responseEntity = ResponseEntity.ok(DeleteMethodResponse.builder()
        .status(true).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());
    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.GET_PROJECT_BY_ID)).thenReturn(true);
    when(projectDistributeController
        .deleteProjectDistribute(httpServletRequest, httpServletResponse, prjectId,
            projectSourceId))
        .thenReturn(responseEntity);

    ResponseEntity response = projectDistributeController
        .deleteProjectDistribute(httpServletRequest, httpServletResponse, prjectId,
            projectSourceId);

    DeleteMethodResponse body = (DeleteMethodResponse) response
        .getBody();
    Boolean status = body.getStatus();
    Assertions.assertEquals(status, true);
  }
}
