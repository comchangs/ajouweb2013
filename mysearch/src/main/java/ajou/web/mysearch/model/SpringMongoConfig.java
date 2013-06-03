package ajou.web.mysearch.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
 
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
 
@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
 
	@Override
	public String getDatabaseName() {
		return "keyword";
	}
 
	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient("54.249.102.156");
	}
}