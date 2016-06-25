/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.showcase.extension.autodiscover.client;

import org.easymock.Mock;
import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.showcase.extension.autodiscover.ReflectionHelper;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * DeclarativeArchiveProcessor
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class ArchiveProcessor implements ApplicationArchiveProcessor {
	
	private static final String ANNOTATION_CLASS_NAME = "org.easymock.Mock";
	private static final String EASYMOCK_ARTIFACT = "org.easymock:easymock";

	private static final String FEST_ASSERTIONS_CLASS_NAME = "org.fest.assertions.Assertions";
	private static final String FEST_ARTIFACT = "org.easytesting:fest-assert";

	@Override
	public void process(Archive<?> applicationArchive, TestClass testClass) {
		if (ReflectionHelper.isClassPresent(FEST_ASSERTIONS_CLASS_NAME)) {
			appendFestLibrary(applicationArchive);
		}

		if (ReflectionHelper.isClassPresent(ANNOTATION_CLASS_NAME)) {
			if (ReflectionHelper.getFieldsWithAnnotation(testClass.getJavaClass(), Mock.class).size() > 0) {
				appendMockitoLibrary(applicationArchive);
			}
		}
	}

	private void appendFestLibrary(Archive<?> applicationArchive) {
		if (applicationArchive instanceof LibraryContainer) {
			LibraryContainer<?> container = (LibraryContainer<?>) applicationArchive;
			container.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
					.loadMetadataFromPom("pom.xml").artifact(FEST_ARTIFACT).resolveAsFiles());
		}
	}

	private void appendMockitoLibrary(Archive<?> applicationArchive) {
		if (applicationArchive instanceof LibraryContainer) {
			LibraryContainer<?> container = (LibraryContainer<?>) applicationArchive;
			container.addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
					.loadMetadataFromPom("pom.xml").artifact(EASYMOCK_ARTIFACT).resolveAsFiles());
		}
	}
}
