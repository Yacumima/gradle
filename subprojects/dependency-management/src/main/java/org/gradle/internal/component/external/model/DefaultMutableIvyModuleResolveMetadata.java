/*
 * Copyright 2016 the original author or authors.
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

package org.gradle.internal.component.external.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.internal.artifacts.ivyservice.NamespaceId;
import org.gradle.internal.component.external.descriptor.Artifact;
import org.gradle.internal.component.external.descriptor.Configuration;
import org.gradle.internal.component.model.DefaultIvyArtifactName;
import org.gradle.internal.component.model.Exclude;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

import static org.gradle.api.artifacts.Dependency.DEFAULT_CONFIGURATION;

public class DefaultMutableIvyModuleResolveMetadata extends AbstractMutableModuleComponentResolveMetadata<IvyConfigurationMetadata> implements MutableIvyModuleResolveMetadata {
    private final ImmutableList<Artifact> artifactDefinitions;
    private final ImmutableMap<String, Configuration> configurationDefinitions;
    private ImmutableList<Exclude> excludes;
    private ImmutableMap<NamespaceId, String> extraAttributes;
    private String branch;

    /**
     * Creates default metadata for an Ivy module version with no ivy.xml descriptor.
     */
    public static DefaultMutableIvyModuleResolveMetadata missing(ModuleVersionIdentifier id, ModuleComponentIdentifier componentIdentifier) {
        DefaultMutableIvyModuleResolveMetadata metadata = new DefaultMutableIvyModuleResolveMetadata(id, componentIdentifier);
        metadata.setMissing(true);
        return metadata;
    }

    public DefaultMutableIvyModuleResolveMetadata(ModuleVersionIdentifier id, ModuleComponentIdentifier componentIdentifier) {
        this(id, componentIdentifier, ImmutableList.<ModuleDependencyMetadata>of());
    }

    public DefaultMutableIvyModuleResolveMetadata(ModuleVersionIdentifier id, ModuleComponentIdentifier componentIdentifier, Collection<? extends ModuleDependencyMetadata> dependencies) {
        this(id, componentIdentifier,
            ImmutableList.of(new Configuration(DEFAULT_CONFIGURATION, true, true, ImmutableSet.<String>of())),
            ImmutableList.copyOf(dependencies),
            ImmutableList.of(new Artifact(new DefaultIvyArtifactName(componentIdentifier.getModule(), "jar", "jar"), ImmutableSet.of(DEFAULT_CONFIGURATION))),
            ImmutableList.<Exclude>of());
    }

    public DefaultMutableIvyModuleResolveMetadata(ModuleVersionIdentifier id, ModuleComponentIdentifier componentIdentifier, Collection<Configuration> configurationDefinitions,
                                                  Collection<? extends ModuleDependencyMetadata> dependencies, Collection<? extends Artifact> artifactDefinitions, Collection<? extends Exclude> excludes) {
        super(id, componentIdentifier, ImmutableList.copyOf(dependencies));
        this.configurationDefinitions = toMap(configurationDefinitions);
        this.artifactDefinitions = ImmutableList.copyOf(artifactDefinitions);
        this.excludes = ImmutableList.of();
        this.extraAttributes = ImmutableMap.of();
        this.excludes = ImmutableList.copyOf(excludes);
    }

    public DefaultMutableIvyModuleResolveMetadata(IvyModuleResolveMetadata metadata) {
        super(metadata);
        this.configurationDefinitions = metadata.getConfigurationDefinitions();
        this.artifactDefinitions = metadata.getArtifactDefinitions();
        this.excludes = metadata.getExcludes();
        this.branch = metadata.getBranch();
        this.extraAttributes = metadata.getExtraAttributes();
    }

    private static ImmutableMap<String, Configuration> toMap(Collection<Configuration> configurations) {
        ImmutableMap.Builder<String, Configuration> builder = ImmutableMap.builder();
        for (Configuration configuration : configurations) {
            builder.put(configuration.getName(), configuration);
        }
        return builder.build();
    }

    @Override
    public ImmutableMap<String, Configuration> getConfigurationDefinitions() {
        return configurationDefinitions;
    }

    @Override
    public boolean definesVariant(String name) {
        return configurationDefinitions.containsKey(name);
    }

    @Override
    public ImmutableList<Artifact> getArtifactDefinitions() {
        return artifactDefinitions;
    }

    @Override
    public ImmutableList<Exclude> getExcludes() {
        return excludes;
    }

    @Override
    public ImmutableMap<NamespaceId, String> getExtraAttributes() {
        return extraAttributes;
    }

    @Override
    public void setExtraAttributes(Map<NamespaceId, String> extraAttributes) {
        this.extraAttributes = ImmutableMap.copyOf(extraAttributes);
    }

    @Nullable
    @Override
    public String getBranch() {
        return branch;
    }

    @Override
    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public IvyModuleResolveMetadata asImmutable() {
        return new DefaultIvyModuleResolveMetadata(this);
    }

}
