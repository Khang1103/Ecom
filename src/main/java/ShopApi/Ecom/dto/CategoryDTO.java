package ShopApi.Ecom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private Integer id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    private String description;

    private Long productCount;
}