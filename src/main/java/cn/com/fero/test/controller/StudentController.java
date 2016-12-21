package cn.com.fero.test.controller;

import cn.com.fero.framework.web.controller.BaseController;
import cn.com.fero.framework.web.response.JsonResponse;
import cn.com.fero.test.service.StudentService;
import cn.com.fero.test.vo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by niexiaowei on 2016/12/20
 */
@RestController
public class StudentController extends BaseController {

    @Autowired
    StudentService studentService;
    @RequestMapping(value = "/v1/students", method = RequestMethod.POST)
    public JsonResponse add(@RequestBody Student student) {
        studentService.add(student);
        return new JsonResponse("添加成功");
    }
}
