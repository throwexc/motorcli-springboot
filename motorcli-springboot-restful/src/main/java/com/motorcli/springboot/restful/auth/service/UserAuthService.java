package com.motorcli.springboot.restful.auth.service;

import com.motorcli.springboot.restful.auth.UserInfo;

public interface UserAuthService {

    UserInfo findByUsername(String username);
}
