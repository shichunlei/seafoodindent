package com.project.seafoodindent.model;

/***
 * 用户实体类
 * 
 * @author 师春雷
 * 
 */
public class Courier {

	public String id;

	public String email;

	public String name;

	public String mobile;

	public String auth_token;

	public String created_at;

	public String updated_at;

	@Override
	public String toString() {
		return "courier [id=" + id + ", email=" + email + ", name=" + name
				+ ", mobile=" + mobile + ", auth_token=" + auth_token
				+ ", created_at=" + created_at + ", updated_at=" + updated_at
				+ "]";
	}

}