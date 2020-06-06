package cn.henu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.henu.pojo.Answer;
import cn.henu.pojo.Exam;
import cn.henu.pojo.Student;

public interface ExamMapper {
	@Insert("insert into exam value(#{e_name},#{e_starttime},#{e_teacher},#{e_autostart},#{e_isend},#{e_file},#{e_clear},#{e_isstart},#{e_examination})")
	int insExam(Exam exam);

	@Select("select * from exam")
	List<Exam> selAllExam();

	@Update("update exam set e_name=#{param2.e_name},e_starttime=#{param2.e_starttime},e_autostart=#{param2.e_autostart} where e_name=#{param1}")
	int updExam(String e_oldname, Exam exam);
	
	@Update("update exam set e_isend=1 where e_name=#{e_name}")
	int updStopExam(String e_name);
	
	@Update("update exam set e_clear=1 where e_name=#{e_name}")
	int updClearExam(String e_name);

	@Delete("delete from student where stu_exam=#{e_name}")
	int delClearExam(String e_name);

	@Select("select * from exam where e_isend=0 and e_isstart=1")
	List<Exam> selStartExam();

	@Update("update exam set e_examination=#{param2} where e_name=#{param1}")
	int savePager(String e_name,String originalFileName);

	@Update("update exam set e_isstart=1 where e_name=#{param1}")
	int updIsStart(String e_name);

	@Select("select * from student where stu_exam=#{param1}")
	List<Student> selStartExamStudent(String e_name);

	@Insert("insert into answer(stu_id,exam_name,file_name,file_size,times) value(#{stu_id},#{exam_name},#{file_name},#{file_size},#{times})")
	int saveAnswer(Answer answer);

	@Select("select e_examination from exam where e_name=#{param1}")
	String selFileByName(String e_name);

	@Select("update student set stu_submit=1 where stu_id=#{param1} and stu_exam=#{param2}")
	String upSubmitStudent(String stu_id, String stu_exam);



}
