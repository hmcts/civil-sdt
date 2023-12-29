# renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.4.19
FROM hmctspublic.azurecr.io/base/java:17-distroless

USER hmcts
COPY lib/applicationinsights.json /opt/app
COPY build/libs/civil-sdt.jar /opt/app/

EXPOSE 4550
CMD [ "civil-sdt.jar" ]
