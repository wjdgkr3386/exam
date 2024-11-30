package com.naver.erp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginDAO loginDAO;
	
	public int checkSignin(Map<String,String> midPwd){
		int checkCnt = loginDAO.checkSignin(midPwd);
		if(checkCnt>0) {
			return 1;
		}else if(checkCnt==0) {
			if(loginDAO.insertId(midPwd)>0) {
				return 2;
			}
				return 0;
		}else {
			return 0;
		}
	}

}
