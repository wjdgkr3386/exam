package com.naver.erp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProblemServiceImpl implements ProblemService {

	@Autowired
	ProblemDAO problemDAO;
	
	public int insertExam(ExamDTO examDTO){
		int problemCnt=0;

		if(problemDAO.examTitleCheck(examDTO)>0) { problemCnt=3; }
		else if(problemDAO.examCodeCheck(examDTO.getExam_code())>0) { problemCnt=2; }
		else {
			if(problemDAO.insertExam_table(examDTO)<1) { problemCnt=0; }
			else if(problemDAO.insertProblem_table(examDTO)>0) { problemCnt=1; }
		}
		return problemCnt;
	}

	public int updateProblem(ExamDTO examDTO){
		int updateCnt=0;
		String exam_code = examDTO.getExam_code();
		String is_last_save = examDTO.getIs_last_save();
        String folderPath = "C:/zzz/workspace_exam/prj/src/main/resources/static/img/" + exam_code;

        if(is_last_save.equals("최종"))
        	if(Util.isExtension(examDTO)==-13) { return -13; }
        
		if(problemDAO.updateExam(examDTO)<1) { updateCnt=0; }
		else if(problemDAO.deleteProblem(exam_code)<1) { updateCnt=0; }
		else if(problemDAO.insertProblem_table(examDTO)>0) { updateCnt=1; }

        if(is_last_save.equals("최종")) {
	        List<String> file_nameList = examDTO.getFile_name();
			if( file_nameList != null && !file_nameList.isEmpty() && file_nameList.size()>0 ) {
				if(problemDAO.deleteFile(exam_code)<1) { updateCnt=0; }
				if(problemDAO.insertFile_table(examDTO)<1) { updateCnt=0; }
				else {
					updateCnt=Util.saveFileToDirectory(examDTO);
				}
			} else {
				List<Map<String,String>> file_nameMapList = problemDAO.getFile_name(exam_code);
				if( file_nameMapList!=null && !file_nameMapList.isEmpty() && file_nameMapList.size()>0 ) {
					if( problemDAO.deleteFile(exam_code)<1 ) { updateCnt=0; }
					else { updateCnt=1; }
				}
				Util.fileDelete(folderPath);
			}
        }
		return updateCnt;
	}	
	
	public int deleteExam(String exam_code) {
		List<Map<String,String>> file_nameList = problemDAO.getFile_name(exam_code);
		String folderPath = "C:/zzz/workspace_exam/prj/src/main/resources/static/img/" + exam_code;
		int deleteCount=0;
		
		if( !file_nameList.isEmpty() && file_nameList.size()>0 ) {
			if(problemDAO.deleteFile(exam_code)<1) { deleteCount=0;	}
			else {
				Util.fileDelete(folderPath);
			}
		}
		problemDAO.deleteSolv(exam_code);
		if(problemDAO.deleteProblem(exam_code)<1) { deleteCount=0; }
		else if(problemDAO.deleteExam(exam_code)>0) { deleteCount=1; }
		
		return deleteCount;
	}
	
	public int insertSolv(ExamDTO examDTO) {
		if(problemDAO.nicknameCheck(examDTO)>0) { return 2; }
		if(problemDAO.insertSolv(examDTO)>0) { return 1; }
		
		return 0;
	}
}
