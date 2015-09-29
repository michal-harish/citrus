/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.camel.actions;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Christoph Deppisch
 * @since 2.4
 */
public class RemoveCamelRouteAction extends AbstractCamelRouteAction {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(RemoveCamelRouteAction.class);

    /** The Camel route to start */
    private List<String> routeIds;

    /**
     * Default constructor.
     */
    public RemoveCamelRouteAction() {
        setName("remove-routes");
    }

    @Override
    public void doExecute(TestContext context) {
        for (String routeId : routeIds) {
            try {
                log.info(String.format("Removing Camel route '%s' from context '%s'", routeId, camelContext.getName()));

                if (!camelContext.getRouteStatus(routeId).isStopped()) {
                    throw new CitrusRuntimeException("Camel routes must be stopped before removal!");
                }

                if (camelContext.removeRoute(routeId)) {
                    log.info(String.format("Successfully removed Camel route '%s'", routeId));
                } else {
                    throw new CitrusRuntimeException(String.format("Failed to remove Camel route '%s' from context '%s'", routeId, camelContext.getName()));
                }
            } catch (CitrusRuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new CitrusRuntimeException(String.format("Failed to remove Camel route '%s' from context '%s'", routeId, camelContext.getName()), e);
            }
        }
    }

    /**
     * Sets the Camel routes to stop.
     * @param routeIds
     */
    public void setRouteIds(List<String> routeIds) {
        this.routeIds = routeIds;
    }

    /**
     * Gets the Camel routes to remove.
     * @return
     */
    public List<String> getRouteIds() {
        return routeIds;
    }
}