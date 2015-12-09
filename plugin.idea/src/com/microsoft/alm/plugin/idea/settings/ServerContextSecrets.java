// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.plugin.idea.settings;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.microsoft.alm.common.utils.UrlHelper;
import com.microsoft.alm.plugin.authentication.AuthenticationInfo;
import com.microsoft.alm.plugin.context.ServerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerContextSecrets {
    private static final Logger logger = LoggerFactory.getLogger(ServerContextSecrets.class);

    public static void forget(final String key) {
        forgetPassword(key);
    }

    public static AuthenticationInfo load(final String key) throws IOException {
        final String authInfoSerialized = readPassword(key);

        AuthenticationInfo info = null;
        if (StringUtils.isNotEmpty(authInfoSerialized)) {
            info = JsonHelper.read(authInfoSerialized, AuthenticationInfo.class);
        }

        if (info == null) {
            forget(key);
            logger.warn("getServerContextSecrets: info was null for key: ", key);
            return null;
        }
        return info;
    }

    public static void save(final ServerContext context) {
        if (context == null) {
            return;
        }

        final String key = UrlHelper.asString(context.getUri());
        final AuthenticationInfo authenticationInfo = context.getAuthenticationInfo();
        final String stringValue = JsonHelper.write(authenticationInfo);

        writePassword(key, stringValue);
    }

    public static void forgetPassword(final String key) {
        try {
            PasswordSafe.getInstance().removePassword(null, ServerContextSecrets.class, key);
        } catch (PasswordSafeException e) {
            logger.warn("Failed to clear password store", e);
        }
    }

    public static void writePassword(final String key, final String value) {
        try {
            PasswordSafe.getInstance().storePassword(null, ServerContextSecrets.class, key, value);
        } catch (PasswordSafeException e) {
            logger.warn("Failed to get password", e);
        }
    }

    public static String readPassword(final String key) {
        String password = null;
        try {
            password = PasswordSafe.getInstance().getPassword(null, ServerContextSecrets.class, key);
        } catch (PasswordSafeException e) {
            logger.warn("Failed to read password", e);
        }
        return password;
    }
}