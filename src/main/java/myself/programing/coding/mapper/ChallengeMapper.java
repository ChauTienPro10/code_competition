package myself.programing.coding.mapper;

import java.util.List;
import myself.programing.coding.dto.ChallengeDto;
import myself.programing.coding.entity.Challenge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    ChallengeDto toDto(Challenge challenge);

    List<ChallengeDto> toDtoList(List<Challenge> challenges);
}
