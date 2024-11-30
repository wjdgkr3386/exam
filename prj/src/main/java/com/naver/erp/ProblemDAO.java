package com.naver.erp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemDAO {
	int examTitleCheck(ExamDTO examDTO);
	int examCodeCheck(String exam_code);
	int examMidCheck(ExamDTO examDTO);
	String examIs_last_saveCheck(ExamDTO examDTO);
	
	
	int searchResultCount(SearchDTO searchDTO);
	int problemAllCount(SearchDTO searchDTO);
	
	List<Map<String,Object>> search(SearchDTO searchDTO);
	
	List<Map<String,Object>> getCheck(CheckDTO checkDTO);
	int getProblemMax(CheckDTO checkDTO);
	int getExamineeCount(CheckDTO checkDTO);
	List<Map<String,String>> getFile_name(String exam_code);
	
	int insertExam_table(ExamDTO examDTO);
	int insertProblem_table(ExamDTO examDTO);
	int insertFile_table(ExamDTO examDTO);
	
	int nicknameCheck(ExamDTO examDTO);
	int insertSolv(ExamDTO examDTO);
	String getTitle(CheckDTO checkDTO);
	
	int deleteFile(String exam_code);
	int deleteProblem(String exam_code);
	int deleteSolv(String exam_code);
	int deleteExam(String exam_code);
	
	
	int updateExam(ExamDTO examDTO);
	
	List<Map<String,Object>> getProblem(String exam_code);
}
