/*
 * Copyright (C) Scott Cranton, Jakub Korab, and Christian Posta
 * https://github.com/CamelCookbook
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

package org.camelcookbook.structuringroutes.routecontrol;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates the manual shutting down of a route.
 */
public class RouteStoppingProcessorTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:in").routeId("mainRoute")
                    .log("Stopping route")
                    .process(new RouteStoppingProcessor())
                    .log("Signalled to stop route")
                    .to("mock:out");

                from("timer:statusChecker").routeId("statusChecker")
                    .to("controlbus:route?routeId=mainRoute&action=status")
                    .filter(simple("${body} == 'Stopped'"))
                    .to("mock:stopped");
            }
        };
    }

    @Test
    public void testRouteShutdown() throws InterruptedException {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);

        MockEndpoint mockStopped = getMockEndpoint("mock:stopped");
        mockStopped.setExpectedMessageCount(1);

        template.sendBody("direct:in", "mainRoute");

        assertMockEndpointsSatisfied();
    }
}
