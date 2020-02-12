package com.the.mild.project.server;

import java.util.Arrays;

// import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import com.the.mild.project.db.mongo.util.MongoDatabaseFactory;
import com.the.mild.project.db.mongo.util.MongoDatabaseType;
import com.the.mild.project.db.mongo.util.MongoEnv;
// import org.glassfish.jersey.media.json.JsonJacksonModule;
// import org.glassfish.jersey.com.the.mild.project.server.Application;
// import org.glassfish.jersey.com.the.mild.project.server.ResourceConfig;
// import org.glassfish.jersey;

public class ServerTester {
    private static final MongoEnv env = new MongoEnv();

    private static MongoDatabaseFactory factory;

    public static void main(String[] args) throws InterruptedException {
        factory = new MongoDatabaseFactory(env.getMongoUser(), env.getMongoPass());

        Arrays.stream(MongoDatabaseType.values())
              .forEach(type -> factory.addDatabase(type, env.getMongoCluster()));

        // final MongoDatabase developTestDb = factory.getDatabase(MongoDatabaseType.DEVELOP_TEST);
        // final MongoDocumentHandler handler = new MongoDocumentHandler(developTestDb);

        // final int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 8080;
        // final URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
        //
        // final Application application = Application.builder(ResourceConfig.forApplicationClass(ServerTester.class.getPackage().getName()).build()).build();
        // application.addModules(new JsonJacksonModule());
        // final HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, application);
        // httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("src/main/webapp"), CONTENT_PATH);
        //
        // for (NetworkListener networkListener : httpServer.getListeners()) {
        //     if (System.getenv("FILE_CACHE_ENABLED") == null) {
        //         networkListener.getFileCache().setEnabled(false);
        //     }
        // }
        //
        // Runtime.getRuntime().addShutdownHook(new Thread() {
        //     @Override
        //     public void run() {
        //         httpServer.stop();
        //     }
        // });

        // MongoURI mongolabUri = new MongoURI(System.getenv("MONGOLAB_URI") != null ? System.getenv("MONGOLAB_URI") : "mongodb://127.0.0.1:27017/hello");
        // Mongo m = new Mongo(mongolabUri);
        // mongoDB = m.getDB(mongolabUri.getDatabase());
        // if ((mongolabUri.getUsername() != null) && (mongolabUri.getPassword() != null)) {
        //     mongoDB.authenticate(mongolabUri.getUsername(), mongolabUri.getPassword());
        // }
        //
        // contentUrl = System.getenv("CONTENT_URL") != null ? System.getenv("CONTENT_URL") : CONTENT_PATH;

        Thread.currentThread().join();
    }
}
