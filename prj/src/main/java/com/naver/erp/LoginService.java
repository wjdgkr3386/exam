package com.naver.erp;

import java.util.Map;

public interface LoginService {
	
	int checkSignin( Map<String,String> midPwd  ) throws Exception;
}
