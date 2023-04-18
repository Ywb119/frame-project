package com.frame.rpc.provider.netty.pojo;

import lombok.*;

import java.io.Serializable;


/**
 * @description
 * @author: ts
 * @create:2021-04-09 09:41
 */
@Getter
@Setter
@ToString
public class UserInfo implements Serializable {
	
	private  Integer id;
	private String name;
	private Integer age;
	private String gender;
	private String address;

	public UserInfo(Integer id, String name, Integer age, String gender, String address) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.address = address;
	}

	
}
