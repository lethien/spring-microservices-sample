package microservices.book.gateway.configuration;

import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

public class RibbonConfiguration {

	@Bean
	public IPing ribbonPing(final IClientConfig config) {
		return new PingUrl(false, "/application/health");
	}
	
	@Bean
	public IRule ribbonRule(final IClientConfig config) {
		// Replace the default RoundRobinRule.
		// Skip instances that don't respond
		return new AvailabilityFilteringRule();
	}
}
