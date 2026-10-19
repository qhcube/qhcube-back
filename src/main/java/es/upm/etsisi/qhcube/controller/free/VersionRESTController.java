package es.upm.etsisi.qhcube.controller.free;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${apiFreePrefix}")
public class VersionRESTController {

    private final String version;

    public VersionRESTController(Optional<BuildProperties> buildProperties) {
        if (buildProperties.isPresent()) {
            this.version = buildProperties.get().getVersion();
        } else {
            this.version = "desconocida";
        }
    }

    @GetMapping("/version-back")
    public ResponseEntity<Map<String, String>> getVersion() {
        return ResponseEntity.ok(Map.of("version", this.version));
    }
}
