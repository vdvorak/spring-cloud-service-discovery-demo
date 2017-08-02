package info.developerblog.spring.cloud.marathon;

import info.developerblog.spring.cloud.marathon.actuator.MarathonEndpoint;
import info.developerblog.spring.cloud.marathon.actuator.MarathonHealthIndicator;
import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.MarathonClient;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConditionalOnMarathonEnabled
@AutoConfigureBefore(CommonsClientAutoConfiguration.class)
public class MarathonAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MarathonProperties marathonProperties() {
        return new MarathonProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public Marathon marathonClient(MarathonProperties properties) {
        return MarathonClient.getInstance(properties.getEndpoint());
    }

    @Configuration
    @ConditionalOnClass(Endpoint.class)
    protected static class MarathonHealthConfig {
        @Bean
        public MarathonEndpoint marathonEndpoint(Marathon marathon) {
            return new MarathonEndpoint(marathon);
        }

        @Bean
        public MarathonHealthIndicator marathonHealthIndicator(Marathon marathon) {
            return new MarathonHealthIndicator(marathon);
        }
    }
}
