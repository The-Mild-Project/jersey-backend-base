package com.the.mild.project.server.util;

import static com.the.mild.project.server.util.ResourceConfig.PathParam.PATH_PARAM_EXAMPLE_ID;
import static com.the.mild.project.server.util.ResourceConfig.PathParam.PATH_PARAM_ID;

public final class ResourceConfig {
    public static final String PATH_TEST_RESOURCE = "testresource";

    public static final String PATH_TEST_RESOURCE_PREFIX = "/test";
    public static final String PATH_TEST_RESOURCE_WITH_PARAM = PATH_TEST_RESOURCE_PREFIX + "/{" +
                                                                   PATH_PARAM_ID + "}";
    public static final String PATH_TEST_RESOURCE_WITH_PARAM_FORMAT = PATH_TEST_RESOURCE_PREFIX + "/%s";

    public static final String PATH_TEST_RESOURCE_WITH_MULTIPLE_PARAMS = PATH_TEST_RESOURCE_PREFIX + "/{" +
                                                                             PATH_PARAM_ID + "}/example/{" +
                                                                             PATH_PARAM_EXAMPLE_ID + "}";

    public static final String PATH_TEST_RESOURCE_WITH_MULTIPLE_PARAMS_FORMAT = PATH_TEST_RESOURCE_PREFIX + "/%s/example/%s";

    private ResourceConfig() {
        // Utility
    }

    public static final class PathParam {
        public static final String PATH_PARAM_ID = "id";
        public static final String PATH_PARAM_EXAMPLE_ID = "exampleId";

        private PathParam() {
            // Utility
        }
    }
}
