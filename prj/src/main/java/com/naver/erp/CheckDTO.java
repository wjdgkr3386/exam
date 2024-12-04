package com.naver.erp;

import java.util.List;

public class CheckDTO {

	private String exam_code;
	private String exam_title;
	
	private List<String> id;
	private List<String> problem_no;
	private List<String> problem_content;
	private List<String> problem_answer;
	
	private int problem_no_search;	//문제 번호 검색

	
	
	
	
	
	public String getExam_code() {
		return exam_code;
	}

	public void setExam_code(String exam_code) {
		this.exam_code = exam_code;
	}

	public String getExam_title() {
		return exam_title;
	}

	public void setExam_title(String exam_title) {
		this.exam_title = exam_title;
	}

	public List<String> getId() {
		return id;
	}

	public void setId(List<String> id) {
		this.id = id;
	}

	public List<String> getProblem_no() {
		return problem_no;
	}

	public void setProblem_no(List<String> problem_no) {
		this.problem_no = problem_no;
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

	public int getProblem_no_search() {
		return problem_no_search;
	}

	public void setProblem_no_search(int problem_no_search) {
		this.problem_no_search = problem_no_search;
	}

	
	
	
}
