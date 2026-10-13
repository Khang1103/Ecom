package ShopApi.Ecom.dto;

import lombok.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequest {
    @Pattern(
            regexp = "PENDING|PROCESSING|SHIPPED|DELIVERED|CANCELLED",
            message = "Trạng thái đơn hàng không hợp lệ"
    )
    private String status;

    @Size(max = 100, message = "Mã vận đơn không được vượt quá 100 ký tự")
    private String trackingNumber;
}
