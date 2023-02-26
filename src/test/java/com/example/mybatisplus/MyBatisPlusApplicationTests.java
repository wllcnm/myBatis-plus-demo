package com.example.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mybatisplus.mapper.UserMapper;
import com.example.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
class MyBatisPlusApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }


    @Test
    void testSelect() {
        //查询tb_user记录
        List<User> list = userMapper.selectList(null);
        System.out.println(list);
    }

    /*根据id查找结果*/
    @Test
    void findById() {
        User user = userMapper.findById(1L);
        System.out.println(user);
    }

    /*
     * 测试插入*/
    @Test
    void testInsert() {
        User user = new User();
        user.setAge(20);
        user.setName("speed");
        user.setUserName("cxk");
        user.setPassword("123456");
        user.setBirthday(LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        int result = userMapper.insert(user);
        System.out.println(result);
    }

    /*
     * 测试更新数据
     * */
    @Test
    void testUpdate() {
        User user = new User();
        user.setId(1L);
        user.setAge(99);
        user.setBirthday(null);
        //设置条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "蔡徐坤");
        //查询
        int result = userMapper.update(user, queryWrapper);
        System.out.println(result);
    }

    //使用updateWrapper可以将指定字段更新为空
    @Test
    void testUpdate2() {
        User user = new User();
        user.setId(1L);
        user.setAge(99);
        user.setBirthday(null);
        //设置条件
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "蔡徐坤").set("birthday", null);
        //查询
        int result = userMapper.update(user, updateWrapper);
        System.out.println(result);
    }

    @Test
    void testDelete() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("99", 20).eq("name", "蔡徐坤");
        userMapper.delete(queryWrapper);
    }

    //批量删除,关键api userMapper.deleteBatchIds
    @Test
    void testDelete2() {
        List<Long> ids = Arrays.asList(1L, 3L, 4L, 5L);
        userMapper.deleteBatchIds(ids);
    }


    //测试查询
    @Test
    void testSearch() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "speed");
        //查询记录条数
        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println(count);
        //查询单条记录,只能查出一条,否则报错
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
        //查询多条记录
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);

    }

    @Test
    void testPagePlugin() {
        //构造page对象,传入的是当前页码和要查询多少条
        Page<User> page = new Page<>(1, 2);
        //构造查询条件
        String name = "caocao";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(name != null && !name.equals(""), "name", "蔡徐坤");
        queryWrapper.gt("age", 90);
        IPage<User> userIPage = userMapper.selectPage(page, queryWrapper);

        //获取结果条数
        long pageCount = userIPage.getPages();
        long total = userIPage.getTotal();
        System.out.println(pageCount + " " + total);
        //获取结果集
        List<User> user = userIPage.getRecords();
        System.out.println(user);
    }

    @Test
    void testLike() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(User::getName, "蔡");
        queryWrapper.select(User::getId, User::getName);
        queryWrapper.orderByDesc(User::getAge);
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);

    }


}
