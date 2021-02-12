package com.redhat.rhc.etp.ei.camel_ose_amqp_cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CamelRoute extends RouteBuilder {

	@SuppressWarnings("unused")
	@Inject
	private CamelContext camelContext;
	
	@Inject
	@Uri("timer://foo?period=30000")
	private Endpoint timer;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CamelRoute.class);
	
	private static final String durableSubscriptionSettings = "" + 
		"subscriptionDurable=true&" +
		"subscriptionShared=true&" +
		"subscriptionName=CamelTestConsumer";

	private static final String amqpRouteUri_event_test = "amqp-ngss:topic:event.test" +
//		"?" + durableSubscriptionSettings +
		"";

	@Inject
	@Uri(amqpRouteUri_event_test)
	private Endpoint eventTest;
	
	private static final String amqpRouteUri_ExpiryQueue = "amqp-ngss:ExpiryQueue";
	
	@Inject
	@Uri(amqpRouteUri_ExpiryQueue)
	private Endpoint expiryQueue;

	@Inject
	@Uri("amqp-ngss://cdk-test-queue")
	private Endpoint cdkTestQueue;
	
	@Override
	public void configure() throws Exception {
		LOGGER.info("Configuring routes");

		from(eventTest)
			.to("log:event.test");
		
		from(expiryQueue)
			.to("log:ExpiryQueue");
		
		from(timer).id("route-timer")
		  .setBody(constant("Hello World from CamelRoute")).id("route-setBody")
		  .to(cdkTestQueue);

		from(cdkTestQueue).id("route-cdk-test-queue")
		  .to("log:CDK-AMQ7-CDI-Test?showAll=true&multiline=true").id("route-log");
	}

}
