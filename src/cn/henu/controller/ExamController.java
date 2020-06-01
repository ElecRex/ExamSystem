package cn.henu.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.henu.pojo.Answer;
import cn.henu.service.StudentService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.henu.pojo.Exam;
import cn.henu.pojo.Student;
import cn.henu.pojo.Teacher;
import cn.henu.service.ExamService;

@Controller
public class ExamController {
    @Resource
    private ExamService examServiceImpl;
    @Resource
    private StudentService studentService;
    @Resource
    private ExamService examService;

    @RequestMapping("examStart")
    public String examStart(HttpSession session) {
        int index = examServiceImpl.selStartExam().size();
        System.out.println(index);
        if (index >= 1) {
            session.setAttribute("info", "<script>alert('当前存在已开启的考试');</script>");
        } else {
            int index1 = examServiceImpl.examStart(((Exam) session.getAttribute("exam")).getE_name());
            if (index1 >= 1) {
                session.setAttribute("info", "<script>alert('考试开启成功');</script>");
            } else {
                session.setAttribute("info", "<script>alert('考试开启成功');</script>");
            }
        }
        return "redirect:/teacher/teacher_exam_before.jsp";
    }

    @RequestMapping("saveAnswer")
    public String saveAnswer(MultipartFile paper, HttpServletRequest req, HttpSession session) {
        boolean index = examServiceImpl.saveAnswer(paper, req, session);
        if (index) {
            session.setAttribute("info", "<script>alert('文件上传成功');</script>");
        } else {
            session.setAttribute("info", "<script>alert('文件上传失败');</script>");
        }
        //-------------------------------------------------------
        List<Exam> exams = examServiceImpl.selStartExam();
        getSubmits(exams.get(0).getE_name()); //统计已经提交试卷人数，若提交stu_submit=1
        //--------------------------------------------------------
        return "redirect:/student/student_about.jsp";
    }

    @RequestMapping("examDownload")
    public void examDownload(String e_name, HttpServletResponse res, HttpServletRequest req) throws IOException {
        String fileName = examServiceImpl.selFileByName(e_name);
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream os = res.getOutputStream();

        File file = new File(req.getServletContext().getRealPath("files"), fileName);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        os.write(bytes);
        os.flush();
        os.close();
    }

    @RequestMapping("savePaper")
    public String savePaper(MultipartFile paper, HttpServletRequest req, HttpSession session) {
        boolean index = examServiceImpl.savePager(paper, req, session);
        if (index) {
            session.setAttribute("info", "<script>alert('文件上传成功');</script>");
        } else {
            session.setAttribute("info", "<script>alert('文件上传失败');</script>");
        }
        return "redirect:/teacher/teacher_addexam.jsp";
    }

    @RequestMapping("getStartExamInfo")
    @ResponseBody
    public List<Student> getStartExamInfo() {
        List<Exam> exams = examServiceImpl.selStartExam();
        if (exams.size() > 1) {
            System.out.println("当前存在多场考试，请联系管理员");
        } else if (exams.size() == 1) {
            return examServiceImpl.selStartExamStudent(exams.get(0).getE_name());
        } else {
            System.out.println("当前不存在考试");
        }
        return null;
    }

    @RequestMapping("getStartExam")
    @ResponseBody
    public boolean getStartExam() {
        int index = examServiceImpl.selStartExam().size();
        System.out.println(index);
        return index >= 1;
    }

    @RequestMapping("stopExam")
    @ResponseBody
    public void stopExam(String name, HttpSession session) {
        int index = examServiceImpl.updStopExam(name);
        if (index >= 1) {
            session.removeAttribute("examStartName");
            System.out.println("考试停止成功");
        } else {
            System.out.println("考试停止失败");
        }
    }

    @RequestMapping("clearExam")
    @ResponseBody
    public void clearExam(String name) {
        int index = examServiceImpl.updClearExam(name);
        if (index >= 1) {
            System.out.println("考试清理成功");
        } else {
            System.out.println("考试清理失败");
        }
    }

    @RequestMapping("getAllExams")
    @ResponseBody
    public List<Exam> getAllExam(HttpSession session) {
        return examServiceImpl.getAllExam();
    }

    @RequestMapping("addExam")
    @ResponseBody
    public void addExam(Exam exam, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        exam.setE_teacher(teacher.getT_name());
        int index = examServiceImpl.insExam(exam);
        if (index >= 1) {
            System.out.println("添加考试成功");
        } else {
            System.out.println("添加考试失败");
        }
    }

    @RequestMapping("goEditExam")
    public String goEditExam(Exam exam, HttpSession session) {
        session.setAttribute("exam", exam);
        return "redirect:/teacher/teacher_addexam.jsp";
    }

    @RequestMapping("editExam")
    public String editExam(String e_oldname, Exam exam, HttpSession session) {
        System.out.println(exam);
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Exam oldExam = (Exam) session.getAttribute("exam");
        exam.setE_teacher(teacher.getT_name());
        int index = examServiceImpl.updExam(e_oldname, exam);
        if (index >= 1) {
            System.out.println("修改考试成功");
        } else {
            System.out.println("修改考试失败");
        }
        oldExam.setE_name(exam.getE_name());
        oldExam.setE_starttime(exam.getE_starttime());
        oldExam.setE_autostart(exam.getE_autostart());
        oldExam.setE_teacher(teacher.getT_name());
        System.out.println(oldExam);
        session.setAttribute("exam", oldExam);
        return "redirect:/teacher/teacher_addexam.jsp";
    }

    /**
     * 查询参加某一考试的学生是否提交试卷
     */
    public void getSubmits(String examName) {
        List<Student> students = studentService.selSubmitStudent(examName);
        Answer answer;
        List<Answer> answers = new ArrayList<>();
        for(Student s: students){
            try{
                answer = studentService.selAllInfo(s.getStu_id(), examName).get(0);
                answers.add(answer);
            }catch (Exception e){}

        }
        for(Student s: students){
            for(Answer a: answers){
                if(a.getStu_id().equals(s.getStu_id())){
                    String code = examService.upSubmitStudent(s.getStu_id(), a.getExam_name());
                }
            }
        }

    }

}
