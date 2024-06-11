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

package org.camelcookbook.splitjoin.split;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates the splitting of a List by using a Simple expression to locate it in an object graph.
 */
public class SplitSimpleExpressionSpringTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/splitSimpleExpression-context.xml");
    }

    @Test
    public void testSimpleExpressionReferenceToList() throws Exception {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(3);
        mockOut.expectedBodiesReceived("one", "two", "three");

        List<String> list = new LinkedList<String>();
        list.add("one");
        list.add("two");
        list.add("three");
        ListWrapper wrapper = new ListWrapper();
        wrapper.setWrapped(list);

        template.sendBody("direct:in", wrapper);

        assertMockEndpointsSatisfied();
    }

}
