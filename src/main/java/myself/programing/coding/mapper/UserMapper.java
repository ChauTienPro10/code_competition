package myself.programing.coding.mapper;

import myself.programing.coding.dto.UserDto;
import myself.programing.coding.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "account.username", target = "username")
    UserDto toDto(User user);
}
