package com.web.oa.junit;

import org.apache.shiro.crypto.hash.Md5Hash;

public class Md5Demo {

	public static void main(String[] args) {
		// 进行md5加密处理
		String salt = "eteokues";// 盐
		Md5Hash md5Hash = new Md5Hash("123", salt, 2);
		System.out.println(md5Hash.toString());
	}
}
