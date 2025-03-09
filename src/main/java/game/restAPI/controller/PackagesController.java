package game.restAPI.controller;

import game.restAPI.repository.PackageRepository;
import java.io.OutputStream;
import java.io.IOException;

public class PackagesController implements Controller {
    private final PackageRepository packageRepository;

    public PackagesController(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        System.out.println("[DEBUG] Handling packages request: " + method);

        if (method.equals("POST")) {
            output.write("HTTP/1.1 201 Created\r\n\r\nPackage created successfully".getBytes());
        } else {
            output.write("HTTP/1.1 405 Method Not Allowed\r\n\r\n".getBytes());
        }
        output.flush();
    }
}

