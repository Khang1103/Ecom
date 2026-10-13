package ShopApi.Ecom.service;

import ShopApi.Ecom.dto.CategoryDTO;
import ShopApi.Ecom.entity.Category;
import ShopApi.Ecom.exception.ResourceNotFoundException;
import ShopApi.Ecom.repository.CategoryRepository;
import ShopApi.Ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CategoryDTO getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToDTO(category);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName().trim())) {
            throw new RuntimeException("Tên danh mục đã tồn tại!");
        }

        Category category = Category.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription())
                .build();

        return mapToDTO(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDTO updateCategory(Integer id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        String newName = dto.getName().trim();
        if (!category.getName().equalsIgnoreCase(newName) && categoryRepository.existsByName(newName)) {
            throw new RuntimeException("Tên danh mục đã tồn tại!");
        }

        category.setName(newName);
        category.setDescription(dto.getDescription());

        return mapToDTO(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        long count = productRepository.countByCategoryId(id);
        if (count > 0) {
            throw new RuntimeException("Không thể xóa danh mục đang chứa " + count + " sản phẩm! Vui lòng chuyển hoặc xóa sản phẩm trước.");
        }

        categoryRepository.delete(category);
    }

    private CategoryDTO mapToDTO(Category category) {
        long count = productRepository.countByCategoryId(category.getId());
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .productCount(count)
                .build();
    }
}
