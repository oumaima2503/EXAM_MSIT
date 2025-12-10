package ma.xproce.mediaserver.mapper;


import ma.xproce.mediaserver.dao.entities.Creator;
import ma.xproce.mediaserver.dto.CreatorDto;
import ma.xproce.mediaserver.dto.CreatorNewDto;
import org.springframework.stereotype.Component;

@Component
public class CreatorMapper {

    public CreatorDto fromCreatorToCreatorDto(Creator creator) {
        CreatorDto creatorDto = new CreatorDto();
        creatorDto.setName(creator.getName());
        return creatorDto;
    }
    public Creator fromCretorDtoToCreator(CreatorDto creatorDto){
        Creator creator = new Creator();
        creator.setName(creatorDto.getName());
        return creator;
    }
    public Creator fromCreatorDtoNewToUser(CreatorNewDto creatorNewDto) {
        Creator creator = new Creator();
        creator.setName(creatorNewDto.getName());
        return creator;
    }


}
