package sheep.portal.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import sheep.portal.entity.Message;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {
    @Select("select * from Message where (this_user_id = #{this_user_id} and that_user_id = #{that_user_id}) or (that_user_id = #{this_user_id} and this_user_id = #{that_user_id}) ")
    List<Message> messageList(int this_user_id, int that_user_id);

    @Select("select distinct that_user_id from Message where this_user_id = #{this_user_id} ")
    List<Integer> peopleList1(int this_user_id);

    @Select("select distinct this_user_id from Message where that_user_id = #{this_user_id} ")
    List<Integer> peopleList2(int this_user_id);

    @Insert("insert into Message(this_user_id, that_user_id, message, time, status) values (#{this_user_id}, #{that_user_id}, #{message}, #{time}, #{status})")
    int sendMessage(Message message);

    @Update("update Message set status = 1 where this_user_id = #{that_user_id} and that_user_id = #{this_user_id}")
    int readMessage(int this_user_id, int that_user_id);

    @Delete("delete from Message where (this_user_id = #{this_user_id} and that_user_id = #{that_user_id}) or (this_user_id = #{that_user_id} and that_user_id = #{this_user_id})")
    int deleteDislog(int this_user_id, int that_user_id);
}
