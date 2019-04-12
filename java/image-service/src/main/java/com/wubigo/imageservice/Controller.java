package com.wubigo.imageservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {
	@Autowired
	private Environment env;
		
	@RequestMapping("/images")
	public List<Image> getImages() {
		List<Image> images = Arrays.asList(
			new Image(1, "Treehouse", "https://wubigo.com/post/k8s-app-development-toolbox/featured_hu47d231d63492733826f3a040baad0438_760864_680x500_fill_q90_lanczos_smart1.jpg"),
			new Image(2, "The Town", "https://wubigo.com/post/blog_on_hugo/featured_hu284695fc8e59587d70e5b2d7df491030_200937_680x500_fill_q90_lanczos_smart1.jpg"),
            new Image(3, "The Last Hero", "https://wubigo.com/post/kubeamd-cheat-sheet/featured_huff6dce5c74a19d801415df9f3d509047_4091_680x500_fill_q90_lanczos_smart1.jpg")
            );
		return images;
	}
}