package xyz.frt.yiban.basesdk.dao.sys;

import org.apache.ibatis.annotations.Mapper;
import xyz.frt.yiban.basesdk.dao.BaseMapper;
import xyz.frt.yiban.basesdk.entity.sys.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}