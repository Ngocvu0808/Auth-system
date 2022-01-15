package com.ttt.mar.auth.service.iface;

import static org.mockito.Mockito.doReturn;

import com.ttt.mar.auth.mapper.RoleMapper;
import com.ttt.mar.auth.repositories.RoleExtendedRepository;
import com.ttt.rnd.lib.dto.RoleCustomDto;
import com.ttt.rnd.lib.entities.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author bontk
 * @created_date 20/04/2021
 */

@RunWith(MockitoJUnitRunner.class)
class RoleServiceTest {

  @Mock
  private RoleService roleService;

  @Mock
  private RoleMapper roleMapper;

  @Mock
  private RoleExtendedRepository roleRepository;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
    final int SIZE = 5;
    List<Role> models = new ArrayList<>();
    for (int i = 0; i < SIZE; i++) {
      Role role = new Role();
      role.setId(i + 1);
      role.setCode("ROLE_" + (i + 1));
      role.setName("ROLE " + (i + 1));
      role.setDefaultRole(false);
      role.setIsSystemRole(false);
      models.add(role);
    }
    doReturn(models).when(roleRepository).findAllByIsDeletedFalseAndTypeIdIn(new ArrayList<>());

    List<RoleCustomDto> dtos = models.stream()
        .map(roleMapper::toRoleCustomerDto)
        .collect(Collectors.toList());

    doReturn(dtos).when(roleService).findListRoleByTypes("");
  }

  @Test
  public void findListRoleByTypes() {
    List<RoleCustomDto> dtos = roleService.findListRoleByTypes("");

    Assertions.assertEquals(dtos.size(), 5);
    Assertions.assertEquals(dtos.get(0).getId(), 1);
  }

}