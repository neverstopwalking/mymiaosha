package com.zzj.miaosha.domain;

import java.util.Date;

public class MiaoShaUser {
	private Long id;
	private String nickname;
	private String password;
	private String salt;
	private String head;
	private int login_count;
	private Date register_Date;
	private Date LastLoginDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getLogin_count() {
		return login_count;
	}
	public void setLogin_count(int login_count) {
		this.login_count = login_count;
	}
	public Date getRegister_Date() {
		return register_Date;
	}
	public void setRegister_Date(Date register_Date) {
		this.register_Date = register_Date;
	}
	public Date getLastLoginDate() {
		return LastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		LastLoginDate = lastLoginDate;
	}
	
	
	
	
	
	
}
