package ma.xproce.mediaclient.controller;



import ma.xproce.mediaclient.dto.CreatorDto;
import ma.xproce.mediaclient.dto.VideoDto;
import ma.xproce.mediaclient.service.CreatorServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creators")
public class CreatorController {

    @Autowired
    private CreatorServiceClient creatorService;

    @GetMapping("/{id}")
    public CreatorDto getCreator(@PathVariable String id) {
        CreatorDto creatorDto = creatorService.getCreator(id);
        System.out.println(creatorDto);
        return creatorDto;
    }

    @GetMapping("/{id}/videos")
    public List<VideoDto> getCreatorVideos(@PathVariable String id) {
        List<VideoDto> videoDtos = (List<VideoDto>) creatorService.getCreatorVideos(id);
        System.out.println("Found " + videoDtos.size() + " videos for creator " + id);
        return videoDtos;
    }
}
