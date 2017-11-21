/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.nativeplatform.fixtures.debug

import org.gradle.nativeplatform.fixtures.NativeBinaryFixture
import org.gradle.nativeplatform.fixtures.app.SourceElement
import org.gradle.test.fixtures.file.TestFile


trait DebugInfo {
    void assertHasDebugSymbolsForSources(TestFile binary, SourceElement sourceElement) {
        assertHasDebugSymbolsForSources(new NativeBinaryFixture(binary, null), sourceElement)
    }

    void assertHasDebugSymbolsForSources(NativeBinaryFixture fixture, SourceElement sourceElement) {
        assert fixture.binaryInfo.hasDebugSymbolsFor(sourceElement.files.findAll { !it.name.endsWith(".h") }.collect { it.name })
    }

    void assertDoesNotHaveDebugSymbolsForSources(TestFile binary, SourceElement sourceElement) {
        assertDoesNotHaveDebugSymbolsForSources(new NativeBinaryFixture(binary, null), sourceElement)

    }

    void assertDoesNotHaveDebugSymbolsForSources(NativeBinaryFixture fixture, SourceElement sourceElement) {
        assert fixture.binaryInfo.doesNotHaveDebugSymbolsFor(sourceElement.files.findAll { !it.name.endsWith(".h") }.collect { it.name })
    }
}
