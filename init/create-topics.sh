#!/bin/bash
TOPIC_1="signos_vitales"

sleep 10

kafka-topics --bootstrap-server kafka-1:9092 --list | grep -q $TOPIC_1 || \
kafka-topics --bootstrap-server kafka-1:9092 --create --topic $TOPIC_1 --partitions 3 --replication-factor 1


echo "Topics creados/verificados: $TOPIC_1"