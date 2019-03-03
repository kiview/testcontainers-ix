package dev.kiview.ix.testcontainersarticle;

import org.junit.Assert;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpExample {

    @Test
    public void simpleHttpRequestToContainer() throws IOException, InterruptedException {
        GenericContainer httpdContainer = new GenericContainer("httpd:2.4")
                .withExposedPorts(80);

        httpdContainer.start();

        String containerUri = "http://"
                + httpdContainer.getContainerIpAddress() + ":"
                + httpdContainer.getFirstMappedPort();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(containerUri)).build();

        String responseBody = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString())
                .body();

        Assert.assertEquals("<html><body><h1>It works!</h1></body></html>", responseBody.strip());
        httpdContainer.stop();
    }

    @Test
    public void tryWithResourcesUsage() throws IOException, InterruptedException {

        try(GenericContainer httpdContainer = new GenericContainer("httpd:2.4")
                .withExposedPorts(80)) {

            httpdContainer.start();

            String containerUri = "http://"
                    + httpdContainer.getContainerIpAddress() + ":"
                    + httpdContainer.getFirstMappedPort();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(containerUri)).build();

            String responseBody = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body();

            Assert.assertEquals("<html><body><h1>It works!</h1></body></html>", responseBody.strip());
        }
    }


}
