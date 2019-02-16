package com.demo.raphaufrj.demoweblogic.controller;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class ResourceController {

	@RequestMapping(method = RequestMethod.GET)
	String readResource() {
		return readImages().toString();
	}

	@Value("${images.directory}")
	private String imagesDirectory;

	@Autowired
	private ResourcePatternResolver resourceResolver;

	private Map<String, String> readImages() {
		final Map<String, String> securityImageMap = new HashMap<>();

		try {
			final List<String> fileNames = getResourceNames();

			if (CollectionUtils.isEmpty(fileNames)) {
				System.out.println("There is no security images under " + fileNames);
			}

			for (String imageName : fileNames) {
				final String imagePath = imagesDirectory + "/" + imageName;
				System.out.println("Security image is processing. Relative path is " + imageName);

				final InputStream inputStream = new ClassPathResource(imagePath).getInputStream();
				final byte[] bytes = IOUtils.toByteArray(inputStream);
				final String encodedImage = Base64.getEncoder().encodeToString(bytes);

				securityImageMap.put(imageName, encodedImage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return securityImageMap;
	}

	public List<String> getResourceNames() throws Exception {
		System.out.println("Images directory is " + imagesDirectory);
		final Resource[] resources =
			resourceResolver.getResources("classpath:" + imagesDirectory + "/*");
		return Arrays.stream(resources).map(Resource::getFilename).collect(Collectors.toList());
	}

}
