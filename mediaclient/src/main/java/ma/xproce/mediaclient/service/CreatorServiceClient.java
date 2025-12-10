package ma.xproce.mediaclient.service;

import ma.xproce.mediaappgrpc.proto.Creator;
import ma.xproce.mediaappgrpc.proto.CreatorIdRequest;
import ma.xproce.mediaappgrpc.proto.CreatorServiceGrpc;
import ma.xproce.mediaappgrpc.proto.VideoStream;
import ma.xproce.mediaclient.dto.CreatorDto;
import ma.xproce.mediaclient.dto.VideoStreamDto;
import ma.xproce.mediaclient.mapper.VideoMapper;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreatorServiceClient {
	@GrpcClient("mediaserver")
	private CreatorServiceGrpc.CreatorServiceBlockingStub stub;

	@Autowired
	private VideoMapper mapper;

	public CreatorDto getCreator(String id) {
		Creator c = stub.getCreator(CreatorIdRequest.newBuilder().setId(id == null ? "" : id).build());
		if (c == null) return null;
		return mapper.fromCreatorProtoToDto(c);
	}

	public VideoStreamDto getCreatorVideos(String id) {
		VideoStream s = stub.getCreatorVideos(CreatorIdRequest.newBuilder().setId(id == null ? "" : id).build());
		return mapper.fromVideoStreamProtoToDto(s);
	}
}