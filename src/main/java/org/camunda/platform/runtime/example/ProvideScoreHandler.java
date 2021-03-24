/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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
package org.camunda.platform.runtime.example;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ExternalTaskSubscription("scoreProvider") // create a subscription for this topic name
public class ProvideScoreHandler implements ExternalTaskHandler {

  @Override
  public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

    // only for the sake of this demonstration, we generate random data
    // in a real-world scenario, we would load the data from a database
    String customerId = "C-" + UUID.randomUUID().toString().substring(32);
    int creditScore = (int) (Math.random() * 11);

    VariableMap variables = Variables.createVariables();
    variables.put("customerId", customerId);
    variables.put("creditScore", creditScore);

    // complete the external task
    externalTaskService.complete(externalTask, variables);

    Logger.getLogger("scoreProvider")
        .log(Level.INFO, "Credit score {0} for customer {1} provided!", new Object[]{creditScore, customerId});
  }

}
