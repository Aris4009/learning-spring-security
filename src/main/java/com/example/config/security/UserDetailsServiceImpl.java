package com.example.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.exception.BusinessException;
import com.example.mapper.RoleDao;
import com.example.mapper.UserDao;
import com.example.mapper.UserRoleDao;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private UserDao userDao;

	private RoleDao roleDao;

	private UserRoleDao userRoleDao;

	public UserDetailsServiceImpl(UserDao userDao, RoleDao roleDao, UserRoleDao userRoleDao) {
		this.userDao = userDao;
		this.roleDao = roleDao;
		this.userRoleDao = userRoleDao;
	}

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) {
		if (StrUtil.isEmpty(username)) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("username");
		}
		User user = userDao.templateOne(new User(username));
		if (ObjectUtil.isNull(user)) {
			throw BusinessException.invalidUsernameOrPasswordError();
		}

		UserRole userRole = userRoleDao.templateOne(new UserRole(user.getId()));
		if (ObjectUtil.isNull(userRole)) {
			throw BusinessException.invalidRoleError();
		}

		Role role = roleDao.templateOne(new Role(userRole.getRoleId()));
		if (ObjectUtil.isNull(role)) {
			throw BusinessException.invalidRoleError();
		}

	}
}
