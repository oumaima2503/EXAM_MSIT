package ma.xproce.mediaclient.service;

import ma.xproce.mediaappgrpc.proto.Video;
import ma.xproce.mediaappgrpc.proto.VideoIdRequest;
import ma.xproce.mediaappgrpc.proto.VideoServiceGrpc;
import ma.xproce.mediaclient.dto.UploadVideoRequestDto;
import ma.xproce.mediaclient.dto.VideoDto;
import ma.xproce.mediaclient.mapper.VideoMapper;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceClient {
    @GrpcClient("mediaserver")
    VideoServiceGrpc.VideoServiceBlockingStub stub;
    @Autowired
    private VideoMapper mapper;

    // Accept UploadVideoRequestDto and return VideoDto
    public VideoDto uploadVideo(UploadVideoRequestDto requestDto) {
        // convert DTO to proto and call gRPC
        ma.xproce.mediaappgrpc.proto.UploadVideoRequest protoReq = mapper.fromUploadDtoToProto(requestDto);
        Video video = stub.uploadVideo(protoReq);
        return mapper.fromVideoProtoToVideoDto(video);
    }

    // Get video by id
    public VideoDto getVideo(String id) {
        VideoIdRequest req = VideoIdRequest.newBuilder().setId(id == null ? "" : id).build();
        Video video = stub.getVideo(req);
        return mapper.fromVideoProtoToVideoDto(video);
    }
}