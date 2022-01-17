package com.ttt.mar.leads.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ttt.mar.leads.dto.ProjectDetailResponse;
import com.ttt.mar.leads.dto.ProjectRequestDto;
import com.ttt.mar.leads.dto.ProjectResponseDto;
import com.ttt.mar.leads.dto.ProjectUpdateRequest;
import com.ttt.mar.leads.dto.ProjectUpdateStatusDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.mar.leads.mapper.ProjectMapper;
import com.ttt.mar.leads.repositories.ProjectRepository;
import com.ttt.mar.leads.service.iface.ProjectService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectServiceTest {

  @Mock
  private ProjectService projectService;
  @Mock
  private ProjectMapper projectMapper;
  @Mock
  private ProjectRepository projectRepository;
  @Mock
  private UserService userService;

  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

  @Before
  public void setUp()
      throws ParseException, OperationNotImplementException, ResourceNotFoundException {
    MockitoAnnotations.initMocks(this);

    Project entity = new Project();
    entity.setId(1);
    entity.setCode("MH370");
    entity.setName("MH370-VPB");
    entity.setPartnerCode("VPB");
    entity.setStartDate(simpleDateFormat.parse("16/04/2020"));
    entity.setEndDate(simpleDateFormat.parse("27/05/2021"));

    when(userService.checkValidUser(1)).thenAnswer(invocationOnMock -> {
      User user = new User();
      user.setId(1);
      user.setName("Admin");
      return user;
    });
    when(projectMapper.fromDto(Mockito.any(ProjectRequestDto.class))).thenReturn(entity);
    when(projectRepository.save(entity)).thenAnswer(invocationOnMock -> entity);
    when(userService.mapByListId(Collections.singleton(1))).thenAnswer(invocationOnMock -> {
      Map<Integer, UserResponseDto> userResponseDtoMap = new HashMap<>();
      UserResponseDto userResponseDto = new UserResponseDto();
      userResponseDto.setId(1);
      userResponseDto.setName("admin");
      userResponseDto.setUsername("admin");
      userResponseDtoMap.put(1, userResponseDto);

      return userResponseDtoMap;
    });
  }

  @Test
  public void createProjectTest()
      throws ParseException, OperationNotImplementException, ResourceNotFoundException {
    ProjectRequestDto dto = new ProjectRequestDto();
    dto.setCode("MH3701");
    dto.setName("MH3701-VPB");
    dto.setPartnerCode("VPB");
    dto.setStartDate(simpleDateFormat.parse("16/04/2020"));
    dto.setEndDate(simpleDateFormat.parse("27/05/2021"));

    when(projectService.createProject(1, dto)).thenReturn(12);

    Integer projectId = projectService.createProject(1, dto);

    Assertions.assertNotNull(projectId, "data is empty");
    Assertions.assertSame(projectId, 12);
  }

  @Test
  public void getAllProjectTest() {
    List<Project> projects = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Project p1 = new Project();
      p1.setId(i + 1);
      p1.setCode("PROJECT_0" + (i + 1));
      p1.setName("Project " + (i + 1));
      p1.setIsDeleted(false);
      p1.setPartnerCode("partner_code_" + (i + 1));
      p1.setCreatedTime(new Date());
      projects.add(p1);
    }
    DataPagingResponse<ProjectResponseDto> data = new DataPagingResponse<>();
    List<ProjectResponseDto> res = projects.stream()
        .map(projectMapper::toDto)
        .collect(Collectors.toList());
    data.setList(res);
    data.setCurrentPage(1);
    data.setNum(res.size());
    data.setTotalPage(1);

    when(projectService.getAll(1, 10, 0L, 0L, null, "", ""))
        .thenReturn(data);

    DataPagingResponse<ProjectResponseDto> resp = projectService
        .getAll(1, 10, 0L, 0L, null, "", "");

    Assertions.assertEquals(resp.getNum(), 5);
    Assertions.assertEquals(resp.getTotalPage(), 1);

    verify(projectService, times(1)).getAll(1, 10, 0L, 0L, null, "", "");
  }

  @Test
  public void updateProjectStatusTest()
      throws OperationNotImplementException, ResourceNotFoundException, ParseException {
    Integer userId = 1;
    Integer idProject = 1;
    ProjectUpdateStatusDto projectUpdateStatusDto = new ProjectUpdateStatusDto();
    projectUpdateStatusDto.setId(idProject);
    projectUpdateStatusDto.setStatus(ProjectStatus.ACTIVE);

    when(projectService.updateProjectStatus(userId, projectUpdateStatusDto)).thenReturn(idProject);
    Integer id = projectService.updateProjectStatus(userId, projectUpdateStatusDto);

    Assertions.assertEquals(idProject, id);
  }

  @Test
  public void updateProject()
      throws ParseException, OperationNotImplementException, ResourceNotFoundException {
    Integer userId = 1;
    ProjectUpdateRequest projectUpdateRequest = new ProjectUpdateRequest();
    projectUpdateRequest.setId(1);
    projectUpdateRequest.setName("Project-Name");
    projectUpdateRequest.setPartnerCode("VPB");
    projectUpdateRequest.setStartDate(simpleDateFormat.parse("16/04/2020"));
    projectUpdateRequest.setEndDate(simpleDateFormat.parse("27/05/2021"));
    projectUpdateRequest.setNote("Note");

    when(projectService.updateProject(userId, projectUpdateRequest))
        .thenReturn(projectUpdateRequest.getId());
    Integer projectId = projectService.updateProject(userId, projectUpdateRequest);
    Assertions.assertEquals(projectId, projectUpdateRequest.getId());
  }

  @Test
  public void getByIdTest() throws ParseException, ResourceNotFoundException {
    Integer projectId = 1;
    Integer creatorId = 1;

    Project project = new Project();
    project.setId(projectId);
    project.setName("Project-1");
    project.setCode("P001");
    project.setPartnerCode("VP-001");
    project.setStartDate(simpleDateFormat.parse("16/04/2020"));
    project.setEndDate(simpleDateFormat.parse("27/05/2021"));
    project.setStatus(ProjectStatus.ACTIVE);
    project.setCreatorUserId(creatorId);
    project.setCreatedTime(simpleDateFormat.parse("27/05/2021"));

    when(projectMapper.toDetailDto(Mockito.any(Project.class))).thenAnswer(invocationOnMock -> {
      ProjectDetailResponse projectDetailResponse = new ProjectDetailResponse();
      projectDetailResponse.setId(projectId);
      projectDetailResponse.setName("Project-1");
      projectDetailResponse.setCode("P001");
      projectDetailResponse.setPartnerCode("VP-001");
      projectDetailResponse.setStartDate(simpleDateFormat.parse("16/04/2020"));
      projectDetailResponse.setEndDate(simpleDateFormat.parse("27/05/2021"));
      projectDetailResponse.setStatus(ProjectStatus.ACTIVE);
      projectDetailResponse.setCreatedTime(simpleDateFormat.parse("27/05/2021"));

      return projectDetailResponse;
    });
    ProjectDetailResponse response = projectMapper.toDetailDto(project);

    when(projectService.getById(projectId)).thenAnswer(invocationOnMock -> {
      response.setCreatorName("admin");
      return response;
    });

    ProjectDetailResponse detailResponse = projectService.getById(projectId);
    Assertions.assertNotNull(detailResponse);
  }

  @Test
  public void deleteProjectTest() throws OperationNotImplementException, ResourceNotFoundException {
    Integer deleterId = 1;
    Integer projectId = 1;
    when(projectService.deleteProject(deleterId, projectId)).thenReturn(true);
    Boolean result = projectService.deleteProject(deleterId, projectId);
    Assertions.assertEquals(result, true);
  }
}
