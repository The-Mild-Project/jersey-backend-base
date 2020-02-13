package com.the.mild.project;

import static com.the.mild.project.ResourceConfig.PathParams.PATH_PARAM_EXAMPLE_ID;
import static com.the.mild.project.ResourceConfig.PathParams.PATH_PARAM_ID;

public final class ResourceConfig {
    public static final String SERVICE_NAME = "test";

    // Resource path names
    public static final String PATH_TEST_RESOURCE = "testresource";
    public static final String PATH_TEST_RESOURCE_WITH_PARAM = "{" + PATH_PARAM_ID + "}";
    public static final String PATH_TEST_RESOURCE_WITH_MULTIPLE_PARAMS = "/{" +
                                                                             PATH_PARAM_ID + "}/example/{" +
                                                                             PATH_PARAM_EXAMPLE_ID + "}";

    // public enum Resource {
    //     PATH_TEST_RESOURCE("testresource");
    //
    //     private final String pathname;
    //
    //     Resource(String pathname) {
    //         this.pathname = pathname;
    //     }
    //
    //     public String getPathname() {
    //         return pathname;
    //     }
    // }

    public static final class PathFormats {
        public static final String PATH_TEST_RESOURCE_WITH_PARAM_FORMAT = "/%s";
        public static final String PATH_TEST_RESOURCE_WITH_MULTIPLE_PARAMS_FORMAT = "/%s/example/%s";

        private PathFormats() {
            // Utility
        }
    }

    public static final class PathParams {
        public static final String PATH_PARAM_ID = "id";
        public static final String PATH_PARAM_EXAMPLE_ID = "exampleId";

        private PathParams() {
            // Utility
        }
    }

    private ResourceConfig() {
        // Utility
    }
}
