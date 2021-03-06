package com.baomidou.mybatisplus.samples.enums;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.samples.enums.entity.User;
import com.baomidou.mybatisplus.samples.enums.enums.AgeEnum;
import com.baomidou.mybatisplus.samples.enums.enums.GenderEnum;
import com.baomidou.mybatisplus.samples.enums.enums.GradeEnum;
import com.baomidou.mybatisplus.samples.enums.enums.UserState;
import com.baomidou.mybatisplus.samples.enums.mapper.UserMapper;

/**
 * <p>
 * 内置 Enums 演示
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Resource
    private UserMapper mapper;

    @Test
    public void insert() {
        User user = new User();
        user.setName("K神");
        user.setAge(AgeEnum.ONE);
        user.setGrade(GradeEnum.HIGH);
        user.setGender(GenderEnum.MALE);
        user.setEmail("abc@mp.com");
        Assert.assertTrue(mapper.insert(user) > 0);
        // 成功直接拿会写的 ID
        System.err.println("\n插入成功 ID 为：" + user.getId());

        List<User> list = mapper.selectList(null);
        for (User u : list) {
            System.out.println(u);
            Assert.assertNotNull("age should not be null", u.getAge());
            if (u.getId().equals(user.getId())) {
                Assert.assertNotNull("gender should not be null", u.getGender());
                Assert.assertNotNull("grade should not be null", u.getGrade());

            }
        }
    }

    @Test
    public void delete() {
        Assert.assertTrue(mapper.delete(new QueryWrapper<User>()
                .lambda().eq(User::getAge, AgeEnum.TWO)) > 0);
    }

    @Test
    public void update() {
        Assert.assertTrue(mapper.update(new User().setAge(AgeEnum.TWO),
                new QueryWrapper<User>().eq("age", AgeEnum.THREE)) > 0);
    }

    @Test
    public void select() {
        User user = mapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId, 2));
        Assert.assertEquals("Jack", user.getName());
        Assert.assertTrue(AgeEnum.THREE == user.getAge());

        //#1500 github: verified ok. Not a bug
        List<User> userList = mapper.selectList(new QueryWrapper<User>().lambda().eq(User::getUserState, UserState.ACTIVE));
        Assert.assertEquals(3, userList.size());
        Optional<User> userOptional = userList.stream()
                .filter(x -> x.getId() == 1)
                .findFirst();
        userOptional.ifPresent(user1 -> Assert.assertTrue(user1.getUserState() == UserState.ACTIVE));
    }
}
