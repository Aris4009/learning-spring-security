package com.example.service;

import org.beetl.sql.core.SqlId;
import org.beetl.sql.core.page.DefaultPageResult;
import org.beetl.sql.core.page.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.User;
import com.example.exception.BusinessException;
import com.example.mapper.UserDao;

import cn.hutool.core.util.StrUtil;

@Service
@Transactional
public class UserService {

	private UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public User insert(User user) throws BusinessException {
		addValidator(user);
		userDao.insert(user);
		return user;
	}

	public User update(User user) throws BusinessException {
		updateValidator(user);
		userDao.updateTemplateById(user);
		user = userDao.templateOne(user);
		return user;
	}

	public int delete(User user) throws BusinessException {
		updateValidator(user);
		return userDao.deleteById(user.getId());
	}

	public DefaultPageResult<User> page(PageRequest<User> pageRequest) {
		SqlId sqlId = SqlId.of("user", "page");
		return (DefaultPageResult<User>) userDao.getSQLManager().pageQuery(sqlId, User.class, pageRequest.getParas(),
				pageRequest);
	}

	private void addValidator(User user) throws BusinessException {
		if (user == null) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError();
		}

		if (StrUtil.isEmpty(user.getUsername()) || StrUtil.isEmpty(user.getPassword())) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("username", "password");
		}
	}

	private void updateValidator(User user) throws BusinessException {
		if (user == null) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError();
		}

		if (user.getId() == null) {
			throw BusinessException.paramsMustBeNotEmptyOrNullError("id");
		}
	}
}
