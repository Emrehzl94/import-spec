/*
 * Copyright (c) "Neo4j"
 * Neo4j Sweden AB [https://neo4j.com]
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
package org.neo4j.importer.v1.validation.plugin;

import org.neo4j.importer.v1.sources.Source;
import org.neo4j.importer.v1.validation.SpecificationValidationResult.Builder;
import org.neo4j.importer.v1.validation.SpecificationValidator;

public class NoDuplicatedSourceNameValidator implements SpecificationValidator {
    private static final String ERROR_CODE = "DUPL-004";

    private final NameCounter nameCounter;

    public NoDuplicatedSourceNameValidator() {
        nameCounter = new NameCounter(ERROR_CODE);
    }

    @Override
    public void visitSource(int index, Source source) {
        nameCounter.track(source.getName(), String.format("$.sources[%d]", index));
    }

    @Override
    public boolean report(Builder builder) {
        return nameCounter.reportErrorsIfAny(builder);
    }
}
