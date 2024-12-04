package com.naver.erp;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginDAO {
	public int getLoginIdCnt( Map<String,String> idPwd  );

	public int checkSignin(Map<String,String> midPwd);
	
	public int insertId(Map<String,String> midPwd);
	
}

