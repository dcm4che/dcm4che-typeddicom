package org.dcm4che.typeddicom.publisher;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.credentials.PasswordCredentials;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class TypeddicomPublisherPlugin implements Plugin<Project> {
    private static final String TYPEDDICOM_GROUP = "org.dcm4che";
    private static final String TYPEDDICOM_VERSION = "0.5.1-SNAPSHOT";

    private static final String STANDARD_PUBLICATION_NAME = "mavenJava";
    private static final String GRADLE_PLUGIN_PUBLICATION_NAME = "pluginMaven";
    private TypeddicomPublisherPluginExtension extension;

    @Override
    public void apply(@NotNull Project project) {
        project.setGroup(TYPEDDICOM_GROUP);
        project.setVersion(TYPEDDICOM_VERSION);

        this.extension = project.getExtensions().create("typeddicomPublisher", TypeddicomPublisherPluginExtension.class);

        project.getPluginManager().apply(MavenPublishPlugin.class);

        addJavadocAndSourcesJarToJavaPluginExtension(project);

        addVersionInfoToJarManifest(project);

        project.getGradle().projectsEvaluated(gradle -> {
            if (project.getPluginManager().hasPlugin("java-gradle-plugin")) {
                configurePublishingAndSigningExtension(project, GRADLE_PLUGIN_PUBLICATION_NAME);
            } else {
                configurePublishingAndSigningExtension(project, STANDARD_PUBLICATION_NAME);
            }
        });
    }

    private static void addJavadocAndSourcesJarToJavaPluginExtension(@NotNull Project project) {
        JavaPluginExtension javaPluginExtension = project.getExtensions().getByType(JavaPluginExtension.class);
        javaPluginExtension.withJavadocJar();
        javaPluginExtension.withSourcesJar();
    }

    private static void addVersionInfoToJarManifest(Project project) {
        project.getTasks().withType(Jar.class, jar -> {
            jar.getManifest().attributes(Map.of(
                    "Implementation-Title", project.getName(),
                    "Implementation-Version", TYPEDDICOM_VERSION
            ));
        });
    }

    private void configurePublishingAndSigningExtension(Project project, String publicationName) {
        MavenPublication mavenPublication = configurePublishingExtensionAndReturnMavenPublication(project, publicationName);
        applyAndConfigureSigningPluginIfSigningKeyIsAvailable(project, mavenPublication);
    }

    @NotNull
    private MavenPublication configurePublishingExtensionAndReturnMavenPublication(Project project, String publicationName) {
        PublishingExtension publishing = project.getExtensions().getByType(PublishingExtension.class);
        MavenPublication publication = publishing.getPublications().maybeCreate(publicationName, MavenPublication.class);

        publication.setArtifactId(project.getName());

        if (!Objects.equals(publicationName, GRADLE_PLUGIN_PUBLICATION_NAME)) {
            publication.from(project.getComponents().getByName("java"));
        }

        publication.pom(pom -> {
            pom.getName().set(extension.getPomName());
            pom.getDescription().set(extension.getPomDescription());
            pom.getName().set("dcm4che-typeddicom-java-generator-gradleplugin");
            pom.getDescription().set("This gradle plugin provides a Task to generate Java code which provides the " +
                    "classes which are specified in the DICOM standard. The classes can be extended with custom " +
                    "tags by providing a yaml file containing the changes.");
            pom.getUrl().set("https://github.com/dcm4che/dcm4che-typeddicom");
            pom.getProperties().put("project.build.sourceEncoding", "UTF-8");
            pom.licenses(licenses -> licenses.license(license -> {
                license.getName().set("Mozilla Public License Version 2.0");
                license.getUrl().set("https://www.mozilla.org/en-US/MPL/2.0/");
            }));
            pom.developers(developers -> developers.developer(developer -> {
                developer.getId().set("Nirostar");
                developer.getName().set("Niklas Roth");
                developer.getEmail().set("36939232+nirostar@users.noreply.github.com");
            }));
            pom.scm(scm -> {
                scm.getConnection().set("scm:git:git://github.com/dcm4che/dcm4che-typeddicom.git");
                scm.getDeveloperConnection().set("scm:git:git@github.com:dcm4che/dcm4che-typeddicom.git");
                scm.getUrl().set("https://github.com/dcm4che/dcm4che-typeddicom");
            });
            pom.issueManagement(issueManagement ->
                    issueManagement.getUrl().set("https://github.com/dcm4che/dcm4che-typeddicom/issues")
            );
        });

        publishing.getRepositories().maven(repo -> {
            repo.setName("dcm4cheMaven");
            repo.setUrl("sftp://dcm4che.org:22/home/maven2");
            repo.credentials(PasswordCredentials.class);
        });

        return publication;
    }

    private static void applyAndConfigureSigningPluginIfSigningKeyIsAvailable(Project project, MavenPublication mavenPublication) {
        if (project.hasProperty("signingKey") && project.hasProperty("signingPassword")) {
            project.getPluginManager().apply(SigningPlugin.class);
            SigningExtension signing = project.getExtensions().getByType(SigningExtension.class);
            signing.useInMemoryPgpKeys(
                    Objects.requireNonNull(project.property("signingKey")).toString(),
                    Objects.requireNonNull(project.property("signingPassword")).toString()
            );
            signing.sign(mavenPublication);
        }
    }
}
