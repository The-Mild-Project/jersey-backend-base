package com.the.mild.project.server.util;

public class ResourceConfig {
    public static final String PATH_TEST_RESOURCE = "testresource";

    public static final String PATH_TEST_RESOURCE_WITH_PARAM_ID = "id";
    public static final String PATH_TEST_RESOURCE_WITH_PARAM = "/test/{" + PATH_TEST_RESOURCE_WITH_PARAM_ID + "}";
    public static final String PATH_TEST_RESOURCE_WITH_PARAM_FORMAT = "/test/%s";
}
