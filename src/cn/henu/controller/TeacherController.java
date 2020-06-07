package cn.henu.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.henu.pojo.Answer;
import cn.henu.util.ZipHelper;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.henu.pojo.Exam;
import cn.henu.pojo.Teacher;
import cn.henu.service.ExamService;
import cn.henu.service.TeacherService;
import cn.henu.util.MD5;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TeacherController {
    @Resource
    private TeacherService teacherServiceImpl;
    @Resource
    private ExamService examServiceImpl;

    @RequestMapping("goStudentInfo")
    public String goStudentInfo(int id, HttpSession session) {
        Object obj = session.getAttribute("examStartName");
        String exam = null;
        if (obj == null) {
            List<Exam> exams = examServiceImpl.selStartExam();
            if (exams.size() > 1) {
                session.setAttribute("info", "<script>alert('当前存在多场考试，请联系管理员');</script>");
                System.out.println("当前存在多场考试，请联系管理员");
                return "redirect:/teacher/teacher_about.jsp";
            } else if (exams.size() < 1) {
                session.setAttribute("info", "<script>alert('当前不存在考试');</script>");
                System.out.println("当前不存在考试");
                return "redirect:/teacher/teacher_about.jsp";
            } else {
                exam = exams.get(0).getE_name();
            }
        } else {
            exam = (String) obj;
            session.setAttribute("examStartName", exam);
        }
        if (id == 1) {
            return "redirect:/teacher/teacher_exam_survey.jsp";
        } else if (id == 2) {
            return "redirect:/teacher/teacher_studentinfo.jsp";
        } else if (id == 3) {
            return "redirect:/teacher/teacher_unlocked.jsp";
        } else if (id == 4) {
            return "redirect:/teacher/teacher_notice.jsp";
        }
        return "redirect:/teacher/teacher_about.jsp";

    }

    @RequestMapping("goEditTeacher")
    public String goEditTeacher(Teacher teacher, HttpSession session) {
        session.setAttribute("teacherinfo", teacher);
        return "redirect:/admin/admin_editTeacher.jsp";
    }

    @RequestMapping("editTeacherInfo")
    public String editTeacherInfo(Teacher teacher, HttpSession session) {
        int index = teacherServiceImpl.updTeacher(teacher);
        if (teacher.getT_pwd() != null && !teacher.getT_pwd().equals("")) {
            teacher.setT_pwd(MD5.getMD5String(teacher.getT_pwd()));
            teacherServiceImpl.updPwd(teacher);
        }
        if (index >= 1) {
            System.out.println("修改老师信息成功");
        } else {
            System.out.println("修改老师信息失败");
        }
        return "redirect:/admin/admin_manageteacher.jsp";

    }

    @RequestMapping("deleteTeacher")
    @ResponseBody
    public void deleteTeacher(String name, HttpSession session) {
        int index = teacherServiceImpl.delTeacher(name);
        if (index >= 1) {
            System.out.println("删除老师成功");
        } else {
            System.out.println("删除老师失败");
        }
    }

    @RequestMapping("exitTeacher")
    public String updPwd(String oldPwd, String newPwd1, String newPwd2, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        oldPwd = MD5.getMD5String(oldPwd);
        String newPwd = MD5.getMD5String(newPwd1);
        if (!oldPwd.equals(teacher.getT_pwd())) {
            session.setAttribute("info", "<script>alert('您输入的密码不正确');</script>");
            if (teacher.getT_manager() == 1) {
                return "redirect:/admin/admin_about.jsp";
            } else {
                return "redirect:/teacher/teacher_about.jsp";
            }
        }
        if (!newPwd1.equals(newPwd2)) {
            session.setAttribute("info", "<script>alert('您两次输入的密码不一致');</script>");
            if (teacher.getT_manager() == 1) {
                return "redirect:/admin/admin_about.jsp";
            } else {
                return "redirect:/teacher/teacher_about.jsp";
            }
        }
        teacher.setT_pwd(newPwd);
        int index = teacherServiceImpl.updPwd(teacher);
        if (index >= 1) {
            System.out.println("密码修改成功");
            session.setAttribute("info", "<script>alert('密码修改成功');</script>");
            session.setAttribute("teachers", teacher);
        } else {
            session.setAttribute("info", "<script>alert('出现未知错误');</script>");
            System.out.println("密码修改失败");
        }
        if (teacher.getT_manager() == 1) {
            return "redirect:/admin/admin_about.jsp";
        } else {
            return "redirect:/teacher/teacher_about.jsp";
        }
    }

    @RequestMapping("getAllTeacher")
    @ResponseBody
    public List<Teacher> getAllTeacher(Teacher teacher, HttpSession session) {
        return teacherServiceImpl.getAllTeacher();
    }

    @RequestMapping("addTeacher")
    @ResponseBody
    public void addTeacher(Teacher teacher, HttpSession session) {
        teacher.setT_pwd(MD5.getMD5String(teacher.getT_pwd()));
        int index = teacherServiceImpl.addTeacher(teacher);
        if (index >= 1) {
            System.out.println("添加老师成功");
        } else {
            System.out.println("添加老师失败");
        }
    }

    @RequestMapping("login/teacher")
    public String login(Teacher teacher, HttpSession session) {
        teacher.setT_pwd(MD5.getMD5String(teacher.getT_pwd()));
        Teacher tea = teacherServiceImpl.login(teacher);
        System.out.println(tea);
        if (tea != null) {
            session.setAttribute("teacher", tea);
            session.setAttribute("teachers", teacherServiceImpl.getAllTeacher());
            if (tea.getT_manager() == 1) {
                return "redirect:/admin/admin_about.jsp";
            }
            if (tea.getT_manager() == 0) {
                return "redirect:/teacher/teacher_about.jsp";
            }
            System.out.println("登录出错");
        }
        int count_manager = teacherServiceImpl.getManagerCount();
        if (count_manager <= 0 && teacher.getT_manager() == 1 && teacher.getT_username().equals("admin")
                && teacher.getT_pwd().equals(MD5.getMD5String("admin"))) {
            Teacher tea_admin = new Teacher();
            tea_admin.setT_manager(1);
            tea_admin.setT_name("默认管理员");
            tea_admin.setT_pwd("admin");
            tea_admin.setT_username("admin");
            session.setAttribute("teacher", tea_admin);
            session.setAttribute("teachers", teacherServiceImpl.getAllTeacher());
            System.out.println(tea_admin);
            return "redirect:/admin/admin_about.jsp";
        }
        return "redirect:/login.jsp";
    }

    @RequestMapping("teacherInformation")
    public String addNotice(HttpServletRequest request, HttpSession session) {
        String info = request.getParameter("information");
        session.setAttribute("notice", info);
        return "redirect:/login.jsp";
    }

    @RequestMapping("teacherDownload")
    public String teacherDownload(String e_name, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
        String savePath = req.getServletContext().getRealPath("files/" + e_name);
        String zipFilePath = req.getServletContext().getRealPath("files/papers");
        System.out.println("---试卷压缩包的保存路径为：" + savePath);
        boolean flag = ZipHelper.toZip(savePath, zipFilePath, e_name);
        if(flag) {
            System.out.println("压缩成功!");
        } else {
            System.out.println("压缩失败!");
        }
        //浏览器下载zip文件
        try{
            ServletOutputStream os = res.getOutputStream();
            File file = new File(req.getServletContext().getRealPath("files/papers"), e_name+".zip");
            byte[] bytes = FileUtils.readFileToByteArray(file);
            os.write(bytes);
            os.flush();
            os.close();
        }catch (Exception e){
            e.getMessage();
            session.setAttribute("downloadTip", "<script>alert('当前没有已提交试卷，无法下载！');</script>");
        }
        return "redirect:/teacher/teacher_exam_after.jsp";
    }
}

