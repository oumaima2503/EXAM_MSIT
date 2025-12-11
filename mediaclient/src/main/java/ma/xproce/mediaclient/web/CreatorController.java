package ma.xproce.mediaclient.web;

import ma.xproce.mediaclient.dto.CreatorDto;
import ma.xproce.mediaclient.dto.VideoStreamDto;
import ma.xproce.mediaclient.service.CreatorServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class CreatorController {
	@Autowired
	private CreatorServiceClient creatorServiceClient;

	// Récupère les infos d'un creator
	@GetMapping("/creators/{id}")
	public ResponseEntity<CreatorDto> getCreator(@PathVariable String id) {
		try {
			CreatorDto creatorDto = creatorServiceClient.getCreator(id);
			if (creatorDto == null) {
				return ResponseEntity.notFound().build();
			}
			System.out.println("Creator retrieved: " + creatorDto);
			return ResponseEntity.ok(creatorDto);
		} catch (Exception ex) {
			System.err.println("Error retrieving creator: " + ex.getMessage());
			return ResponseEntity.status(500).build();
		}
	}

	// Récupère les vidéos d'un creator
	@GetMapping("/creators/{id}/videos")
	public ResponseEntity<VideoStreamDto> getCreatorVideos(@PathVariable String id) {
		try {
			VideoStreamDto streamDto = creatorServiceClient.getCreatorVideos(id);
			System.out.println("Creator videos retrieved: " + streamDto);
			return ResponseEntity.ok(streamDto);
		} catch (Exception ex) {
			System.err.println("Error retrieving creator videos: " + ex.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
}
