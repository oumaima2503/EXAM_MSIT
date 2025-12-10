package ma.xproce.mediaserver.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ma.xproce.mediaappgrpc.proto.UploadVideoRequest;
import ma.xproce.mediaappgrpc.proto.Video;
import ma.xproce.mediaappgrpc.proto.VideoIdRequest;
import ma.xproce.mediaappgrpc.proto.VideoServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
public class VideoService extends VideoServiceGrpc.VideoServiceImplBase {

	// stockage en mémoire id -> Video (proto)
	private final Map<String, Video> videos = new ConcurrentHashMap<>();

	// réutiliser le CreatorService pour enregistrer creators / lister les vidéos par creator
	private final CreatorService creatorService;

	@Autowired
	public VideoService(CreatorService creatorService) {
		this.creatorService = creatorService;
	}

	@Override
	public void uploadVideo(UploadVideoRequest request, StreamObserver<Video> responseObserver) {
		try {
			if (request == null) {
				responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Request is null").asRuntimeException());
				return;
			}

			String id = UUID.randomUUID().toString();
			Video.Builder vb = Video.newBuilder()
					.setId(id)
					.setTitle(request.getTitle() == null ? "" : request.getTitle())
					.setDescription(request.getDescription() == null ? "" : request.getDescription())
					.setUrl(request.getUrl() == null ? "" : request.getUrl())
					.setDurationSeconds(request.getDurationSeconds());

			if (request.hasCreator()) {
				// Normaliser / compléter le creator :
				var reqCreator = request.getCreator();
				ma.xproce.mediaappgrpc.proto.Creator finalCreator = reqCreator;

				// Si aucun id dans la requête -> créer un nouveau creator et utiliser celui-ci
				if (reqCreator.getId() == null || reqCreator.getId().isEmpty()) {
					finalCreator = creatorService.createCreator(reqCreator.getName(), reqCreator.getEmail());
				} else {
					// si id fourni, essayer de récupérer les infos complètes existantes
					ma.xproce.mediaappgrpc.proto.Creator existing = creatorService.findCreator(reqCreator.getId());
					if (existing != null) {
						// si la requête apporte des informations non vides, remplacer/mettre à jour l'enregistrement
						if ((!reqCreator.getName().isEmpty() && !reqCreator.getName().equals(existing.getName()))
								|| (!reqCreator.getEmail().isEmpty() && !reqCreator.getEmail().equals(existing.getEmail()))) {
							creatorService.registerCreator(reqCreator);
							finalCreator = reqCreator;
						} else {
							finalCreator = existing;
						}
					} else {
						// pas d'existant => enregistrer la valeur fournie (même si incomplète)
						creatorService.registerCreator(reqCreator);
						finalCreator = reqCreator;
					}
				}
				vb.setCreator(finalCreator);
			}

			Video video = vb.build();

			// stocker en mémoire
			videos.put(id, video);

			// informer le CreatorService pour maintien des index vidéos par creator
			if (video.hasCreator()) {
				creatorService.addVideoForCreator(video);
			}

			responseObserver.onNext(video);
			responseObserver.onCompleted();
		} catch (Exception ex) {
			responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).withCause(ex).asRuntimeException());
		}
	}

	@Override
	public void getVideo(VideoIdRequest request, StreamObserver<Video> responseObserver) {
		try {
			if (request == null || request.getId() == null || request.getId().isEmpty()) {
				responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("id is required").asRuntimeException());
				return;
			}
			Video v = videos.get(request.getId());
			if (v == null) {
				responseObserver.onError(Status.NOT_FOUND.withDescription("Video not found for id: " + request.getId()).asRuntimeException());
				return;
			}
			responseObserver.onNext(v);
			responseObserver.onCompleted();
		} catch (Exception ex) {
			responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).withCause(ex).asRuntimeException());
		}
	}
}