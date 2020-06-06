package cn.henu.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.henu.mapper.ExamMapper;
import cn.henu.pojo.Answer;
import cn.henu.pojo.Exam;
import cn.henu.pojo.Student;
import cn.henu.service.ExamService;

@Service
public class ExamServiceImpl implements ExamService {
    @Resource
    private ExamMapper examMapper;

    @Override
    public int insExam(Exam exam) {
        return examMapper.insExam(exam);
    }

    @Override
    public List<Exam> getAllExam() {
        return examMapper.selAllExam();
    }

    @Override
    public int updExam(String e_oldname, Exam exam) {
        return examMapper.updExam(e_oldname, exam);
    }

    @Override
    public int updStopExam(String name) {
        return examMapper.updStopExam(name);
    }

    @Override
    public int updClearExam(String name) {
        return examMapper.updClearExam(name);
    }

    @Override
    public int delClearExam(String e_name) {
        return examMapper.delClearExam(e_name);
    }

    @Override
    public List<Exam> selStartExam() {
        return examMapper.selStartExam();
    }

    @Override
    public boolean saveAnswer(MultipartFile multipartFile, HttpServletRequest req, HttpSession session) {
        String originalFileName = multipartFile.getOriginalFilename();
        File targetFilePath = null;

        Object stu = session.getAttribute("student");
        Student stuTmp = (Student) stu;

        String path = req.getServletContext().getRealPath("files/" + stuTmp.getStu_exam());
        targetFilePath = new File(path, originalFileName);
        if (!targetFilePath.exists()) {
//            Object stu = session.getAttribute("student");
            String stu_id = "", exam_name = "";
            if (stu != null) {
                Student student = (Student) stu;
                stu_id = student.getStu_id();
                exam_name = student.getStu_exam();
            } else {
                return false;
            }
            targetFilePath.getParentFile().mkdirs();
            try {
                if (targetFilePath.createNewFile()) {
                    System.out.println(targetFilePath);
                    multipartFile.transferTo(targetFilePath);
                    Answer answer = new Answer();
                    answer.setStu_id(stu_id);
                    answer.setExam_name(exam_name);
                    answer.setFile_name(originalFileName);
                    answer.setFile_size("" + multipartFile.getSize());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    answer.setTimes(df.format(new Date()));
                    int index = examMapper.saveAnswer(answer);
                    return index >= 1;
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean savePager(MultipartFile multipartFile, HttpServletRequest req, HttpSession session) {
        String originalFileName = multipartFile.getOriginalFilename();
        File targetFilePath = null;
        String path = req.getServletContext().getRealPath("files/papers");
        targetFilePath = new File(path, originalFileName);
        if (!targetFilePath.exists()) {
            targetFilePath.getParentFile().mkdirs();
            try {
                if (targetFilePath.createNewFile()) {
                    System.out.println(targetFilePath);
                    multipartFile.transferTo(targetFilePath);
                    Exam exam = (Exam) session.getAttribute("exam");
                    examMapper.savePager(exam.getE_name(), originalFileName);
                    exam.setE_examination(originalFileName);
                    session.setAttribute("exam", exam);
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public int examStart(String e_name) {
        return examMapper.updIsStart(e_name);
    }

    @Override
    public List<Student> selStartExamStudent(String e_name) {
        return examMapper.selStartExamStudent(e_name);
    }

    @Override
    public String selFileByName(String e_name) {
        return examMapper.selFileByName(e_name);
    }

    @Override
    public String upSubmitStudent(String stu_id, String stu_exam) {
        return examMapper.upSubmitStudent(stu_id, stu_exam);
    }

}
