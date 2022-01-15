package com.ttt.mar.leads.controller;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.LeadSourceByProjectResponse;
import com.ttt.mar.leads.dto.ProjectDetailResponse;
import com.ttt.mar.leads.dto.ProjectResponseDto;
import com.ttt.mar.leads.dto.ProjectSourceResponse;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author KietDT
 * @created_date 28/04/2021
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectSourceControllerTest {

  @Mock
  private ProjectSourceController projectSourceController;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletResponse httpServletResponse;

  @Mock
  private AuthGuard authGuard;

  private final Integer prjectId = 1;

  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

  @Before
  public void setUp() throws UserNotFoundException, ProxyAuthenticationException {
    when(httpServletRequest.getHeader("Authorization")).thenReturn("123");
    when(authGuard.getUserId(httpServletRequest)).thenReturn(1);
  }

  @Test
  public void getProjectSourceFilterTest() throws ProxyAuthenticationException {
    List<ProjectSourceResponse> projectSourceResponses = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ProjectSourceResponse projectSourceResponse = new ProjectSourceResponse();
      projectSourceResponse.setId(i + 1);
      projectSourceResponse.setSource("Source " + (i + 1));
      projectSourceResponse.setNameSource("Name-Source " + (i + 1));
      projectSourceResponse.setStatus(LeadSourceStatus.ACTIVE);
      projectSourceResponse.setUtmSource("utm-source " + (i + 1));
      projectSourceResponses.add(projectSourceResponse);
    }
    DataPagingResponse<ProjectSourceResponse> data = new DataPagingResponse<>();
    data.setList(projectSourceResponses);
    data.setCurrentPage(1);
    data.setNum(projectSourceResponses.size());
    data.setTotalPage(1);

    ResponseEntity responseEntity = ResponseEntity.ok(GetMethodResponse.builder()
        .status(true).data(data).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.GET_PROJECT_BY_ID)).thenReturn(true);
    when(projectSourceController
        .getProjectSourceFilter(httpServletRequest, httpServletResponse, prjectId, "", 1, 10))
        .thenReturn(responseEntity);

    ResponseEntity resp = projectSourceController
        .getProjectSourceFilter(httpServletRequest, httpServletResponse, prjectId, "", 1, 10);

    GetMethodResponse<DataPagingResponse<ProjectSourceResponse>> body = (GetMethodResponse<DataPagingResponse<ProjectSourceResponse>>) resp
        .getBody();
    DataPagingResponse<ProjectSourceResponse> bodyData = body.getData();
    List<ProjectSourceResponse> projectList = bodyData.getList();

    Assertions.assertEquals(projectList.size(), 5);
  }

  @Test
  public void getLeadSourceNotExistProjectTest() throws ProxyAuthenticationException {
    List<LeadSourceByProjectResponse> data = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      LeadSourceByProjectResponse response = new LeadSourceByProjectResponse();
      response.setId(i);
      response.setName("Lead-Source" + i);
      data.add(response);
    }
    ResponseEntity responseEntity = ResponseEntity.ok(GetMethodResponse.builder()
        .status(true).data(data).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.GET_PROJECT_BY_ID)).thenReturn(true);
    when(projectSourceController
        .getLeadSourceNotExistProject(httpServletRequest, httpServletResponse, prjectId,
            LeadSourceStatus.ACTIVE)).thenReturn(responseEntity);

    ResponseEntity response = projectSourceController
        .getLeadSourceNotExistProject(httpServletRequest, httpServletResponse, prjectId,
            LeadSourceStatus.ACTIVE);
    GetMethodResponse<List<LeadSourceByProjectResponse>> body = (GetMethodResponse<List<LeadSourceByProjectResponse>>) response
        .getBody();
    List<LeadSourceByProjectResponse> leadSourceByProjectResponses = body.getData();
    Assertions.assertEquals(data.size(), leadSourceByProjectResponses.size());
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
    when(projectSourceController
        .deleteProjectSource(httpServletRequest, httpServletResponse, prjectId, projectSourceId))
        .thenReturn(responseEntity);

    ResponseEntity response = projectSourceController
        .deleteProjectSource(httpServletRequest, httpServletResponse, prjectId, projectSourceId);

    DeleteMethodResponse body = (DeleteMethodResponse) response
        .getBody();
    Boolean status = body.getStatus();
    Assertions.assertEquals(status, true);
  }
}
