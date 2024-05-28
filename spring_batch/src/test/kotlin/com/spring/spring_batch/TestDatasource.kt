
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class TestDatasource {

    @Bean
    fun getDatasource(): DataSource {
        return DataSourceBuilder.create()
            .username("postgres")
            .password("password")
            .url("jdbc:postgresql://localhost:5432/batch_process")
            .driverClassName("org.postgresql.Driver")
            .build()
    }


}