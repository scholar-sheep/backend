FROM docker.elastic.co/elasticsearch/elasticsearch:7.10.0

COPY elasticsearch-analysis-ik-7.10.0.zip /usr/share/elasticsearch

RUN unzip /usr/share/elasticsearch/elasticsearch-analysis-ik-7.10.0.zip -d /usr/share/elasticsearch/plugins/ik/ \
    && rm -f /usr/share/elasticsearch/elasticsearch-analysis-ik-7.10.0.zip