package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.DefaultPageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.exception.BusinessException;
import com.example.response.entity.Response;
import com.example.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	private final HttpServletRequest request;

	public UserController(UserService userService, HttpServletRequest request) {
		this.userService = userService;
		this.request = request;
	}

	@PostMapping("/insert")
	public Response<User> insert(@RequestBody User user) throws BusinessException {
		return Response.ok(userService.insert(user), request);
	}

	@PostMapping("/update")
	public Response<User> update(@RequestBody User user) throws BusinessException {
		return Response.ok(userService.update(user), request);
	}

	@PostMapping("/delete")
	public Response<Integer> delete(@RequestBody User user) throws BusinessException {
		return Response.ok(userService.delete(user), request);
	}

	@PostMapping("/page")
	public Response<DefaultPageResult<User>> page(@RequestBody DefaultPageRequest<User> pageRequest) {
		return Response.ok(userService.page(pageRequest), request);
	}
}
