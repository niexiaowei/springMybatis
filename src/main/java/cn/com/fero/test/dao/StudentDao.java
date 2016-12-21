package cn.com.fero.test.dao;

import cn.com.fero.test.vo.Student;

/**
 * Created by niexiaowei on 2016/12/20
 */
public interface StudentDao {
    void add(Student student);

    Student getById(Integer id);
}
