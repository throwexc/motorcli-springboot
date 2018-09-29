package com.motorcli.springboot.restful.auth.service;

import com.motorcli.springboot.restful.auth.UserInfo;
import com.motorcli.springboot.restful.auth.UserRole;

import java.util.List;

public interface RoleAuthService {

    List<UserRole> findRolesByUser(UserInfo userInfo);
}
