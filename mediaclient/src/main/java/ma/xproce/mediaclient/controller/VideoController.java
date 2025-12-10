package ma.xproce.mediaclient.controller;

import ma.xproce.mediaappgrpc.proto.Creator;
import ma.xproce.mediaappgrpc.proto.UploadVideoRequest;
import ma.xproce.mediaclient.dto.VideoDto;
import ma.xproce.mediaclient.service.VideoServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/videos")
@RestController
public class VideoController {
    @Autowired
    private VideoServiceClient videoService;

    @PostMapping("addVideo")
    public VideoDto uploadVideo() {
        Creator creator = Creator.newBuilder()
                .setName("Xproce")
                .setEmail("hirchoua.badr@gmail.com")
                .setId("2")
                .build();
        UploadVideoRequest request = UploadVideoRequest.newBuilder()
                .setTitle("grpc 101")
                .setDescription("The gRPC 101 is an introductory course to master Grpc")
                .setUrl("https://github.com/badrhr/gRPC101")
                .setDurationSeconds(380)
                .setCreator(creator)
                .build();
        VideoDto videoDto = videoService.uploadVideo(request);
        System.out.println(videoDto);
        return videoDto;
    }

    @GetMapping("/{id}")
    public VideoDto getVideo(@PathVariable String id) {
        // Appelle le service client qui convertit gRPC Video -> VideoDto
        return videoService.getVideo(id);
    }


}


