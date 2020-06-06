package cn.henu.service.impl;

import cn.henu.mapper.ExamMapper;
import cn.henu.mapper.StudentMapper;
import cn.henu.pojo.Answer;
import cn.henu.pojo.Exam;
import cn.henu.pojo.Student;
import cn.henu.service.StudentService;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ExamMapper examMapper;

    @Override
    public Student login(Student student) {
        Exam exam = examMapper.selStartExam().get(0);
        student.setStu_exam(exam.getE_name());
        return studentMapper.selByStudent(student);
    }

    public boolean importExcelToSQL(String fileUrl, String stu_exam) {
        try (FileInputStream inputStream = new FileInputStream(fileUrl)) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() < 1) {
                    continue;
                }
                Student student = new Student();

                row.getCell(0).setCellType(CellType.STRING);
                row.getCell(1).setCellType(CellType.STRING);
                row.getCell(2).setCellType(CellType.STRING);

                student.setStu_id(row.getCell(0).getStringCellValue());
                student.setStu_name(row.getCell(1).getStringCellValue());
                student.setStu_class(row.getCell(2).getStringCellValue());
                student.setStu_exam(stu_exam);
                System.out.println(student);
                studentMapper.insStudent(student);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Student> selAllStudents(String e_name) {
        return studentMapper.selAllStudents(e_name);
    }

    @Override
    public int insStudent(Student student) {
        return studentMapper.insStudent(student);
    }

    @Override
    public List<Student> selStudentById(String stu_id, String e_name) {
        return studentMapper.selStudentById(stu_id, e_name);
    }

    @Override
    public int saveExcel(MultipartFile multipartFile, String e_name, HttpServletRequest req, HttpSession session) {
        String originalFileName = multipartFile.getOriginalFilename();
        File targetFilePath = null;
        String path = req.getServletContext().getRealPath("files/studentExcel");
        targetFilePath = new File(path, originalFileName);
        if (!targetFilePath.exists()) {
            targetFilePath.getParentFile().mkdirs();
            try {
                if (targetFilePath.createNewFile()) {
                    System.out.println(targetFilePath);
                    multipartFile.transferTo(targetFilePath);
                    if (importExcelToSQL(path + "/" + originalFileName, e_name)) {
                        System.out.println("成功");
                        return 0;
                    } else {
                        return 3;
                    }
                }
            } catch (IOException e) {
                return 2;
            }
        }
        return 1;
    }

    @Override
    public int updUnBind(String stu_id) {
        return studentMapper.updUnBind(stu_id);
    }

    @Override
    public int updStuIp(Student stu) {
        return studentMapper.updStuIp(stu);
    }

    @Override
    public List<Answer> selAllInfo(String stu_id, String exam_name) {
        return studentMapper.selAllInfo(stu_id, exam_name);
    }

    @Override
    public List<Student> selSubmitStudent(String exam_name) {
        return studentMapper.selSubmitStudent(exam_name);
    }


}
