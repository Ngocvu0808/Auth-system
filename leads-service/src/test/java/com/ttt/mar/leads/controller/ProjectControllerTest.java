package com.ttt.mar.leads.controller;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.ProjectDetailResponse;
import com.ttt.mar.leads.dto.ProjectRequestDto;
import com.ttt.mar.leads.dto.ProjectResponseDto;
import com.ttt.mar.leads.dto.ProjectUpdateRequest;
import com.ttt.mar.leads.dto.ProjectUpdateStatusDto;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.dto.PutMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * @author bontk
 * @created_date 20/04/2021
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectControllerTest {

  @Mock
  private ProjectController projectController;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletResponse httpServletResponse;

  @Mock
  private AuthGuard authGuard;

  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

  @Before
  public void setUp() throws UserNotFoundException, ProxyAuthenticationException {
    when(httpServletRequest.getHeader("Authorization")).thenReturn("123");
    when(authGuard.getUserId(httpServletRequest)).thenReturn(1);
  }

  @Test
  public void getAllProjectTest() throws ProxyAuthenticationException {

    List<ProjectResponseDto> projects = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ProjectResponseDto p1 = new ProjectResponseDto();
      p1.setId(i + 1);
      p1.setCode("PROJECT_0" + (i + 1));
      p1.setName("Project " + (i + 1));
      p1.setPartnerCode("partner_code_" + (i + 1));
      p1.setStartDate(new Date());
      p1.setEndDate(new Date());
      projects.add(p1);
    }
    DataPagingResponse<ProjectResponseDto> data = new DataPagingResponse<>();
    data.setList(projects);
    data.setCurrentPage(1);
    data.setNum(projects.size());
    data.setTotalPage(1);

    ResponseEntity responseEntity = ResponseEntity.ok(GetMethodResponse.builder()
        .status(true).data(data).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.GET_ALL_PROJECT)).thenReturn(true);
    when(projectController.getAll(httpServletRequest, 1, 10, 0L, 0L, null, "", ""))
        .thenReturn(responseEntity);

    ResponseEntity resp = projectController.getAll(httpServletRequest, 1, 10, 0L, 0L, null, "", "");

    GetMethodResponse<DataPagingResponse<ProjectResponseDto>> body = (GetMethodResponse<DataPagingResponse<ProjectResponseDto>>) resp
        .getBody();
    DataPagingResponse<ProjectResponseDto> bodyData = body.getData();
    List<ProjectResponseDto> projectList = bodyData.getList();

    Assertions.assertEquals(projectList.size(), 5);
    Assertions.assertEquals(projectList.get(0).getCode(), "PROJECT_01");
  }

  @Test
  public void createProjectTest()
      throws ParseException, ProxyAuthenticationException {
    ProjectRequestDto dto = new ProjectRequestDto();
    dto.setCode("MH3701");
    dto.setName("MH3701-VPB");
    dto.setPartnerCode("VPB");
    dto.setStartDate(simpleDateFormat.parse("16/04/2020"));
    dto.setEndDate(simpleDateFormat.parse("27/05/2021"));

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.ADD_PROJECT)).thenReturn(true);
    ResponseEntity responseEntity = ResponseEntity.ok(PostMethodResponse.builder()
        .status(true).id(1).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());

    when(projectController.createProject(httpServletRequest, httpServletResponse, dto))
        .thenReturn(responseEntity);

    ResponseEntity responseEntityResult = projectController
        .createProject(httpServletRequest, httpServletResponse, dto);
    PostMethodResponse<Integer> body = (PostMethodResponse<Integer>) responseEntityResult
        .getBody();
    Integer id = body.getId();
    Assertions.assertEquals(id, 1);
  }

  @Test
  public void updateStatusProjectTest() throws ProxyAuthenticationException {
    Integer projectId = 1;
    ProjectUpdateStatusDto dto = new ProjectUpdateStatusDto();
    dto.setId(projectId);
    dto.setStatus(ProjectStatus.ACTIVE);

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.UPDATE_STATUS_PROJECT)).thenReturn(true);

    ResponseEntity responseEntity = ResponseEntity.ok(PutMethodResponse.builder()
        .status(true).id(projectId).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());
    when(projectController
        .updateStatusProject(httpServletRequest, httpServletResponse, projectId, dto))
        .thenReturn(responseEntity);
    ResponseEntity result = projectController
        .updateStatusProject(httpServletRequest, httpServletResponse, projectId, dto);

    PutMethodResponse<Integer> body = (PutMethodResponse<Integer>) result
        .getBody();
    Integer id = body.getId();
    Assertions.assertEquals(id, projectId);
  }

  @Test
  public void getProjectByIdTest() throws ProxyAuthenticationException, ParseException {
    Integer projectId = 1;
    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.GET_PROJECT_BY_ID)).thenReturn(true);

    ProjectDetailResponse projectDetailResponse = new ProjectDetailResponse();
    projectDetailResponse.setId(projectId);
    projectDetailResponse.setName("Project-1");
    projectDetailResponse.setCode("P001");
    projectDetailResponse.setPartnerCode("VP-001");
    projectDetailResponse.setStartDate(simpleDateFormat.parse("16/04/2020"));
    projectDetailResponse.setEndDate(simpleDateFormat.parse("27/05/2021"));
    projectDetailResponse.setStatus(ProjectStatus.ACTIVE);
    projectDetailResponse.setCreatedTime(simpleDateFormat.parse("27/05/2021"));
    projectDetailResponse.setCreatorName("admin");

    ResponseEntity responseEntity = ResponseEntity.ok(GetMethodResponse.builder()
        .status(true).data(projectDetailResponse).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());

    when(projectController.getProjectById(httpServletRequest, httpServletResponse, projectId))
        .thenReturn(responseEntity);
    ResponseEntity result = projectController
        .getProjectById(httpServletRequest, httpServletResponse, projectId);
    GetMethodResponse<ProjectDetailResponse> body = (GetMethodResponse<ProjectDetailResponse>) result
        .getBody();
    ProjectDetailResponse data = body.getData();
    Assertions.assertNotNull(data);
  }

  @Test
  public void updateProjectTest() throws ParseException, ProxyAuthenticationException {
    Integer projectId = 1;
    ProjectUpdateRequest projectUpdateRequest = new ProjectUpdateRequest();
    projectUpdateRequest.setId(projectId);
    projectUpdateRequest.setName("Project-Name");
    projectUpdateRequest.setPartnerCode("VPB");
    projectUpdateRequest.setStartDate(simpleDateFormat.parse("16/04/2020"));
    projectUpdateRequest.setEndDate(simpleDateFormat.parse("27/05/2021"));
    projectUpdateRequest.setNote("Note");

    when(authGuard.checkPermission(httpServletRequest, null, PermissionObjectCode.LEADS,
        ServicePermissionCode.UPDATE_PROJECT)).thenReturn(true);
    ResponseEntity responseEntity = ResponseEntity.ok(PutMethodResponse.builder()
        .status(true).id(projectId).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());
    when(projectController
        .updateProject(httpServletRequest, httpServletResponse, projectId, projectUpdateRequest))
        .thenReturn(responseEntity);
    ResponseEntity result = projectController
        .updateProject(httpServletRequest, httpServletResponse, projectId, projectUpdateRequest);

    PutMethodResponse<Integer> body = (PutMethodResponse<Integer>) result.getBody();
    Integer id = body.getId();
    Assertions.assertEquals(id, projectId);
  }

  @Test
  public void deleteProjectTest() {
    Integer projectId = 1;

    ResponseEntity responseEntity = ResponseEntity.ok(DeleteMethodResponse.builder()
        .status(true).errorCode(HttpStatus.OK.name().toLowerCase())
        .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
        .build());
    when(projectController
        .deleteProject(httpServletRequest, httpServletResponse, projectId))
        .thenReturn(responseEntity);
    ResponseEntity result = projectController
        .deleteProject(httpServletRequest, httpServletResponse, projectId);
    DeleteMethodResponse body = (DeleteMethodResponse) result.getBody();

    boolean status = body.getStatus();
    Assertions.assertEquals(status, true);
  }
}
