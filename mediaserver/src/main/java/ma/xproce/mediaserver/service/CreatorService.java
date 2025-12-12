package ma.xproce.mediaserver.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ma.xproce.mediaappgrpc.proto.Creator;
import ma.xproce.mediaappgrpc.proto.CreatorIdRequest;
import ma.xproce.mediaappgrpc.proto.Video;
import ma.xproce.mediaappgrpc.proto.VideoStream;
import ma.xproce.mediaappgrpc.proto.CreatorServiceGrpc;
import ma.xproce.mediaserver.mapper.CreatorMapper;
import ma.xproce.mediaserver.mapper.VideoMapper;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@GrpcService
public class CreatorService extends CreatorServiceGrpc.CreatorServiceImplBase {

	// Stockage en mémoire des entités Creator
	private final Map<String, ma.xproce.mediaserver.dao.entities.Creator> creatorEntities = new ConcurrentHashMap<>();

	// Stockage vidéos par creatorId (entités)
	private final Map<String, List<ma.xproce.mediaserver.dao.entities.Video>> videoEntitiesByCreator = new ConcurrentHashMap<>();

	private final CreatorMapper creatorMapper;
	private final VideoMapper videoMapper;

	public CreatorService(CreatorMapper creatorMapper, VideoMapper videoMapper) {
		this.creatorMapper = creatorMapper;
		this.videoMapper = videoMapper;
	}

	// Méthode utilitaire pour créer un creator
	public Creator createCreator(String name, String email) {
		String id = UUID.randomUUID().toString();

		// Création de l'entité
		ma.xproce.mediaserver.dao.entities.Creator entity = new ma.xproce.mediaserver.dao.entities.Creator();
		entity.setId(id);
		entity.setName(name == null ? "" : name);
		entity.setEmail(email == null ? "" : email);

		// Stockage de l'entité
		creatorEntities.put(id, entity);
		videoEntitiesByCreator.putIfAbsent(id, new CopyOnWriteArrayList<>());

		// Conversion en proto pour le retour
		return creatorMapper.toProto(entity);
	}

	// Enregistrer un Creator
	public void registerCreator(Creator creator) {
		if (creator == null) return;

		if (creator.getId() == null || creator.getId().isEmpty()) {
			createCreator(creator.getName(), creator.getEmail());
			return;
		}

		// Conversion du proto en entité
		ma.xproce.mediaserver.dao.entities.Creator entity = creatorMapper.toEntity(creator);
		creatorEntities.put(creator.getId(), entity);
		videoEntitiesByCreator.putIfAbsent(creator.getId(), new CopyOnWriteArrayList<>());
	}

	// Retourne un creator existant (en entité)
	public ma.xproce.mediaserver.dao.entities.Creator findCreatorEntity(String id) {
		if (id == null || id.isEmpty()) return null;
		return creatorEntities.get(id);
	}

	// Retourne un creator existant (en proto)
	public Creator findCreator(String id) {
		ma.xproce.mediaserver.dao.entities.Creator entity = findCreatorEntity(id);
		return entity != null ? creatorMapper.toProto(entity) : null;
	}

	// Ajouter une vidéo pour le creator (version entité)
	public void addVideoForCreator(ma.xproce.mediaserver.dao.entities.Video videoEntity) {
		if (videoEntity == null || videoEntity.getCreator() == null) return;

		String creatorId = videoEntity.getCreator().getId();
		if (creatorId == null || creatorId.isEmpty()) return;

		// S'assurer que le creator existe
		if (!creatorEntities.containsKey(creatorId)) {
			creatorEntities.put(creatorId, videoEntity.getCreator());
		}

		videoEntitiesByCreator.computeIfAbsent(creatorId, k -> new CopyOnWriteArrayList<>()).add(videoEntity);
	}

	// Ajouter une vidéo pour le creator (version proto)
	public void addVideoForCreator(Video videoProto) {
		if (videoProto == null || !videoProto.hasCreator()) return;

		// Convertir le proto en entité
		ma.xproce.mediaserver.dao.entities.Video videoEntity = videoMapper.toEntity(videoProto);
		addVideoForCreator(videoEntity);
	}

	@Override
	public void getCreator(CreatorIdRequest request, StreamObserver<Creator> responseObserver) {
		String id = request == null ? "" : request.getId();
		ma.xproce.mediaserver.dao.entities.Creator entity = creatorEntities.get(id);

		if (entity == null) {
			responseObserver.onError(Status.NOT_FOUND
					.withDescription("Creator not found for id: " + id)
					.asRuntimeException());
			return;
		}

		// Conversion de l'entité vers proto
		responseObserver.onNext(creatorMapper.toProto(entity));
		responseObserver.onCompleted();
	}

	@Override
	public void getCreatorVideos(CreatorIdRequest request, StreamObserver<VideoStream> responseObserver) {
		String id = request == null ? "" : request.getId();

		if (!creatorEntities.containsKey(id)) {
			responseObserver.onError(Status.NOT_FOUND
					.withDescription("Creator not found for id: " + id)
					.asRuntimeException());
			return;
		}

		List<ma.xproce.mediaserver.dao.entities.Video> entityList = videoEntitiesByCreator.getOrDefault(id, Collections.emptyList());

		// Conversion des entités en proto
		List<Video> videoList = entityList.stream()
				.map(videoMapper::toProto)
				.collect(Collectors.toList());

		VideoStream stream = VideoStream.newBuilder().addAllVideos(videoList).build();
		responseObserver.onNext(stream);
		responseObserver.onCompleted();
	}
}