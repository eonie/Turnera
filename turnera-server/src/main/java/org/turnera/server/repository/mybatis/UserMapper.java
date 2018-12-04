package org.turnera.server.repository.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.turnera.server.entity.User;

public interface UserMapper {
    @Select("select * from tur_user where id = #{id}")
    User getUserById(@Param("id") Long id);
}
