package ma.xproce.mediaserver.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ma.xproce.mediaappgrpc.proto.Creator;
import ma.xproce.mediaappgrpc.proto.CreatorIdRequest;
import ma.xproce.mediaappgrpc.proto.Video;
import ma.xproce.mediaappgrpc.proto.VideoStream;
import ma.xproce.mediaappgrpc.proto.CreatorServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@GrpcService
public class CreatorService extends CreatorServiceGrpc.CreatorServiceImplBase {

	// Stockage en mémoire thread-safe : id -> Creator (proto)
	private final Map<String, Creator> creators = new ConcurrentHashMap<>();

	// Stockage vidéos par creatorId
	private final Map<String, List<Video>> videosByCreator = new ConcurrentHashMap<>();

	// Méthode utilitaire pour créer un creator (peut être utilisée par d'autres services/tests)
	public Creator createCreator(String name, String email) {
		String id = UUID.randomUUID().toString();
		Creator c = Creator.newBuilder()
				.setId(id)
				.setName(name == null ? "" : name)
				.setEmail(email == null ? "" : email)
				.build();
		creators.put(id, c);
		videosByCreator.putIfAbsent(id, new CopyOnWriteArrayList<>());
		return c;
	}

	// Enregistrer explicitement un Creator proto (utile si VideoService reçoit un Creator dans la requête)
	public void registerCreator(Creator creator) {
		// si id absent, on crée un nouveau creator pour éviter perte d'infos
		if (creator == null) return;
		if (creator.getId() == null || creator.getId().isEmpty()) {
			createCreator(creator.getName(), creator.getEmail());
			return;
		}
		creators.put(creator.getId(), creator);
		videosByCreator.putIfAbsent(creator.getId(), new CopyOnWriteArrayList<>());
	}

	// Retourne un creator existant (ou null) — méthode synchrone utile pour VideoService
	public Creator findCreator(String id) {
		if (id == null || id.isEmpty()) return null;
		return creators.get(id);
	}

	// Ajouter une vidéo pour le creator (utilitaire)
	public void addVideoForCreator(Video video) {
		if (video == null || !video.hasCreator()) return;
		String creatorId = video.getCreator().getId();
		if (creatorId == null || creatorId.isEmpty()) return;
		videosByCreator.computeIfAbsent(creatorId, k -> new CopyOnWriteArrayList<>()).add(video);
		// Ensure the creator is registered minimally
		creators.putIfAbsent(creatorId, video.getCreator());
	}

	@Override
	public void getCreator(CreatorIdRequest request, StreamObserver<Creator> responseObserver) {
		String id = request == null ? "" : request.getId();
		Creator c = creators.get(id);
		if (c == null) {
			responseObserver.onError(Status.NOT_FOUND.withDescription("Creator not found for id: " + id).asRuntimeException());
			return;
		}
		responseObserver.onNext(c);
		responseObserver.onCompleted();
	}

	@Override
	public void getCreatorVideos(CreatorIdRequest request, StreamObserver<VideoStream> responseObserver) {
		String id = request == null ? "" : request.getId();
		if (!creators.containsKey(id)) {
			responseObserver.onError(Status.NOT_FOUND.withDescription("Creator not found for id: " + id).asRuntimeException());
			return;
		}
		List<Video> list = videosByCreator.getOrDefault(id, Collections.emptyList());
		VideoStream stream = VideoStream.newBuilder().addAllVideos(list).build();
		responseObserver.onNext(stream);
		responseObserver.onCompleted();
	}
}
