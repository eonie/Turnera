package org.turnera.server.repository.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;
import org.turnera.server.entity.User;

import java.util.List;

public interface UserMapper {
    @Select("select * from tur_user where id = #{id}")

    User getUserById(@Param("id") Long id);
    @Select("select * from tur_user limit #{page.offset}, #{page.pageSize} ")
    List<User> findUserList(@Param("user") User user, @Param("page") Pageable page);

    @Select("select count(*) from tur_user ")
    Long  findUserListTotal(@Param("user") User user);
}
