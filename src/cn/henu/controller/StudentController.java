package cn.henu.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.henu.pojo.Answer;
import cn.henu.pojo.Student;
import cn.henu.service.ExamService;
import cn.henu.service.StudentService;

@Controller
public class StudentController {
    @Resource
    private StudentService studentServiceImpl;
    @Resource
    private ExamService examServiceImpl;

    @RequestMapping("login/student")
    public String login(Student student, HttpServletRequest request, HttpSession session) {
        Student stu = studentServiceImpl.login(student);

        if (stu != null) { //获取并绑定学生ip地址
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0
                        || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0
                        || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0
                        || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ip == null || ip.length() == 0
                        || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (ip == null || ip.length() == 0
                        || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = (String) ips[index];
                    if (!("unknown".equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
            if (ip.equals("127.0.0.1")) {
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inetAddress.getHostAddress();
            }
            if (stu.getStu_ip() == null || stu.getStu_ip().equals("")) {
                stu.setStu_ip(ip);
                int index = studentServiceImpl.updStuIp(stu);
                if (index < 1) {
                    session.setAttribute("info", "<script>alert('ip地址获取有误');</script>");
                    return "redirect:/login.jsp";
                }
            } else if (!stu.getStu_ip().equals(ip)) {
                session.setAttribute("info", "<script>alert('您的ip地址已被绑定，请使用绑定的ip登录，或联系管理员');</script>");
                return "redirect:/login.jsp";
            }

            session.setAttribute("student", stu);
            return "redirect:/student/student_about.jsp";
        } else {
            return "redirect:/login.jsp";
        }
    }

    @RequestMapping(value = "saveExcel", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    public String saveExcel(MultipartFile file, String e_name, HttpServletRequest req, HttpSession session) {
        int index = studentServiceImpl.saveExcel(file, e_name, req, session);
        if (index == 0) {
            session.setAttribute("info", "<script>alert('文件上传成功');</script>");
        } else {
            String info = "";
            if (index == 1) {
                info = "该文件已存在服务器，换个名字试试！";
            } else if (index == 2) {
                info = "出现未知错误！";
            } else {
                info = "名单存在重复学生信息！";
            }
            session.setAttribute("info", "<script>alert('文件上传失败" + info + "');</script>");
        }
        System.out.println(e_name);
        return "redirect:/teacher/teacher_continueadd.jsp?e_name=" + e_name;
    }

    @RequestMapping("addStudent")
    @ResponseBody
    public void addStudent(Student student, HttpSession session) {
        if (student.getStu_exam() == null) {
            student.setStu_exam((String) session.getAttribute("examStartName"));
        }
        int index = studentServiceImpl.insStudent(student);
        if (index >= 1) {
            session.setAttribute("info", "<script>alert('添加成功');</script>");
        } else {
            session.setAttribute("info", "<script>alert('添加失败');</script>");
        }
    }

    @RequestMapping("getAllInfo")
    @ResponseBody
    public List<Answer> getAllInfo(HttpSession session) {
        Object stu = session.getAttribute("student");
        String stu_id = "", exam_name = "";
        if (stu != null) {
            Student student = (Student) stu;
            stu_id = student.getStu_id();
            exam_name = student.getStu_exam();
        } else {
            System.out.println("student == null!!!");
            return null;
        }
        return studentServiceImpl.selAllInfo(stu_id, exam_name);
    }

    @RequestMapping("getExamStudents")
    @ResponseBody
    public List<Student> getExamStudents(String e_name) {
        return studentServiceImpl.selAllStudents(e_name);
    }

    @RequestMapping("unbind")
    @ResponseBody
    public void unbind(String stu_id) {
        int index = studentServiceImpl.updUnBind(stu_id);
        if (index >= 1) {
            System.out.println("IP解绑成功！");
        } else {
            System.out.println("IP解绑失败！");
        }
    }

    @RequestMapping("selStudentById")
    @ResponseBody
    public List<Student> selStudentById(String stu_id, HttpSession session) {
        Object obj = session.getAttribute("examStartName");
        if (obj != null) {
            return studentServiceImpl.selStudentById(stu_id, obj.toString());
        }
        String e_name = examServiceImpl.selStartExam().get(0).getE_name();
        session.setAttribute("examStartName", e_name);
        return studentServiceImpl.selStudentById(stu_id, e_name);
    }

    @RequestMapping("exit")
    public String exit(HttpSession session) {
        session.removeAttribute("student");
        session.removeAttribute("teacher");
        return "redirect:/login.jsp";
    }
}
