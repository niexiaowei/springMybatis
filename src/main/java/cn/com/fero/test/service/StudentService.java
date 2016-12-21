package cn.com.fero.test.service;

import cn.com.fero.test.dao.StudentDao;
import cn.com.fero.test.vo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by niexiaowei on 2016/12/20
 */
@Service
public class StudentService {
    @Autowired
    private StudentDao studentDao;

    public void add(Student student) {
        studentDao.add(student);
    }
}
