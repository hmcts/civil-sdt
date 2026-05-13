# renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.7.8
FROM hmctsprod.azurecr.io/base/java:21-distroless

USER hmcts
COPY lib/applicationinsights.json /opt/app
COPY build/libs/civil-sdt.jar /opt/app/

EXPOSE 4550
CMD [ "civil-sdt.jar" ]
