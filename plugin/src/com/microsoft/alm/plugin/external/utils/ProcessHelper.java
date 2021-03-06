// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.plugin.external.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProcessHelper {
    public static Process startProcess(final String workingDirectory, final List<String> arguments) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder(arguments);

        // Disable any telemetry that the tool may initiate
        pb.environment().put("tf_notelemetry", "TRUE");
        pb.environment().put("TF_ADDITIONAL_JAVA_ARGS", "-Duser.country=US -Duser.language=en");

        if (StringUtils.isNotEmpty(workingDirectory)) {
            pb.directory(new File(workingDirectory));
        }
        return pb.start();
    }
}
