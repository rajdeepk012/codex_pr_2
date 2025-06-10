package co.mw.gf_dashboard_service.common;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.Base64;

public class Base64ToResourceTransformer {

    public static Resource transformBase64ToResource(String base64, String fileName) {
        if (base64.contains(",")) {
            base64 = base64.split(",")[1];
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64);

        return new ByteArrayResource(decodedBytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
    }
}
