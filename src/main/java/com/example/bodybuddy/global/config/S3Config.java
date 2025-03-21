package com.example.bodybuddy.global.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
/*	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Value("${cloud.aws.region}")
	private String region;

	@Bean
	public AmazonS3 amazonS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		return AmazonS3ClientBuilder
			.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(region)
			.build();
	}*/
}