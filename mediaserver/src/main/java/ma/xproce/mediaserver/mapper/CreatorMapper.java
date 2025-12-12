package ma.xproce.mediaserver.mapper;

import ma.xproce.mediaappgrpc.proto.Creator;

import org.springframework.stereotype.Component;

@Component
public class CreatorMapper {

	// proto -> entity
	public ma.xproce.mediaserver.dao.entities.Creator toEntity(Creator proto) {
		if (proto == null) return null;
		ma.xproce.mediaserver.dao.entities.Creator e = new ma.xproce.mediaserver.dao.entities.Creator();
		// keep proto id if present (JPA expects String id)
		if (proto.getId() != null && !proto.getId().isEmpty()) {
			e.setId(proto.getId());
		}
		e.setName(proto.getName() == null ? "" : proto.getName());
		e.setEmail(proto.getEmail() == null ? "" : proto.getEmail());
		return e;
	}

	// entity -> proto
	public Creator toProto(ma.xproce.mediaserver.dao.entities.Creator e) {
		if (e == null) return Creator.getDefaultInstance();
		return Creator.newBuilder()
				.setId(e.getId() == null ? "" : e.getId())
				.setName(e.getName() == null ? "" : e.getName())
				.setEmail(e.getEmail() == null ? "" : e.getEmail())
				.build();
	}
}