package com.naver.erp;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ExamDTO {
	

	private String nickname;
	private String exam_title;
	private String exam_code;
	private List<String> problem_content;
	private List<String> problem_answer;
	private String is_last_save;
	private String mid;
	
	private List<MultipartFile> file;
	private List<String> file_name;
	
	
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getExam_title() {
		return exam_title;
	}
	public void setExam_title(String exam_title) {
		this.exam_title = exam_title;
	}
	public String getExam_code() {
		return exam_code;
	}
	public void setExam_code(String exam_code) {
		this.exam_code = exam_code;
	}
	public List<String> getProblem_content() {
		return problem_content;
	}
	public void setProblem_content(List<String> problem_content) {
		this.problem_content = problem_content;
	}
	public List<String> getProblem_answer() {
		return problem_answer;
	}
	public void setProblem_answer(List<String> problem_answer) {
		this.problem_answer = problem_answer;
	}
	public String getIs_last_save() {
		return is_last_save;
	}
	public void setIs_last_save(String is_last_save) {
		this.is_last_save = is_last_save;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public List<MultipartFile> getFile() {
		return file;
	}
	public void setFile(List<MultipartFile> file) {
		this.file = file;
	}
	public List<String> getFile_name() {
		return file_name;
	}
	public void setFile_name(List<String> file_name) {
		this.file_name = file_name;
	}
	
	
	
}
