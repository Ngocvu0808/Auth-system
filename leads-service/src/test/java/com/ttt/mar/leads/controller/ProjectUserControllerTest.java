package com.ttt.mar.leads.controller;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.dto.ProjectUserResponseDto;
import com.ttt.mar.leads.entities.ProjectUser;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.ArrayList;
import java.util.Date;
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

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectUserControllerTest {

  @Mock
  private ProjectUserController projectUserController;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletResponse httpServletResponse;

  @Mock
  private AuthGuard authGuard;

  @Before
  public void setUp() throws UserNotFoundException, ProxyAuthenticationException {
    when(httpServletRequest.getHeader("Authorization")).thenReturn("123");
    when(authGuard.getUserId(httpServletRequest)).thenReturn(1);
  }

  @Test
  public void getProjectUserNotExist() {
    List<ProjectUserResponseDto> listDataUserProject = new ArrayList<ProjectUserResponseDto>();
    for (int i = 0; i < 5; i++) {
      ProjectUserResponseDto userProject = new ProjectUserResponseDto();
      userProject.setId(i + 1000);
      userProject.setName("test " + i);
      userProject.setRoleName("tester" + i);
      userProject.setUsername("Nambn" + i);
      userProject.setStatus(UserStatus.ACTIVE);
      listDataUserProject.add(userProject);
    }
    DataPagingResponse<ProjectUserResponseDto> data = new DataPagingResponse<>();
    data.setList(listDataUserProject);
    data.setCurrentPage(1);
    data.setNum(listDataUserProject.size());
    data.setTotalPage(1);
    // project id defaut for test =1
    Integer projectId = 1;

    ResponseEntity responseEntity = ResponseEntity
        .ok(GetMethodResponse.builder().status(true).data(data)
            .errorCode(HttpStatus.OK.name().toLowerCase())
            .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG).build());
    when(projectUserController
        .getUserNotExistProject(httpServletRequest, httpServletResponse, projectId,
            UserStatus.ACTIVE)).thenReturn(responseEntity);

    ResponseEntity resp = projectUserController
        .getUserNotExistProject(httpServletRequest, httpServletResponse,
            projectId, UserStatus.ACTIVE);
    GetMethodResponse<DataPagingResponse<ProjectUserResponseDto>> body = (GetMethodResponse<DataPagingResponse<ProjectUserResponseDto>>) resp
        .getBody();
    DataPagingResponse<ProjectUserResponseDto> bodyData = body.getData();
    List<ProjectUserResponseDto> projectList = bodyData.getList();
    Assertions.assertEquals(projectList.size(), 5);
    Assertions.assertEquals(projectList.get(0).getName(), "test 0");
  }

  @Test
  public void deleteUserOfProject() throws Exception {
    ProjectUser projectUser = new ProjectUser();
    projectUser.setCreatedTime(null);
    projectUser.setCreatorUserId(null);
    projectUser.setDeleted(false);
    projectUser.setDeleterUserId(null);
    projectUser.setId(10001);
    projectUser.setModifiedTime(new Date());
//		projectUser.setProject(projectService.getProjectById(1));
    projectUser.setUpdaterUserId(null);
    projectUser.setUserId(1811);
    // project id for test
    Integer projectId = 1;
    Integer projectUserId = 1;

    ResponseEntity responseEntity = ResponseEntity
        .ok(DeleteMethodResponse.builder().status(true)
            .errorCode(HttpStatus.OK.name().toLowerCase())
            .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG).build());
    when(projectUserController
        .deleteProject(httpServletRequest, httpServletResponse, projectId, 1811))
        .thenReturn(responseEntity);
    ResponseEntity result = projectUserController
        .deleteProject(httpServletRequest, httpServletResponse, projectId,
            projectUserId);
    DeleteMethodResponse body = (DeleteMethodResponse) result.getBody();

    boolean status = body.getStatus();
    Assertions.assertEquals(status, true);
  }

  @Test
  public void getListUserOfProject() throws Exception {
    List<ProjectUserResponseDto> projectUser = new ArrayList<ProjectUserResponseDto>();
    for (int i = 0; i < 3; i++) {
      ProjectUserResponseDto p1 = new ProjectUserResponseDto();
      p1.setId(111 + i);
      p1.setName("NamBN" + i);
      p1.setRoleName("tester" + i);
      p1.setStatus(UserStatus.ACTIVE);
      p1.setUsername("nam" + i);
      projectUser.add(p1);
    }
    DataPagingResponse<ProjectUserResponseDto> data = new DataPagingResponse<>();
    data.setList(projectUser);
    data.setCurrentPage(1);
    data.setNum(projectUser.size());
    data.setTotalPage(1);

    ResponseEntity responseEntity = ResponseEntity
        .ok(GetMethodResponse.builder().status(true).data(data)
            .errorCode(HttpStatus.OK.name().toLowerCase())
            .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG).build());
    when(projectUserController
        .getProjectUserFilter(httpServletRequest, httpServletResponse, 1, "", 1, 10))
        .thenReturn(responseEntity);
    ResponseEntity resp = projectUserController
        .getProjectUserFilter(httpServletRequest, httpServletResponse, 1, "",
            1, 10);

    GetMethodResponse<DataPagingResponse<ProjectUserResponseDto>> body = (GetMethodResponse<DataPagingResponse<ProjectUserResponseDto>>) resp
        .getBody();
    DataPagingResponse<ProjectUserResponseDto> bodyData = body.getData();
    List<ProjectUserResponseDto> projectList = bodyData.getList();
    Assertions.assertEquals(projectList.size(), 3);
    Assertions.assertEquals(projectList.get(0).getName(), "NamBN0");
  }

}
