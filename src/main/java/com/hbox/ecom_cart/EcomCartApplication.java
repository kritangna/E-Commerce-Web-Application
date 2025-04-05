package com.hbox.ecom_cart;

import com.hbox.ecom_cart.dto.OrderDto;
import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.entity.Order;
import com.hbox.ecom_cart.entity.Product;
import com.hbox.ecom_cart.repositoty.OrderItemRepository;
import com.hbox.ecom_cart.service.OrderHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
public class EcomCartApplication {

	@Bean
	public ModelMapper modelMapper(OrderItemRepository orderItemRepository)
	{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(Product.class, ProductDto.class).addMappings((mapper) ->
				mapper.map((productCategory) -> productCategory.getCategory(), ProductDto::setCategoryDto));
		return modelMapper;
	}

	public static String generateSignature(String payload, String secret) {
		try {
			Mac sha256HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			sha256HMAC.init(secretKey);

			byte[] hash = sha256HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			throw new RuntimeException("Error generating signature", e);
		}
	}
	public static void main(String[] args) {

		String testPayload = "{\"event\":\"payment.captured\",\"payload\":{\"payment\":{\"entity\":{\"id\":\"pay_29QQoUBi66xm2f\",\"method\":\"card\",\"status\":\"captured\"}}}}";
		String secretKey = "your_webhook_secret";  // Replace with your actual secret

		String signature = generateSignature(testPayload, secretKey);
		System.out.println("Generated Signature: " + signature);
//		byte[] key = new byte[32]; // 32 bytes = 256-bit key
//		new SecureRandom().nextBytes(key);
//		String secretKey = Base64.getEncoder().encodeToString(key);
//		System.out.println("Generated JWT Secret Key: " + secretKey);
		SpringApplication.run(EcomCartApplication.class, args);
//		ConfigurableApplicationContext applicationContext = SpringApplication.run(EcomCartApplication.class, args);
//		String[] list = applicationContext.getBeanDefinitionNames();
//		for (String beanName : list) {
//			System.out.println(beanName);
//		}
	}



}
