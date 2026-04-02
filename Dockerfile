# renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
<<<<<<< sdt-213
ARG APP_INSIGHTS_AGENT_VERSION=3.7.7
FROM hmctspublic.azurecr.io/base/java:21-distroless
=======
ARG APP_INSIGHTS_AGENT_VERSION=3.7.8
FROM hmctsprod.azurecr.io/base/java:17-distroless
>>>>>>> master

USER hmcts
COPY lib/applicationinsights.json /opt/app
COPY build/libs/civil-sdt.jar /opt/app/

EXPOSE 4550
CMD [ "civil-sdt.jar" ]
