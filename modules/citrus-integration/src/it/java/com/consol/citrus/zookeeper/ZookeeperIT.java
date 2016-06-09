/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.zookeeper;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.zookeeper.command.CommandResultCallback;
import com.consol.citrus.zookeeper.command.ZooResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author Martin Maher
 * @since 2.5
 */
public class ZookeeperIT extends TestNGCitrusTestDesigner {

    @Test
    @CitrusTest(name = "Zookeeper_01_IT")
    public void zookeeper01IT() {
        variable("expectedConnectionState", "CONNECTED");
        variable("randomString", "citrus:randomString(10)");

        zookeeper()
            .addValidator("$.responseData.state", "${expectedConnectionState}")
            .info();

        zookeeper()
            .addVariableExtractor("$.responseData.path", "path")
            .create("/${randomString}", "some test data")
            .acl("OPEN_ACL_UNSAFE")
            .mode("PERSISTENT")
            .validateCommandResult(new CommandResultCallback<ZooResponse>() {
                @Override
                public void doWithCommandResult(ZooResponse result, TestContext context) {
                    Map<String, Object> responseData = result.getResponseData();
                    Assert.assertNotNull(responseData);
                    Assert.assertTrue(responseData.containsKey("path"));
                }
            });

        zookeeper()
            .exists("${path}")
            .validateCommandResult(new CommandResultCallback<ZooResponse>() {
                @Override
                public void doWithCommandResult(ZooResponse result, TestContext context) {
                    Map<String, Object> responseData = result.getResponseData();
                    Assert.assertNotNull(responseData);
                    Assert.assertEquals(responseData.get("version"), 0);
                }
            });
    }
}