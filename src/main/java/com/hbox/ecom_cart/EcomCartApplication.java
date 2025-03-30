package com.hbox.ecom_cart;

import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcomCartApplication {

	@Bean
	public ModelMapper modelMapper()
	{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(Product.class, ProductDto.class).addMappings((mapper) ->
				mapper.map((productCategory) -> productCategory.getCategory(), ProductDto::setCategoryDto));
		return modelMapper;
	}

	public static void main(String[] args) {

//		byte[] key = new byte[32]; // 32 bytes = 256-bit key
//		new SecureRandom().nextBytes(key);
//		String secretKey = Base64.getEncoder().encodeToString(key);
//		System.out.println("Generated JWT Secret Key: " + secretKey);
		SpringApplication.run(EcomCartApplication.class, args);

	}



}
