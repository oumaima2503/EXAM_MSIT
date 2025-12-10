package ma.xproce.mediaclient.web;

import ma.xproce.mediaclient.dto.UploadVideoRequestDto;
import ma.xproce.mediaclient.dto.VideoDto;
import ma.xproce.mediaclient.dto.VideoStreamDto;
import ma.xproce.mediaclient.service.CreatorServiceClient;
import ma.xproce.mediaclient.service.VideoServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VideoController {

	@Autowired
	private VideoServiceClient videoServiceClient;

	@Autowired
	private CreatorServiceClient creatorServiceClient;

	@PostMapping("/videos")
	public ResponseEntity<VideoDto> uploadVideo(@RequestBody UploadVideoRequestDto req) {
		VideoDto created = videoServiceClient.uploadVideo(req);
		return ResponseEntity.ok(created);
	}

	@GetMapping("/videos/{id}")
	public ResponseEntity<VideoDto> getVideo(@PathVariable String id) {
		VideoDto v = videoServiceClient.getVideo(id);
		if (v == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(v);
	}

	@GetMapping("/creators/{id}")
	public ResponseEntity<?> getCreator(@PathVariable String id) {
		var c = creatorServiceClient.getCreator(id);
		if (c == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(c);
	}

	@GetMapping("/creators/{id}/videos")
	public ResponseEntity<VideoStreamDto> getCreatorVideos(@PathVariable String id) {
		VideoStreamDto stream = creatorServiceClient.getCreatorVideos(id);
		return ResponseEntity.ok(stream);
	}
}