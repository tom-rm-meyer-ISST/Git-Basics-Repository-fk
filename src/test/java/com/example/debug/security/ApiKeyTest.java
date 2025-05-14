/*
 * Copyright (c) 2023 Volkswagen AG
 * Copyright (c) 2025 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V.
 * (represented by Fraunhofer ISST)
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.example.debug.security;

import com.example.debug.security.annotation.WithMockApiKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiKeyTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void GreetingController_GreetingRequestWithoutAuthHeader_ShouldReturn403() throws Exception {
        this.mockMvc.perform(
                        get("/greeting"))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    void GreetingController_GreetingRequestWithAuthHeader_ShouldReturn200() throws Exception {
        this.mockMvc.perform(
                        get("/greeting")
                                .header("X-API-KEY", "test")
                )
                .andExpect(status().is(200));
    }

    @Test
    @WithMockApiKey(apiKey = "test2")
    void GreetingController_GreetingRequestWithWrongAnnotationAuth_ShouldReturn403() throws Exception {
        this.mockMvc.perform(
                        get("/greeting")
                )
                .andExpect(status().is(403));
    }

    @Test
    @WithMockApiKey
    void GreetingController_GreetingRequestWithCorrectAnnotationAuth_ShouldReturn200() throws Exception {
        this.mockMvc.perform(
                        get("/greeting")
                )
                .andExpect(status().is(200));
    }
}
