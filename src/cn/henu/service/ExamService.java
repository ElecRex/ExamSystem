package cn.henu.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.henu.pojo.Answer;
import org.springframework.web.multipart.MultipartFile;

import cn.henu.pojo.Exam;
import cn.henu.pojo.Student;

public interface ExamService {
	public int insExam(Exam exam);
	
	public List<Exam> getAllExam();

	public int updExam(String e_oldname, Exam exam);

	public int updStopExam(String name);

	public int updClearExam(String name);

	public int delClearExam(String e_name);

	public List<Exam> selStartExam();

	public boolean savePager(MultipartFile multipartFile,HttpServletRequest req,HttpSession session);
	
	public boolean saveAnswer(MultipartFile multipartFile,HttpServletRequest req,HttpSession session);

	public int examStart(String e_name);

	public List<Student> selStartExamStudent(String e_name);

	public String selFileByName(String e_name);

	public String upSubmitStudent(String stu_id, String stu_exam);

}
