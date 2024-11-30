package com.naver.erp;

import java.util.List;
import java.util.Map;

public interface ProblemService {
	
	int insertExam(ExamDTO examDTO) throws Exception;
	
	int updateProblem(ExamDTO examDTO) throws Exception;
	
	int deleteExam(String exam_code) throws Exception;
	
	int insertSolv(ExamDTO examDTO) throws Exception;
}
