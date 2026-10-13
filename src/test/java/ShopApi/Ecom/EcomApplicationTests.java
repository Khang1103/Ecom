package ShopApi.Ecom;

import ShopApi.Ecom.controller.CategoryController;
import ShopApi.Ecom.controller.ProductController;
import ShopApi.Ecom.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EcomApplicationTests {

	@Autowired
	private ProductController productController;

	@Autowired
	private CategoryController categoryController;

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertNotNull(productController);
		assertNotNull(categoryController);
		assertNotNull(userController);
	}

}
