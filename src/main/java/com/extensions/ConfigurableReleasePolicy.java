package com.extensions;

import org.apache.maven.shared.release.policy.PolicyException;
import org.apache.maven.shared.release.policy.version.VersionPolicy;
import org.apache.maven.shared.release.policy.version.VersionPolicyRequest;
import org.apache.maven.shared.release.policy.version.VersionPolicyResult;
import org.apache.maven.shared.release.versions.DefaultVersionInfo;
import org.apache.maven.shared.release.versions.VersionParseException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.StringUtils;

/**
 * Created by atagrawal on 7/13/18.
 */
@Component(role = VersionPolicy.class,
        hint = "configurable",
        description = "Adds suffix to release version. E.g. -beta")
public class ConfigurableReleasePolicy implements VersionPolicy {

    @Override
    public VersionPolicyResult getReleaseVersion(VersionPolicyRequest versionPolicyRequest)
            throws PolicyException, VersionParseException {

        return new VersionPolicyResult()
                .setVersion(getDefaultVersionInfo(versionPolicyRequest).getNextVersion().getReleaseVersionString());
    }

    @Override
    public VersionPolicyResult getDevelopmentVersion(VersionPolicyRequest versionPolicyRequest) throws PolicyException, VersionParseException {
        DefaultVersionInfo defaultVersionInfo = new DefaultVersionInfo(versionPolicyRequest.getVersion());

        return new VersionPolicyResult()
                .setVersion(getDefaultVersionInfo(versionPolicyRequest).getNextVersion().getSnapshotVersionString());

    }

    private DefaultVersionInfo getDefaultVersionInfo(VersionPolicyRequest versionPolicyRequest) throws PolicyException, VersionParseException{
        DefaultVersionInfo defaultVersionInfo = new DefaultVersionInfo(versionPolicyRequest.getVersion());

        String annotation = System.getProperty("annotation");
        String annotationSeparator = System.getProperty("annotationSeparator");
        if(StringUtils.isEmpty(annotation)) {
            annotation = defaultVersionInfo.getAnnotation();
        } else {
            if (StringUtils.isEmpty(annotationSeparator)) {
                annotationSeparator = "-";
            }
        }

        String annotationRevision =  System.getProperty("annotationRevision");
        String annotationRevSeparator = System.getProperty("annotationRevSeparator");
        if(StringUtils.isEmpty(annotationRevision)) {
            annotationRevision = defaultVersionInfo.getAnnotationRevision();
        } else {
            if(StringUtils.isEmpty(annotationRevSeparator)) {
                annotationRevSeparator = "-";
            }
        }

        String buildSpecifier = System.getProperty("buildSpecifier");
        String buildSeparator = System.getProperty("buildSeparator");
        if(StringUtils.isEmpty(buildSpecifier)) {
            buildSpecifier = defaultVersionInfo.getBuildSpecifier();
            buildSeparator = "-";
        } else {
            if (StringUtils.isEmpty(buildSeparator)) {
                buildSeparator = "-";
            }
        }


        return new DefaultVersionInfo(defaultVersionInfo.getDigits(), annotation,
                annotationRevision, buildSpecifier,
                annotationSeparator, annotationRevSeparator, buildSeparator);
    }
}
