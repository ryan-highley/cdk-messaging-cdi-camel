package com.redhat.rhc.etp.ei.camel_ose_amqp_cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.component.amqp.AMQPComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ComponentFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(ComponentFactory.class);
  private static final String amqpUrl = "amqp://192.168.42.1:5672";
  private static final String amqpUser = "amq";
  private static final String amqpPassword = "amq";

  @Produces
  @Named("amqp-ngss")
  public AMQPComponent amqpComponent() {
    LOGGER.info("Creating AMQPComponent");

    AMQPComponent amqpComponent = AMQPComponent.amqpComponent(amqpUrl, amqpUser, amqpPassword);
    amqpComponent.setConcurrentConsumers(10);
    
    return amqpComponent;
  }

}
