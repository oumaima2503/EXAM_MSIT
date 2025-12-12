package ma.xproce.mediaserver.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ma.xproce.mediaappgrpc.proto.UploadVideoRequest;
import ma.xproce.mediaappgrpc.proto.Video;
import ma.xproce.mediaappgrpc.proto.VideoIdRequest;
import ma.xproce.mediaappgrpc.proto.VideoServiceGrpc;
import ma.xproce.mediaserver.mapper.VideoMapper;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
public class VideoService extends VideoServiceGrpc.VideoServiceImplBase {

	// stockage Video entity
	private final Map<String, ma.xproce.mediaserver.dao.entities.Video> videoEntities = new ConcurrentHashMap<>();

	private final CreatorService creatorService;
	private final VideoMapper videoMapper;

	public VideoService(CreatorService creatorService, VideoMapper videoMapper) {
		this.creatorService = creatorService;
		this.videoMapper = videoMapper;
	}

	@Override
	public void uploadVideo(UploadVideoRequest request, StreamObserver<Video> responseObserver) {
		try {
			if (request == null) {
				responseObserver.onError(Status.INVALID_ARGUMENT
						.withDescription("Request is null")
						.asRuntimeException());
				return;
			}

			// Créer l'entity Video
			ma.xproce.mediaserver.dao.entities.Video videoEntity = new ma.xproce.mediaserver.dao.entities.Video();
			String videoId = UUID.randomUUID().toString();
			videoEntity.setId(videoId);
			videoEntity.setTitle(request.getTitle() == null ? "" : request.getTitle());
			videoEntity.setDescription(request.getDescription() == null ? "" : request.getDescription());
			videoEntity.setUrl(request.getUrl() == null ? "" : request.getUrl());
			videoEntity.setDurationSeconds(request.getDurationSeconds());

			// Gestion du creator
			if (request.hasCreator()) {
				ma.xproce.mediaappgrpc.proto.Creator reqCreator = request.getCreator();
				ma.xproce.mediaserver.dao.entities.Creator creatorEntity = null;

				// Si aucun id dans la requête, creer un nouveau creator
				if (reqCreator.getId() == null || reqCreator.getId().isEmpty()) {
					// Créer le creator via le service
					ma.xproce.mediaappgrpc.proto.Creator newCreatorProto = creatorService.createCreator(
							reqCreator.getName(),
							reqCreator.getEmail());
					creatorEntity = creatorService.findCreatorEntity(newCreatorProto.getId());
				} else {
					// Récupérer le creator existant
					creatorEntity = creatorService.findCreatorEntity(reqCreator.getId());

					if (creatorEntity != null) {
						// Vérifier si les informations doivent être mises à jour
						if ((!reqCreator.getName().isEmpty() && !reqCreator.getName().equals(creatorEntity.getName()))
								|| (!reqCreator.getEmail().isEmpty() && !reqCreator.getEmail().equals(creatorEntity.getEmail()))) {
							// Mettre à jour le creator
							ma.xproce.mediaappgrpc.proto.Creator updatedCreatorProto = ma.xproce.mediaappgrpc.proto.Creator.newBuilder()
									.setId(reqCreator.getId())
									.setName(reqCreator.getName())
									.setEmail(reqCreator.getEmail())
									.build();
							creatorService.registerCreator(updatedCreatorProto);
							creatorEntity = creatorService.findCreatorEntity(reqCreator.getId());
						}
					} else {
						// Créer un nouveau creator avec ID fourni
						creatorService.registerCreator(reqCreator);
						creatorEntity = creatorService.findCreatorEntity(reqCreator.getId());
					}
				}

				videoEntity.setCreator(creatorEntity);
			}

			// Sauvegarder l'entity Video
			videoEntities.put(videoId, videoEntity);

			// Ajouter la video au creator
			creatorService.addVideoForCreator(videoEntity);

			// Convertir en proto pour la reponse
			Video videoProto = videoMapper.toProto(videoEntity);
			responseObserver.onNext(videoProto);
			responseObserver.onCompleted();

		} catch (Exception ex) {
			responseObserver.onError(Status.INTERNAL
					.withDescription(ex.getMessage())
					.withCause(ex)
					.asRuntimeException());
		}
	}

	@Override
	public void getVideo(VideoIdRequest request, StreamObserver<Video> responseObserver) {
		try {
			if (request == null || request.getId() == null || request.getId().isEmpty()) {
				responseObserver.onError(Status.INVALID_ARGUMENT
						.withDescription("id is required")
						.asRuntimeException());
				return;
			}

			ma.xproce.mediaserver.dao.entities.Video videoEntity = videoEntities.get(request.getId());
			if (videoEntity == null) {
				responseObserver.onError(Status.NOT_FOUND
						.withDescription("Video not found for id: " + request.getId())
						.asRuntimeException());
				return;
			}

			// Convertir entity to proto
			Video videoProto = videoMapper.toProto(videoEntity);
			responseObserver.onNext(videoProto);
			responseObserver.onCompleted();

		} catch (Exception ex) {
			responseObserver.onError(Status.INTERNAL
					.withDescription(ex.getMessage())
					.withCause(ex)
					.asRuntimeException());
		}
	}
}