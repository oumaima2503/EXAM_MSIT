package ma.xproce.mediaclient.web;

import ma.xproce.mediaclient.dto.UploadVideoRequestDto;
import ma.xproce.mediaclient.dto.VideoDto;
import ma.xproce.mediaclient.service.VideoServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController("videoControllerOld")
public class VideoController {
	@Autowired
	private VideoServiceClient videoServiceClient;

	// Upload  vidéo
	@PostMapping("/videos")
	public ResponseEntity<VideoDto> uploadVideo(@RequestBody UploadVideoRequestDto req) {
		try {
			VideoDto videoDto = videoServiceClient.uploadVideo(req);
			System.out.println("Video uploaded: " + videoDto);
			return ResponseEntity.ok(videoDto);
		} catch (Exception ex) {
			System.err.println("Error uploading video: " + ex.getMessage());
			return ResponseEntity.status(500).build();
		}
	}

	// Récupère une vidéo par id
	@GetMapping("/videos/{id}")
	public ResponseEntity<VideoDto> getVideo(@PathVariable String id) {
		try {
			VideoDto videoDto = videoServiceClient.getVideo(id);
			if (videoDto == null) {
				return ResponseEntity.notFound().build();
			}
			System.out.println("Video retrieved: " + videoDto);
			return ResponseEntity.ok(videoDto);
		} catch (Exception ex) {
			System.err.println("Error retrieving video: " + ex.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
}


